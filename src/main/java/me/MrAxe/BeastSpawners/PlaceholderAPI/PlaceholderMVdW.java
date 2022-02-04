package me.MrAxe.BeastSpawners.PlaceholderAPI;


import org.bukkit.entity.Player;

import org.bukkit.event.Listener;




import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import me.MrAxe.BeastSpawners.BeastSpawners;



public class PlaceholderMVdW
implements Listener {
	BeastSpawners pl;

	public PlaceholderMVdW(BeastSpawners plugin) {
		this.pl = plugin;
	}

	public String holder(){
		
		
				if (pl.getServer().getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")){
					 				 					  
						PlaceholderAPI.registerPlaceholder(pl, "BeastSpawners_level",new PlaceholderReplacer() {
							
							public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
								Player p = e.getPlayer();
								return "" + pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
								
							}
						});
				}

		return null;
					


	}
}

