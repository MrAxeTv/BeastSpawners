package me.MrAxe.BeastSpawners.Commands;

import java.util.Set;

import me.MrAxe.BeastSpawners.Utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.XpLevel;
import me.MrAxe.BeastSpawners.Utils.Utils;



public class Level implements CommandExecutor {

	private BeastSpawners pl;

	public Level(BeastSpawners plugin) {
		pl = plugin;
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(pl.getUtils().getPrefix()+ "Console can't use this command!");
			return false;
		}

		Player p = (Player) sender;



		if(pl.getHooks().isASkyBlockOn() || pl.getHooks().isSSkyBlockOn()) {
			if(!pl.getUtils().getPlayerUtils().hasIsland(p.getUniqueId())) {
				String s = pl.getMsg().getConfig().getString("LevelUp.Hooks.SkyBlockNoIsland");
				s = ChatColor.translateAlternateColorCodes('&', s);
				s = s.replaceAll("%player%", p.getName());
				p.sendMessage(pl.getUtils().getPrefix()+s);
				return false;
			}
			}
		

		//Bukkit.broadcastMessage("legacy:"+ EntityType.IRON_GOLEM.getName()+"   "+ p.getTargetBlock((Set<Material>) null, 5).getType());
	    if(p.getTargetBlock((Set<Material>) null, 5).getType() == (pl.isServerVersionAtLeast(Version.V1_13) ? Material.SPAWNER : Material.valueOf("MOB_SPAWNER")))
	    {
	    	Block b = p.getTargetBlock((Set<Material>) null, 5);
			//Bukkit.broadcastMessage(""+block);
	    	NBTTileEntity comp = new NBTTileEntity(b.getState());

			//CreatureSpawner sp = (CreatureSpawner) block.getState();
			//Bukkit.broadcastMessage("SpawnedType:"+ sp.getSpawnedType());
			//Bukkit.broadcastMessage("CreatureTypeName:"+ sp.getCreatureTypeName());

			
			
			
			//Bukkit.broadcastMessage("CreatureTypeID:"+ sp.getCreatureTypeId());
			//Bukkit.broadcastMessage("CreatureType:"+ sp.getCreatureType());
			//Bukkit.broadcastMessage(""+comp);
			
	     
	    }
		

		
		int xp = XpLevel.getExp(p);
		int lv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
		int nextLv = lv+ 1;
		int cost = pl.getLdata().getConfig().getInt("Levels."+nextLv+".Exp");
		double moneyCost = pl.getLdata().getConfig().getDouble("Levels."+nextLv+".Money");
		String s;	
		s = pl.getMsg().getConfig().getString("Level.CurrentLv");
		s = s.replaceAll("%cost%", ""+Utils.formatInt(cost));
		s = s.replaceAll("%moneycost%", ""+Utils.formatDouble(moneyCost));
		s = s.replaceAll("%xp%", ""+Utils.formatInt(xp));
		s = s.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));
		s=s.replaceAll("%lv%", ""+lv);
		s = ChatColor.translateAlternateColorCodes('&', s);
		p.sendMessage(pl.getUtils().getPrefix() + s);



		if (pl.getLdata().getConfig().contains("LevelCost."+nextLv)){

			s = pl.getMsg().getConfig().getString("Level.NextLevelCost");
			s = s.replaceAll("%cost%", ""+Utils.formatInt(cost));
			s = s.replaceAll("%moneycost%", ""+Utils.formatDouble(moneyCost));
			s = s.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));
			s=s.replaceAll("%lv%", ""+nextLv);
			s = ChatColor.translateAlternateColorCodes('&', s);
			p.sendMessage(pl.getUtils().getPrefix() + s);

		}


		return false;
	}

}
