package me.MrAxe.BeastSpawners.Listeners;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.FileManager.PlayerYml;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;


public class KillsTracker implements Listener {
	private BeastSpawners  pl;


	public KillsTracker(BeastSpawners plugin){
		pl = plugin;

	}



	@EventHandler
	public  void kill(EntityDeathEvent e){
		if (!(e.getEntity().getKiller() instanceof Player)) return;
		Player p = e.getEntity().getKiller();

		PlayerYml pData = new PlayerYml(p.getUniqueId(),pl);
		int kills = pData.getFile().getInt("Kills."+e.getEntity().getType());
		pData.getFile().set("Kills."+e.getEntity().getType(),kills+1);
		pData.saveFile();
	}

}
