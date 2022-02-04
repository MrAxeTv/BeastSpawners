package me.MrAxe.BeastSpawners.FileManager;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.MrAxe.BeastSpawners.BeastSpawners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;





public class YmlCreator{

	private BeastSpawners pl;
	private String name;

	public YmlCreator(BeastSpawners plugin,String n){
		pl = plugin;
		name = n;  
		createFiles(name);
		configUpdate();
	}



	public File configf;
	public FileConfiguration config;






	public void reloadConfig(){
		createFiles(name);

	}
	public FileConfiguration getConfig() {
		return config;
	}
	public void saveConfig(){
		try {
			config.save(configf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public void createFiles(String name) {



		configf = new File(pl.getDataFolder(), name);

		if (!configf.exists()) {
			configf.getParentFile().mkdirs();
			pl.saveResource(name, false);
		}


		config = new YamlConfiguration();

		try {
			config.load(configf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void configUpdate() {
		Reader oldConfigStream = null;
		YamlConfiguration oldConfig = null;
		try {
			oldConfigStream = new InputStreamReader(pl.getResource(name), "UTF-8");
			if (oldConfigStream != null) {
				oldConfig = YamlConfiguration.loadConfiguration(oldConfigStream);
			}} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(getConfig().getDouble("ConfigVersion") < oldConfig.getDouble("ConfigVersion") || !getConfig().isSet("ConfigVersion")) {
			File file;
			double oldVersion = getConfig().getDouble("ConfigVersion");
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH.mm.ss");
			Date date = new Date();

			file = new File(pl.getDataFolder(), name);
			boolean renameResult = file.renameTo(new File(pl.getDataFolder(), name.replaceAll("\\.yml", "")+"_"+ dateFormat.format(date) + "_old.yml"));
			if(renameResult) {
				pl.saveResource(name, false);
				createFiles(name);
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&4Beast&bSpawners&7] &4Old "+ name+" " +oldVersion+" has been replaced with new version!"));

			}else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&4Beast&bSpawners&7] &4Server has faild to replace old version of "+ name+" please conntact author MrAxeTv!"));
			}
		}
	}


}
