package me.MrAxe.BeastSpawners.Commands;

import me.MrAxe.BeastSpawners.Utils.ItemManager;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.Utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.Console;


public class BeastSpawnersCMD implements CommandExecutor {

	private BeastSpawners pl;
	private ItemManager itemManager;

	public BeastSpawnersCMD(BeastSpawners plugin) {
		pl = plugin;
		itemManager = new ItemManager(pl);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


		if(!sender.hasPermission("BeastSpawner.Admin")) {
			pl.getUtils().noPermission((Player) sender);
			return false;
		}

		if(args.length == 0 || args[0].equalsIgnoreCase("Help")) {
			
			for(String s : pl.getMsg().getConfig().getStringList("Staff.Help")) {
			
			s = ChatColor.translateAlternateColorCodes('&', s);

			sender.sendMessage(s);
			}
			return false;
		}

		if(args.length == 1) {

			if(args[0].equalsIgnoreCase("reload")) {

				pl.RegisterConfigs();
				pl.reloadConfig();
				sender.sendMessage(pl.getUtils().getPrefix()+"Config files are reloaded!");
				return false;		
			}
			if(args[0].equalsIgnoreCase("level")) {
				String msg = pl.getMsg().getConfig().getString("Staff.LevelCmd");
				msg = ChatColor.translateAlternateColorCodes('&', msg);

				sender.sendMessage(pl.getUtils().getPrefix() + msg);
				return false;
			}
		}


		if(args.length == 2) {

			if(args[0].equalsIgnoreCase("level")){
				OfflinePlayer p = pl.getServer().getPlayer(args[1]);
				if(!p.isOnline()) {
					sender.sendMessage(pl.getUtils().getPrefix()+"Player is not Online!");
					return false;
				}
				int lv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
				String msg = pl.getMsg().getConfig().getString("Staff.PlayerLevel");
				msg = msg.replaceAll("%player%", p.getName());
				msg = msg.replaceAll("%lv%", ""+lv);
				msg = ChatColor.translateAlternateColorCodes('&', msg);

				sender.sendMessage(pl.getUtils().getPrefix() + msg);

			}
			if(args[0].equalsIgnoreCase("get")){

				if(!(sender instanceof Player)){
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix()+"&cConsole can't use this command!"));
					return false;
				}
				Player p = (Player)sender;



				if(!Type.isMobType(args[1])){
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix()+"&c"+args[1] + " is wrong name of spawner!"));
					return false;
				}
				ItemStack item = itemManager.getSpawnerItem(args[1],1,true);
				p.getInventory().addItem(item);
				Type type = Type.fromName(args[1]);

				sender.sendMessage(ChatColor.translateAlternateColorCodes
						('&', pl.getUtils().getPrefix()+"&6You got &bx1 " + type.getDisplayName() + " &6Spawner!"));
				return false;

			}
		}

		if(args.length == 3) {
			if(args[0].equalsIgnoreCase("level")){
				OfflinePlayer p = pl.getServer().getPlayer(args[1]);
				if(!p.isOnline()) {
					sender.sendMessage(pl.getUtils().getPrefix()+"Player is not Online!");
					return false;
				}
				if(Utils.isInt(args[2])) {	
					if (pl.getLdata().getConfig().getConfigurationSection("Levels").getKeys(false).size() < Integer.parseInt(args[2]) ){
						String s = pl.getMsg().getConfig().getString("Staff.WrongLevel");
						s = ChatColor.translateAlternateColorCodes('&', s);
						sender.sendMessage(pl.getUtils().getPrefix() + s);
						return false;

					}
					pl.getUtils().getPlayerUtils().setLevel( p.getUniqueId(), Integer.parseInt(args[2]));
					String lv = args[2];
					String msg = pl.getMsg().getConfig().getString("Staff.NewLevel");
					msg = msg.replaceAll("%player%", p.getName());
					msg = msg.replaceAll("%lv%", lv);
					msg = ChatColor.translateAlternateColorCodes('&', msg);

					sender.sendMessage(pl.getUtils().getPrefix() +msg);
					return false;

				}else {
					String msg = pl.getMsg().getConfig().getString("Staff.NotNumber");
					msg = ChatColor.translateAlternateColorCodes('&', msg);

					sender.sendMessage(pl.getUtils().getPrefix() +msg);
					return false;

				}
			}


			if(args[0].equalsIgnoreCase("give")){
				Player p = null;
				if(!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
					sender.sendMessage(pl.getUtils().getPrefix()+ args[1] +" is not Online!");
					return false;
				}
				else p = pl.getServer().getPlayer(args[1]);

				if(!Type.isMobType(args[2])){
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix()+"&c"+args[2] + " is wrong name of spawner!"));
					return false;
				}
				ItemStack item = itemManager.getSpawnerItem(args[2],1,true);
				p.getInventory().addItem(item);
				Type type = Type.fromName(args[2]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes
						('&', pl.getUtils().getPrefix()+"&6Giving &bx1 " +""+type.getDisplayName() + " &6Spawner to &b"+ args[1]));
				return false;
				}
			if(args[0].equalsIgnoreCase("get")){

				if(!(sender instanceof Player)){
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix()+"&cConsole can't use this command!"));
					return false;
				}
				Player p = (Player)sender;


				if(!Type.isMobType(args[1])){
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix()+"&c"+args[1] + " is wrong name of spawner!"));
					return false;
				}
				if (!Utils.isInt(args[2])) {
					String s = pl.getMsg().getConfig().getString("Staff.NotNumber");
					s = ChatColor.translateAlternateColorCodes('&',s);
					sender.sendMessage(pl.getUtils().getPrefix()+s);
					return false;
				}
				if (Integer.parseInt(args[2] )< 1) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix() + "&cAmount can't be less then 1!"));
					return false;
				}
				ItemStack item = itemManager.getSpawnerItem(args[1],Integer.parseInt(args[2]),true);
				p.getInventory().addItem(item);
				Type type = Type.fromName(args[1]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes
						('&', pl.getUtils().getPrefix()+"&6You got &bx"+ args[2]+" " + ""+type.getDisplayName() + " &6Spawner(s)!"));
				return false;

			}

			return false;

		}
		if(args.length == 4){
			if(args[0].equalsIgnoreCase("give")) {
				Player p = null;
				if (!Bukkit.getOfflinePlayer(args[1]).isOnline()) {
					sender.sendMessage(pl.getUtils().getPrefix() + args[1] + " is not Online!");
					return false;
				} else p = pl.getServer().getPlayer(args[1]);

				if (!Type.isMobType(args[2])) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix() + "&c" + args[2] + " is wrong name of spawner!"));
					return false;
				}
				if (!Utils.isInt(args[3])) {
					String s = pl.getMsg().getConfig().getString("Staff.NotNumber");
					s = ChatColor.translateAlternateColorCodes('&',s);
					sender.sendMessage(pl.getUtils().getPrefix()+s);
					return false;
				}
				if (Integer.parseInt(args[3] )< 1) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes
							('&', pl.getUtils().getPrefix() + "&cAmount can't be less then 1!"));
					return false;
				}

				ItemStack item = itemManager.getSpawnerItem(args[2], Integer.parseInt(args[3]), true);
				p.getInventory().addItem(item);
				Type type = Type.fromName(args[2]);
				sender.sendMessage(ChatColor.translateAlternateColorCodes
						('&', pl.getUtils().getPrefix() + "&6Giving &bx"+args[3]+" " + ""+type.getDisplayName() + " &6Spawner(s) to &b" + args[1]));
				return false;
			}
		}

		for(String s : pl.getMsg().getConfig().getStringList("Staff.Help")) {

			s = ChatColor.translateAlternateColorCodes('&', s);

			sender.sendMessage(s);
		}
		return false;
	}

}
