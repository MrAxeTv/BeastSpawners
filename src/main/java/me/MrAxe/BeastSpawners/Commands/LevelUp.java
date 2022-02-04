package me.MrAxe.BeastSpawners.Commands;

import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.XpLevel;
import me.MrAxe.BeastSpawners.Commands.Gui.LevelGui;
import me.MrAxe.BeastSpawners.Utils.Utils;
import net.milkbowl.vault.economy.Economy;

import java.util.ArrayList;

public class LevelUp implements CommandExecutor {

	private BeastSpawners pl;
	public static Economy econ = null;
	private LevelGui levelGui;

	public LevelUp(BeastSpawners plugin) {
		pl = plugin;
		setupEconomy();
		levelGui = new LevelGui(pl);
	}
	private void setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = pl.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		econ = rsp.getProvider();
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) {
			sender.sendMessage(pl.getUtils().getPrefix()+ "Console can't use this command!");
			return false;
		}

		Player p = (Player) sender;


		if(pl.getHooks().isASkyBlockOn() || pl.getHooks().isSSkyBlockOn()) {
			if(!pl.getUtils().getPlayerUtils().hasIsland(p.getUniqueId())) {
				String s = pl.getMsg().getConfig().getString("LevelUp.Hooks.SkyBlockNoIsland");
				s = ChatColor.translateAlternateColorCodes('&', s);
				s = s.replaceAll("%player%", p.getName());
				p.sendMessage(pl.getUtils().getPrefix()+s);
				return false;
			}

		}

		if(pl.getConfig().getBoolean("GUI.Enabled")) {
			p.openInventory(levelGui.openLevelGui(p));
			return false;
		}
		int xp = XpLevel.getExp(p);
		int lv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
		lv = lv + 1;

		if (pl.getLdata().getConfig().isSet("Levels."+lv)){
			int cost = pl.getLdata().getConfig().getInt("Levels."+lv+".Exp");
			if(hasToKill(p)){
				killsLevelup(p);
				return false;
			}

			if (xp >= cost){
				if (pl.getLdata().getConfig().isSet("Levels."+lv+".Money")){
					if(!moneyCharge(p,lv)) {			
						return false;
					}
				}
				pl.getUtils().getPlayerUtils().setLevel(p.getUniqueId(), lv);
				XpLevel.removeExp(p, cost);

				String s = pl.getMsg().getConfig().getString("LevelUp.NewLevel");
				s = s.replaceAll("%cost%", ""+Utils.formatInt(cost));
				s=s.replaceAll("%lv%", ""+lv);
				s = s.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));
				s = ChatColor.translateAlternateColorCodes('&', s);
				p.sendMessage(pl.getUtils().getPrefix() + s);

			}
			else{
				String s = pl.getMsg().getConfig().getString("LevelUp.XpNeeded");
				s = s.replaceAll("%cost%", ""+Utils.formatInt(cost));
				s=s.replaceAll("%lv%", ""+lv);
				s = s.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));
				s = ChatColor.translateAlternateColorCodes('&', s);
				p.sendMessage(pl.getUtils().getPrefix() + s);
			}
		}
		else{
			String s = pl.getMsg().getConfig().getString("LevelUp.LastLevel");
			s = ChatColor.translateAlternateColorCodes('&', s);
			p.sendMessage(pl.getUtils().getPrefix() + s);
		}
		return false;
	}
	public boolean moneyCharge(Player p, int lv) {
		if(econ == null)return true;
		if(!pl.getConfig().getBoolean("Options.ChargeMoney")) return true;
		double cost = pl.getLdata().getConfig().getDouble("Levels."+lv+".Money");
		if(econ.has(p, cost)) {
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
			int pkills = pl.getUtils().getPlayerUtils().getKills(p,EntityType.valueOf(split[0]));
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
}
