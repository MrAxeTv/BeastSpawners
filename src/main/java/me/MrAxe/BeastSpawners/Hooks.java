package me.MrAxe.BeastSpawners;

public class Hooks {
	
	BeastSpawners pl;
	
	boolean aSkyBlock = false;
	boolean sSkyBlock = false;
	public Hooks(BeastSpawners pl) {
		this.pl = pl;
		loadHooks();
		
	}
	
  private void loadHooks() {
	  
	  if (pl.getServer().getPluginManager().isPluginEnabled("ASkyBlock") && pl.getConfig().getBoolean("Options.Hooks.ASkyBlockTeamLevel")){
		  aSkyBlock = true;
	  }
	  if (pl.getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2") && pl.getConfig().getBoolean("Options.Hooks.SSkyBlockTeamLevel")){
		  sSkyBlock = true;
	  }
	  
	  
	  
	  
  }
  
	public boolean isASkyBlockOn() {
		
		return aSkyBlock;
	}
	
	public boolean isSSkyBlockOn() {
		
		return sSkyBlock;
	}
	
	

}
