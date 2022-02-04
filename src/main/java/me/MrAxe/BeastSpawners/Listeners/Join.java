package me.MrAxe.BeastSpawners.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;

import me.MrAxe.BeastSpawners.BeastSpawners;


public class Join implements Listener {

	private BeastSpawners pl;	
	
	public Join(BeastSpawners plugin){
		pl = plugin;
	}

	@EventHandler
	public void OnJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
	
		


		
	}

}
