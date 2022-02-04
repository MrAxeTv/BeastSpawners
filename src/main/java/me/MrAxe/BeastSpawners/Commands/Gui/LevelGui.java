package me.MrAxe.BeastSpawners.Commands.Gui;

import java.util.ArrayList;

import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.XpLevel;
import me.MrAxe.BeastSpawners.Utils.Utils;

public class LevelGui {
	BeastSpawners pl;
	public LevelGui(BeastSpawners pl) {
		this.pl = pl;
	}



	public Inventory openLevelGui(Player p) {

		int plv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
		String title = pl.getConfig().getString("GUI.Title");
		title = ChatColor.translateAlternateColorCodes('&', title);
		title = title.replaceAll("%plv%", ""+plv);
		title = title.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));

		int size = pl.getConfig().getInt("GUI.Size");

		Inventory inv = Bukkit.createInventory(null, size, title);

		for (int i = 0; i < size; i++) {
			if(plv >= i+1) {

				ItemStack item = new ItemStack(Material.valueOf(pl.getConfig().getString("GUI.UnlockedLevel.Item")), 1, (short) pl.getConfig().getInt("GUI.UnlockedLevel.Data"));
				ItemMeta meta = item.getItemMeta();
				String name = pl.getConfig().getString("GUI.UnlockedLevel.Name");
				name = ChatColor.translateAlternateColorCodes('&', name);
				name = name.replaceAll("%lv%", ""+(i+1));
				meta.setDisplayName(name);
				ArrayList<String> lore = new ArrayList();
				for(String s : pl.getConfig().getStringList("GUI.UnlockedLevel.Lore")) {
					s = ChatColor.translateAlternateColorCodes('&', s);
					s = s.replaceAll("%lv%", ""+(i+1));
					s = s.replaceAll("%plv%", ""+plv);
					s = s.replaceAll("%exp%", ""+Utils.formatInt(pl.getLdata().getConfig().getInt("Levels."+(i+1)+".Exp")));
					s = s.replaceAll("%money%", ""+Utils.formatDouble(pl.getLdata().getConfig().getInt("Levels."+(i+1)+".Money")));
					s = s.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));
					lore.add(s);		
				}

				for(String s: pl.getUtils().getSpawnerUtils().getList(i+1)) {
						s= pl.getConfig().getString("GUI.UnlockedLevel.LoreColor")+s;
						s = ChatColor.translateAlternateColorCodes('&', s);
						lore.add(s);
				}
				meta.setLore(lore);
				if(pl.getConfig().getBoolean("GUI.UnlockedLevel.Glow")) {
					meta.addEnchant(Enchantment.DURABILITY, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				item.setItemMeta(meta);
				inv.setItem(i,item);



			}else {
				ItemStack item = new ItemStack(Material.valueOf(pl.getConfig().getString("GUI.LockedLevel.Item")), 1, (short) pl.getConfig().getInt("GUI.LockedLevel.Data"));
				ItemMeta meta = item.getItemMeta();
				String name = pl.getConfig().getString("GUI.LockedLevel.Name");
				name = ChatColor.translateAlternateColorCodes('&', name);
				name = name.replaceAll("%lv%", ""+(i+1));
				meta.setDisplayName(name);
				ArrayList<String> lore = new ArrayList();
				ArrayList<String> mobToKill = null;
				for(String s : pl.getConfig().getStringList("GUI.LockedLevel.Lore")) {
					boolean kills = false;

					if(s.equalsIgnoreCase("%kills%")){
						mobToKill = killsLevelup(p,i+1);
						if (mobToKill.isEmpty()) continue;
						kills = true;
						s = pl.getConfig().getString("GUI.LockedLevel.KillsLore");

					}
					s = ChatColor.translateAlternateColorCodes('&', s);
					s = s.replaceAll("%lv%", ""+(i+1));
					s = s.replaceAll("%plv%", ""+plv);
					s = s.replaceAll("%exp%", ""+Utils.formatInt(pl.getLdata().getConfig().getInt("Levels."+(i+1)+".Exp")));
					s = s.replaceAll("%money%", ""+Utils.formatDouble(pl.getLdata().getConfig().getInt("Levels."+(i+1)+".Money")));
					s = s.replaceAll("%pxp%", ""+Utils.formatInt(XpLevel.getExp(p)));
					lore.add(s);
					if(kills){
						mobToKill.forEach(mob ->lore.add(mob));
					}



				}
				for(String s: pl.getUtils().getSpawnerUtils().getList(i+1)) {
						s= pl.getConfig().getString("GUI.LockedLevel.LoreColor")+s;
						s = ChatColor.translateAlternateColorCodes('&', s);
						lore.add(s);
				}
				meta.setLore(lore);
				if(pl.getConfig().getBoolean("GUI.LockedLevel.Glow")) {
					meta.addEnchant(Enchantment.DURABILITY, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				item.setItemMeta(meta);
				inv.setItem(i,item);
			}
			if(i+1 == pl.getLdata().getConfig().getConfigurationSection("Levels").getKeys(false).size()) {
				break;
			}
		}
		return inv;	
	}
	public ArrayList<String> killsLevelup(Player p, int level){

		int nextLv = level;
		if(!pl.getLdata().getConfig().isSet("Levels."+nextLv+".Kills")) return new ArrayList<String>();
		ArrayList<String> mobs = (ArrayList<String>) pl.getLdata().getConfig().getStringList("Levels."+nextLv+".Kills");

		ArrayList<String> list = new ArrayList<>();
		mobs.forEach(mob -> {

			String[] split = mob.split(";");
			int pkills = pl.getUtils().getPlayerUtils().getKills(p, EntityType.valueOf(split[0]));
			int kills = Integer.parseInt(split[1]);
			if(pkills < kills) {
				String m = pl.getMsg().getConfig().getString("LevelUp.Kills.MobList");
				m = m.replaceAll("%mob%", Type.valueOf(split[0]).getDisplayName());
				m = m.replaceAll("%pkills%", "" + pkills);
				m = m.replaceAll("%kills%", "" + kills);
				m = ChatColor.translateAlternateColorCodes('&', m);
              list.add(m);

			}

		});

		return list;
	}




}
