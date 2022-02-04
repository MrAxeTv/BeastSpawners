package me.MrAxe.BeastSpawners.Listeners.Hooks;

import me.MrAxe.BeastSpawners.FileManager.PlayerYml;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.wasteofplastic.askyblock.events.IslandChangeOwnerEvent;
import com.wasteofplastic.askyblock.events.IslandResetEvent;

import me.MrAxe.BeastSpawners.BeastSpawners;

public class ASkyBlockEvents implements Listener {
	
	BeastSpawners pl;
	public ASkyBlockEvents(BeastSpawners pl) {
		this.pl = pl;
		if(pl.getHooks().isASkyBlockOn()) {
		pl.getServer().getPluginManager().registerEvents(this, pl);
		}
		
		
	}
	
	@EventHandler
	public void islandRestart(IslandResetEvent e) {
			
		pl.getUtils().getPlayerUtils().setLevel(e.getPlayer().getUniqueId(), 1);
	}
	@EventHandler
	public void islandOwnerChange(IslandChangeOwnerEvent e) {

		PlayerYml pData = new PlayerYml(e.getNewOwner(),pl);
		pData.getFile().set("Level", pl.getUtils().getPlayerUtils().getLevel(e.getOldOwner()));
		pData.saveFile();
		pData = new PlayerYml(e.getOldOwner(),pl);
		pData.getFile().set("Level", 1);
		pData.saveFile();

	}

}
