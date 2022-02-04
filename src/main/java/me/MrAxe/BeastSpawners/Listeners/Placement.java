package me.MrAxe.BeastSpawners.Listeners;



import me.MrAxe.BeastSpawners.Utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;

import org.bukkit.block.CreatureSpawner;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.nbtinjector.NBTInjector;
import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.WorldUtils.Type;



public class Placement implements Listener {

	private BeastSpawners pl;
	String stack;

	public Placement(BeastSpawners plugin){
		pl = plugin;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void spawnerPlace(PlayerInteractEvent e){
		if(e.isCancelled()) {
			pl.removeSpawnerInHand(e.getPlayer().getName());
			return;
		}
		if ((e.getAction().equals(Action.RIGHT_CLICK_AIR)) ||(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) ){
			Player p = e.getPlayer();
			if (p.getItemInHand().getType().equals((pl.isServerVersionAtLeast(Version.V1_13) ? Material.SPAWNER : Material.valueOf("MOB_SPAWNER")))){
				if(pl.hasSpawnerInHand(p.getName())){
					e.setCancelled(true);
					return;
				}


				NBTItem nbt = new NBTItem(p.getItemInHand());
				String s = "";
				if(nbt.hasNBTData()) {
					if(nbt.hasKey("BlockEntityTag") && nbt.getCompound("BlockEntityTag").hasKey("EntityId")) {
						s = nbt.getCompound("BlockEntityTag").getString("EntityId");
						pl.setSpawnerInHand(p.getName(), s);
						//Bukkit.broadcastMessage("EntityId: "+s);
					}
					if(nbt.hasKey("BlockEntityTag") && nbt.getCompound("BlockEntityTag").hasKey("SpawnData")) {
						s = nbt.getCompound("BlockEntityTag").getCompound("SpawnData").getString("id");
						//Bukkit.broadcastMessage("id: " +s);
						pl.setSpawnerInHand(p.getName(), s);
					}
				}
				if(!p.hasPermission("BeastSpawners.place."+Type.fromName(pl.getSpawnerInHand(p.getName())).name())){

					e.setCancelled(true);
					String m = pl.getMsg().getConfig().getString("Events.Placing.NoPermission");
					m = m.replaceAll("%mob%", Type.fromName(pl.getSpawnerInHand(p.getName())).getDisplayName());
					m = ChatColor.translateAlternateColorCodes('&',m);
					p.sendMessage(pl.getUtils().getPrefix()+ m);
					pl.removeSpawnerInHand(p.getName());
					return;

				}



				int lv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
				String typeName = pl.getSpawnerInHand(e.getPlayer().getName());
				Type type = Type.fromName(typeName);

				if(type == null) return;
				
				//Skip level check if mob is not specified in config file
				if(!pl.getUtils().getSpawnerUtils().isSpawnerLocked(type.name())) return;


				//If player level is bigger then mob level just return
				if (pl.getUtils().getSpawnerUtils().getSpawnerLv(type.toString()) <= lv) return;

				e.setCancelled(true);
				p.updateInventory();
				//Remove player from list of having spawner in hand
				if(pl.hasSpawnerInHand(p.getName())) pl.removeSpawnerInHand(p.getName());
				//To small player level message
				s = pl.getMsg().getConfig().getString("Events.Placing.LowLevel");
				s = ChatColor.translateAlternateColorCodes('&', s);
				p.sendMessage(pl.getUtils().getPrefix() + s);

				return;

			}
		}

		//}
	}
	//@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSpawner(BlockPlaceEvent e) {
		if(!e.getBlock().getType().equals((pl.isServerVersionAtLeast(Version.V1_13) ? Material.SPAWNER : Material.valueOf("MOB_SPAWNER")))) return;
		if(e.isCancelled()){
			pl.removeSpawnerInHand(e.getPlayer().getName());
			return;
		}
		final Block b = e.getBlock();

		CreatureSpawner cs = (CreatureSpawner) b.getState();
		if(!pl.hasSpawnerInHand(e.getPlayer().getName())) return;


		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			
			@Override
			public void run() {

				if(e.getBlock().getLocation().getBlock().equals(b)) {
					if(!e.isCancelled()) {


						if(pl.hasSpawnerInHand(e.getPlayer().getName())) {

							//Bukkit.broadcastMessage(type.name());
							Type type = Type.fromName(pl.getSpawnerInHand(e.getPlayer().getName()));
							cs.setSpawnedType(EntityType.valueOf(type.name()));


							cs.update(true, true);
							pl.removeSpawnerInHand(e.getPlayer().getName());
							String s = pl.getMsg().getConfig().getString("Events.Placing.PlacedSpawner");
							s = s.replaceAll("%mob%", type.getDisplayName());
							s = ChatColor.translateAlternateColorCodes('&',s);
							e.getPlayer().sendMessage(pl.getUtils().getPrefix()+s);

						}

					NBTCompound comp = NBTInjector.getNbtData(b.getState());
					comp.setString("Foo", "Bar");
					}
				}
				
			}
		}, 1);
		

	}
}




