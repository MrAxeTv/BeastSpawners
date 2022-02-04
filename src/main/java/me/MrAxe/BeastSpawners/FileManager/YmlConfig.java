package me.MrAxe.BeastSpawners.FileManager;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import me.MrAxe.BeastSpawners.BeastSpawners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

import me.MrAxe.BeastSpawners.Utils.Version;






public class YmlConfig {

	private File customConfigFile;
	private FileConfiguration customConfig;
	private BeastSpawners pl;

	/* @Override
    public void onEnable(){
        createCustomConfig();
    }*/
	public YmlConfig(BeastSpawners pl) {
		this.pl= pl;
		try {
			saveDeafultConfig();
			configUpdate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return this.customConfig;
	}

	public void saveDeafultConfig() throws IOException {
		customConfigFile = new File(pl.getDataFolder(), "config.yml");
		if (!customConfigFile.exists()) {
			customConfigFile.getParentFile().mkdirs();
			//Getting proper version of file 1.8 or 1.13+
            File temp;  
			if(pl.isServerVersionAtLeast(Version.V1_13)) {
            	  pl.saveResource("configs\\1.13\\config.yml", false);
            	  temp = new File(pl.getDataFolder()+"/configs/1.13/config.yml");
            	  temp.renameTo(customConfigFile);  
              }else {
            	  pl.saveResource("configs\\1.8\\config.yml", false);
            	  temp = new File(pl.getDataFolder()+"/configs/1.8/config.yml");
            	  temp.renameTo(customConfigFile);       	      	  
              }
          deleteDirectory( new File(pl.getDataFolder()+"/configs"));

		}
         //Loading File
		customConfig= new YamlConfiguration();
		try {
			customConfig.load(customConfigFile);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	//Deleting not needed copies 
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	private void configUpdate() throws IOException {
		File newConfigStream = null;
		YamlConfiguration newConfig = null;
		pl.saveResource("configs\\1.13\\config.yml", false);
		pl.saveResource("configs\\1.8\\config.yml", false);
		newConfigStream = new File(pl.getDataFolder()+"/configs/1.13/config.yml");
		if (newConfigStream != null) {
			newConfig = YamlConfiguration.loadConfiguration(newConfigStream);
			deleteDirectory( new File(pl.getDataFolder()+"/configs"));
		}


		if(getConfig().getDouble("ConfigVersion") < newConfig.getDouble("ConfigVersion") || !getConfig().isSet("ConfigVersion")) {
			File file;
			double oldVersion = getConfig().getDouble("ConfigVersion");
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH.mm.ss");
			Date date = new Date();

			file = new File(pl.getDataFolder(), "config.yml");
			boolean renameResult = file.renameTo(new File(pl.getDataFolder(), "config_"+ dateFormat.format(date) + "_old.yml"));
			if(renameResult) {
				if(pl.isServerVersionAtLeast(Version.V1_13)) {
					pl.saveResource("configs\\1.13\\config.yml", false);
					file = new File(pl.getDataFolder()+"/configs/1.13/config.yml");
					file.renameTo(customConfigFile);
				}else {
					pl.saveResource("configs\\1.8\\config.yml", false);
					file = new File(pl.getDataFolder()+"/configs/1.8/config.yml");
					file.renameTo(customConfigFile);
				}
				try {
					customConfig.load(customConfigFile);
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
				deleteDirectory( new File(pl.getDataFolder()+"/configs"));
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&4Beast&bSpawners&7] &4Old config.yml " +oldVersion+" has been replaced with new version!"));

			}else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&4Beast&bSpawners&7] &4Server has faild to replace old version of config.yml please conntact author MrAxeTv!"));
			}
		}
	}



}
