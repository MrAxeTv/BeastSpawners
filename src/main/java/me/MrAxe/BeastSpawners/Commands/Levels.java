package me.MrAxe.BeastSpawners.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.XpLevel;
import me.MrAxe.BeastSpawners.Utils.Utils;

public class Levels implements CommandExecutor {

	private BeastSpawners pl;
	List<String> glist;

	public Levels(BeastSpawners plugin) {
		pl = plugin;
		glist = new ArrayList<String>();
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


		for (String s : pl.getMsg().getConfig().getStringList("Levels.SpawnerLevels")) {
			s = ChatColor.translateAlternateColorCodes('&', s);
			sender.sendMessage(s);

		}

		if (glist.isEmpty()){
			int maxLv = pl.getLdata().getConfig().getConfigurationSection("Levels").getKeys(false).size();
			for (int i = 1; i <= maxLv; i++) {
				List<String> list = pl.getUtils().getSpawnerUtils().getList(i);

				String s = pl.getMsg().getConfig().getString("Levels.LevelsList");
				s = s.replaceAll("%lv%", ""+i);
				s = s.replaceAll("%cost%", ""+Utils.formatInt(pl.getLdata().getConfig().getInt("Levels."+i+".Exp")));
				s = s.replaceAll("%moneycost%", ""+Utils.formatDouble(pl.getLdata().getConfig().getDouble("Levels."+i+".Money")));
				s = s.replaceAll("%type%", ""+list);
				s = ChatColor.translateAlternateColorCodes('&', s);
				glist.add(s);


			}
		}
		for(String s : glist){
			sender.sendMessage(s);	
		}
		sender.sendMessage("");
		String s = pl.getMsg().getConfig().getString("Levels.UpgradeSpawner");
		s = ChatColor.translateAlternateColorCodes('&', s);
		sender.sendMessage(s);


		return false;
	}

}
