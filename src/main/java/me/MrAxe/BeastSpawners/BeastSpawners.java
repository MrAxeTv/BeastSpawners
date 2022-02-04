package me.MrAxe.BeastSpawners;
import java.util.HashMap;


import me.MrAxe.BeastSpawners.Commands.BeastSpawnersCMD;
import me.MrAxe.BeastSpawners.Commands.Tab.BeastSpawnersTab;
import me.MrAxe.BeastSpawners.FileManager.YmlConfig;
import me.MrAxe.BeastSpawners.FileManager.YmlCreator;
import me.MrAxe.BeastSpawners.Listeners.*;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.MrAxe.BeastSpawners.Commands.Level;
import me.MrAxe.BeastSpawners.Commands.LevelUp;
import me.MrAxe.BeastSpawners.Commands.Levels;
import me.MrAxe.BeastSpawners.Listeners.Hooks.ASkyBlockEvents;
import me.MrAxe.BeastSpawners.Listeners.Hooks.SSkyBlockEvents;
import me.MrAxe.BeastSpawners.PlaceholderAPI.PlaceHolderAPI;
import me.MrAxe.BeastSpawners.PlaceholderAPI.PlaceholderMVdW;
import me.MrAxe.BeastSpawners.Utils.Utils;
import me.MrAxe.BeastSpawners.Utils.Version;
import net.milkbowl.vault.economy.Economy;
import de.tr7zw.nbtinjector.NBTInjector;


public class BeastSpawners extends JavaPlugin {
	YmlCreator messages;
	YmlCreator levels;
	private Utils ut;
	private MobDeath md;
	private static BeastSpawners instance;
	public Economy econ;
	PlaceholderMVdW ph;
	private HashMap<String,String> spawnerId;
	
	private Version version = Version.fromPackageName(Bukkit.getServer().getClass().getPackage().getName());
	private YmlConfig config;
	private Hooks hooks;

	@Override
	public void onLoad() {
		getLogger().info("Injecting custom NBT");
		try {
			NBTInjector.inject();
			getLogger().info("Injected!");
		} catch (Throwable ex) {

			getLogger().info("Error while Injecting custom Tile/Entity classes!"+ ex);
		}
	}
	

	@Override
	public void onEnable() {
		
		spawnerId = new HashMap<String, String>();
		
		instance = this;
		RegisterConfigs();
		RegisterClass();
		RegisterCommands();
		RegisterEvents();



	}
	@Override
	public void onDisable() {
		md.MobRemove();



	}
	public void RegisterClass(){
		ut = new Utils(this);
		md = new MobDeath(this);
		hooks = new Hooks(this);
		checkMVd();
		checkPapi();

	}

	public void RegisterCommands(){
		getCommand("levelup").setExecutor(new LevelUp(this));
		getCommand("level").setExecutor(new Level(this));
		getCommand("Levels").setExecutor(new Levels(this));
		getCommand("BeastSpawners").setExecutor(new BeastSpawnersCMD(this));
		getCommand("BeastSpawners").setTabCompleter(new BeastSpawnersTab(this));
	}
	public void RegisterEvents(){
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new Placement(this), this);
		pm.registerEvents(new MobDeath(this), this);
		pm.registerEvents(new ClickGUI(this), this);
		pm.registerEvents(new SilkSpawners(this), this);
		pm.registerEvents(new KillsTracker(this), this);

		new ASkyBlockEvents(this);
		new SSkyBlockEvents(this);
	}

	public void RegisterConfigs(){
        config = new YmlConfig(this);
		messages = new YmlCreator(this, "messages.yml");
		levels = new YmlCreator(this, "levels.yml");
	}
	public static final BeastSpawners getInstance(){
		return instance;
	}

	public YmlCreator getMsg(){
		return messages;
	}
	public YmlCreator getLdata(){
		return levels;
	}
	public Utils getUtils(){
		return ut;
	}

	public void checkMVd(){
		if (getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")){
			ph = new PlaceholderMVdW(this);
			ph.holder();
			PluginManager pm = Bukkit.getServer().getPluginManager();
			pm.registerEvents(new PlaceholderMVdW(this), this);
		}		
	}
	public void checkPapi(){
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			new PlaceHolderAPI(this).register();
		}
	}
	public void setSpawnerInHand(String pName, String sName){	
		spawnerId.put(pName, sName.toLowerCase());	
	}
	public boolean hasSpawnerInHand(String pName){	
		if(spawnerId.containsKey(pName)){
			return true;
		}
		return false;

	}
	public void removeSpawnerInHand(String pName){	
		spawnerId.remove(pName);	
	}
	public String getSpawnerInHand(String pName){	
		return spawnerId.get(pName);	
	}
	public FileConfiguration getConfig() {
		return config.getConfig();
	}
	
    public Version getServerVersion() {
        return version;
    }

    public boolean isServerVersion(Version v) {
        return version == v;
    }

    public boolean isServerVersion(Version... versions) {
        return ArrayUtils.contains(versions, version);
    }

    public boolean isServerVersionAtLeast(Version v) {
        return version.ordinal() >= v.ordinal();
    }
    public Hooks getHooks() {
    	return hooks;
    }

}
