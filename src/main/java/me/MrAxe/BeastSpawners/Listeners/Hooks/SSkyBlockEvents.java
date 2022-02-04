package me.MrAxe.BeastSpawners.Listeners.Hooks;

import me.MrAxe.BeastSpawners.FileManager.PlayerYml;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandTransferEvent;

import me.MrAxe.BeastSpawners.BeastSpawners;

public class SSkyBlockEvents implements Listener {
	
	BeastSpawners pl;
	public SSkyBlockEvents(BeastSpawners pl) {
		this.pl = pl;
		if(pl.getHooks().isSSkyBlockOn()) {
		pl.getServer().getPluginManager().registerEvents(this, pl);
		}
		
		
	}
	
	@EventHandler
	public void islandRestart(IslandDisbandEvent e) {
			
		pl.getUtils().getPlayerUtils().setLevel(e.getPlayer().getUniqueId(), 1);
	}
	@EventHandler
	public void islandOwnerChange(IslandTransferEvent e) {


		PlayerYml pData = new PlayerYml(e.getNewOwner().getUniqueId(),pl);
		pData.getFile().set("Level", pl.getUtils().getPlayerUtils().getLevel(e.getOldOwner().getUniqueId()));
		pData.saveFile();
		pData = new PlayerYml(e.getOldOwner().getUniqueId(),pl);
		pData.getFile().set("Level", 1);
		pData.saveFile();

	}

}
