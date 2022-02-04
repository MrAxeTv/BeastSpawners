package me.MrAxe.BeastSpawners.FileManager;

import me.MrAxe.BeastSpawners.BeastSpawners;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;


public class PlayerYml {
	
	BeastSpawners pl;
	UUID u;
	File userFile;
	FileConfiguration userConfig;
	
	public PlayerYml(UUID u, BeastSpawners plugin){
       this.pl = plugin;
        this.u = u;
        
        userFile = new File(pl.getDataFolder()+"/Players",u + ".yml");
        userConfig = YamlConfiguration.loadConfiguration(userFile);
        
        createFile();

}
	
	public boolean isExists() {
		
		
		return userFile.exists();
	}
	
    public void createFile(){

        if ( !(userFile.exists()) ) {
            try {


                YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);
                OfflinePlayer p = pl.getServer().getOfflinePlayer(u);
                userConfig.set("Name", p.getName());
                userConfig.set("UUID", u.toString());
                userConfig.set("Level", 1);

                userConfig.save(userFile);



            } catch (Exception e) {

                e.printStackTrace();
         
            }
        }
    }
    
    
    public FileConfiguration getFile(){

        return userConfig;

    }


    public void saveFile(){

        try {

            getFile().save(userFile);

        } catch(Exception e) {
        	
        	pl.getLogger().info("ERROR WITH SAVING PLAYER LEVEL UUID: " + u+", PLEASE REPORT THIS TO AUTHOR OF THE PLUGIN");
            e.printStackTrace();

        }
        }
	

}
