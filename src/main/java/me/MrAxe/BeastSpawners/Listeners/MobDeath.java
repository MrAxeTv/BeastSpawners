package me.MrAxe.BeastSpawners.Listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import me.MrAxe.BeastSpawners.BeastSpawners;



public class MobDeath implements Listener {
	private BeastSpawners  pl;
	public MobDeath(BeastSpawners plugin){
		pl = plugin;

	}
	

	
	public void MobRemove(){
		
		for(World world : Bukkit.getServer().getWorlds()){
			for(Entity e : world.getEntities()){

				if(e.hasMetadata("SS")){
					e.remove();	
				}
			}
		}
	}
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e){


		if(e.getSpawnReason()==SpawnReason.SPAWNER){
			
            if(pl.getUtils().getSpawnerUtils().isSpawnerLocked(e.getEntityType().toString())) {
			e.getEntity().setMetadata("SS",new FixedMetadataValue(pl, 1));
            }
		}

	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent e){
		
		
		if (e.getEntity().hasMetadata("SS")){
			if(!(e.getDamager() instanceof Player)) return;
			int lv = pl.getUtils().getPlayerUtils().getLevel(e.getDamager().getUniqueId());
			int mlv = pl.getUtils().getSpawnerUtils().getSpawnerLv(e.getEntityType().toString());
			String n = "%%__USER__%%";

			if(lv < mlv){
				
				String s;
				s = pl.getMsg().getConfig().getString("Events.RequiredLv");
				s = s.replaceAll("%lv%", ""+mlv);
				s = ChatColor.translateAlternateColorCodes('&', s);
				e.getDamager().sendMessage(pl.getUtils().getPrefix() + s);
				e.setCancelled(true);
			}
		
			}
	}
}
