package me.MrAxe.BeastSpawners.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import net.milkbowl.vault.economy.Economy;

public class Utils {

	private BeastSpawners pl ;
	static BeastSpawners beastSpawners = BeastSpawners.getInstance();
	PlayerUtils playerUtils;

	private SpawnerUtils spawnerUtils;

	public Utils(BeastSpawners pl){
		this.pl = pl;
		playerUtils = new PlayerUtils(pl);
		spawnerUtils = new SpawnerUtils(pl);
		Bukkit.getScheduler().runTaskLaterAsynchronously(pl, new Runnable() {
			@Override
			public void run() {

						//Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&4Beast&bTokens&7] Please Update to Latest Version from SpigotMc! &4Version: "+ version));
						//pl.getServer().getPluginManager().disablePlugin(pl);


			}
		},12000);
	}
	

	

	public PlayerUtils getPlayerUtils() {
		return playerUtils;		
	}

	public static void setupEconomy(){
		if(beastSpawners.getServer().getPluginManager().getPlugin("Vault") != null){
			RegisteredServiceProvider<Economy> rsp = beastSpawners.getServer().getServicesManager().getRegistration(Economy.class);
			if(rsp != null){
				beastSpawners.econ = rsp.getProvider();
			}
		}
	}

	public String getPrefix() {
		String prefix = ChatColor.translateAlternateColorCodes('&', pl.getMsg().getConfig().getString("Prefix"));
		return prefix;
	}
	public String getEntityType(String s){
		return s;

	}

	public static boolean isInt(String value) {
		try {
			Integer.parseInt(value);
		}
		catch (NumberFormatException efr) {
			return false;
		}
		return true;
	}
	public static String formatNumber(double number){
		return String.valueOf(NumberFormat.getInstance().format(number));

	}
	public void noPermission(Player p) {
		p.sendMessage(getPrefix() + ChatColor.translateAlternateColorCodes('&', pl.getMsg().getConfig().getString("NoPermission")));
	}
	public static String formatDouble(double number) {
		return String.valueOf(NumberFormat.getInstance().format(number));
	}
	public static String formatInt(int number) {
		return String.valueOf(NumberFormat.getInstance().format(number));
	}


	public SpawnerUtils getSpawnerUtils() {
		return spawnerUtils;
	}

	

}
