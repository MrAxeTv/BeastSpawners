package me.MrAxe.BeastSpawners.Listeners;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import org.bukkit.block.CreatureSpawner;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.nbtinjector.NBTInjector;
import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.XpLevel;
import me.MrAxe.BeastSpawners.Commands.Gui.LevelGui;
import me.MrAxe.BeastSpawners.Utils.Utils;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import net.milkbowl.vault.economy.Economy;

import java.util.ArrayList;


public class ClickGUI implements Listener {

	private BeastSpawners pl;
	String stack;
	public static Economy econ = null;
	private LevelGui levelGui;
	

	public ClickGUI(BeastSpawners plugin){
		pl = plugin;
		setupEconomy();
		levelGui = new LevelGui(pl);
	}
	
	@EventHandler
	public void guiClick(InventoryClickEvent e) {
		
		
		int lv = pl.getUtils().getPlayerUtils().getLevel(e.getWhoClicked().getUniqueId());
        String title = pl.getConfig().getString("GUI.Title");
        title = ChatColor.translateAlternateColorCodes('&', title);
        title = title.replaceAll("%plv%", ""+lv);
        title = title.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp((Player)e.getWhoClicked())));
		if(!e.getInventory().getTitle().equalsIgnoreCase(title)) return;
		e.setCancelled(true);
        int slot = e.getRawSlot()+1;
        if(slot <= lv) return;
        if(lv != slot-1) return;
        Player p = (Player) e.getWhoClicked();
		int xp = XpLevel.getExp(p);
		if (pl.getLdata().getConfig().isSet("Levels."+slot)){
			if(hasToKill(p)){
				killsLevelup(p);
				p.closeInventory();
				return;
			}

			int cost = pl.getLdata().getConfig().getInt("Levels."+slot+".Exp");
			if (xp >= cost){
				if (pl.getLdata().getConfig().isSet("Levels."+slot+".Money")){
					if(!moneyCharge(p,slot)) {			
						return ;
					}
				}
				pl.getUtils().getPlayerUtils().setLevel(p.getUniqueId(), slot);
				XpLevel.removeExp(p, cost);
				
				p.openInventory(levelGui.openLevelGui(p));

				String s = pl.getMsg().getConfig().getString("LevelUp.NewLevel");
				s = s.replaceAll("%cost%", ""+Utils.formatInt(cost));
				s=s.replaceAll("%lv%", ""+slot);
				s = ChatColor.translateAlternateColorCodes('&', s);
				p.sendMessage(pl.getUtils().getPrefix() + s);

			}
			else{
				String s = pl.getMsg().getConfig().getString("LevelUp.XpNeeded");
				s = s.replaceAll("%cost%", ""+Utils.formatInt(cost));
				s=s.replaceAll("%lv%", ""+slot);
				s = ChatColor.translateAlternateColorCodes('&', s);
				p.sendMessage(pl.getUtils().getPrefix() + s);
			}
		}
		else{
			String s = pl.getMsg().getConfig().getString("LevelUp.LastLevel");
			s = ChatColor.translateAlternateColorCodes('&', s);
			p.sendMessage(pl.getUtils().getPrefix() + s);
		}
		return;
	}
	
	public boolean moneyCharge(Player p, int lv) {
		if(econ == null)return true;
		if(!pl.getConfig().getBoolean("Options.ChargeMoney")) return true;
		double cost = pl.getLdata().getConfig().getDouble("Levels."+lv+".Money");
		if(econ.has(p, pl.getLdata().getConfig().getDouble("Levels."+lv+".Money"))) {
			econ.withdrawPlayer(p, cost);
			return true;		
		}else {
			String s = pl.getMsg().getConfig().getString("LevelUp.MoneyNeeded");
			s = s.replaceAll("%cost%", ""+pl.getUtils().formatDouble(cost));
			s=s.replaceAll("%lv%", ""+lv);
			s = ChatColor.translateAlternateColorCodes('&', s);
			p.sendMessage(pl.getUtils().getPrefix() + s);
			return false;
			
		}
		
	}
	public boolean hasToKill(Player p){

		int pLv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
		int nextLv = pLv+1;
		if(!pl.getLdata().getConfig().isSet("Levels."+nextLv+".Kills")) return false;
		ArrayList<String> mobs = (ArrayList<String>) pl.getLdata().getConfig().getStringList("Levels."+nextLv+".Kills");
		for(String mob : pl.getLdata().getConfig().getStringList("Levels."+nextLv+".Kills")){
			String[] split = mob.split(";");
			int pkills = pl.getUtils().getPlayerUtils().getKills(p, EntityType.valueOf(split[0]));
			int kills = Integer.parseInt(split[1]);
			if(pkills < kills) {
				return true;
			}
		}
		return false;
	}

	public void killsLevelup(Player p){
		int pLv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
		int nextLv = pLv+1;
		if(!pl.getLdata().getConfig().isSet("Levels."+nextLv+".Kills")) return;
		ArrayList<String> mobs = (ArrayList<String>) pl.getLdata().getConfig().getStringList("Levels."+nextLv+".Kills");

		String s = pl.getMsg().getConfig().getString("LevelUp.Kills.NeedKills");
		s = ChatColor.translateAlternateColorCodes('&',s);
		p.sendMessage(pl.getUtils().getPrefix()+ s);
		mobs.forEach(mob -> {

			String[] split = mob.split(";");
			int pkills = pl.getUtils().getPlayerUtils().getKills(p,EntityType.valueOf(split[0]));
			int kills = Integer.parseInt(split[1]);
			if(pkills < kills) {
				String m = pl.getMsg().getConfig().getString("LevelUp.Kills.MobList");
				m = m.replaceAll("%mob%", Type.valueOf(split[0]).getDisplayName());
				m = m.replaceAll("%pkills%", "" + pkills);
				m = m.replaceAll("%kills%", "" + kills);
				m = ChatColor.translateAlternateColorCodes('&', m);

				p.sendMessage(m);
			}

		});
	}

	
	private void setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = pl.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		econ = rsp.getProvider();
	}





}




