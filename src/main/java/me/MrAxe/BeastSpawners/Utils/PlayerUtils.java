package me.MrAxe.BeastSpawners.Utils;

import java.util.UUID;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.FileManager.PlayerYml;
import me.MrAxe.BeastSpawners.Utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.IslandRole;
import com.bgsoftware.superiorskyblock.api.island.PlayerRole;
import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {
	BeastSpawners pl;
	public PlayerUtils(BeastSpawners pl) {
		this.pl = pl;
		// TODO Auto-generated constructor stub
	}


	public int getLevel (UUID uuid) {

		if(pl.getHooks().isASkyBlockOn()) {
			if(ASkyBlockAPI.getInstance().hasIsland(uuid) || ASkyBlockAPI.getInstance().calculateIslandLevel(uuid)) {
				UUID owner =ASkyBlockAPI.getInstance().getIslandAt(ASkyBlockAPI.getInstance().getIslandLocation(uuid)).getOwner();
				return new PlayerYml(owner,pl).getFile().getInt("Level");
			}
		}

		if(pl.getHooks().isSSkyBlockOn()) {
			//if(SuperiorSkyblockAPI.getPlayer(uuid).isInsideIsland()) {
			if(SuperiorSkyblockAPI.getPlayer(uuid).getIsland() != null) {
				UUID owner = SuperiorSkyblockAPI.getPlayer(uuid).getIsland().getOwner().getUniqueId();
				return new PlayerYml(owner,pl).getFile().getInt("Level");
			}
		}

		return new PlayerYml(uuid,pl).getFile().getInt("Level");
	}

	public void setLevel(UUID uuid,int lv) {

		if(pl.getHooks().isASkyBlockOn()) {
			if(ASkyBlockAPI.getInstance().hasIsland(uuid) || ASkyBlockAPI.getInstance().calculateIslandLevel(uuid)) {	
				UUID owner =ASkyBlockAPI.getInstance().getIslandAt(ASkyBlockAPI.getInstance().getIslandLocation(uuid)).getOwner();
				PlayerYml pData = new PlayerYml(owner,pl);
				pData.getFile().set("Level", lv);
				pData.saveFile();
				return;
			}
		}
		if(pl.getHooks().isSSkyBlockOn()) {
			//if(SuperiorSkyblockAPI.getPlayer(uuid).isInsideIsland()) {
			if(SuperiorSkyblockAPI.getPlayer(uuid).getIsland() != null) {
				UUID owner = SuperiorSkyblockAPI.getPlayer(uuid).getIsland().getOwner().getUniqueId();
				PlayerYml pData = new PlayerYml(owner,pl);
				pData.getFile().set("Level", lv);
				pData.saveFile();
				return;

			}
		}
		PlayerYml pData = new PlayerYml(uuid,pl);
		pData.getFile().set("Level", lv);
		pData.saveFile();
		return;

	}

	public boolean hasIsland(UUID uuid) {

		if(pl.getHooks().isASkyBlockOn()) {
			if(ASkyBlockAPI.getInstance().hasIsland(uuid) || ASkyBlockAPI.getInstance().calculateIslandLevel(uuid)) {	
				return true;
			}
		}
		if(pl.getHooks().isSSkyBlockOn()) {
			
				if(SuperiorSkyblockAPI.getPlayer(uuid).getIsland() != null) {
				return true;
			}
		}
		return false;

	}
	public ItemStack getItemInMainHand(Player player) {
		if (pl.isServerVersionAtLeast(Version.V1_9)) {
			return player.getInventory().getItemInMainHand();
		}
		return player.getItemInHand();
	}

	public void setItemInMainHand(Player player, ItemStack item) {
		if (pl.isServerVersionAtLeast(Version.V1_9)) {
			player.getInventory().setItemInMainHand(item);
		} else {
			player.setItemInHand(item);
		}
	}

	public ItemStack getItemInOffHand(Player player) {
		if (pl.isServerVersionAtLeast(Version.V1_9)) {
			return player.getInventory().getItemInOffHand();
		}
		return player.getItemInHand();
	}

	public void setItemInOffHand(Player player, ItemStack item) {
		if (pl.isServerVersionAtLeast(Version.V1_9)) {
			player.getInventory().setItemInOffHand(item);
		} else {
			player.setItemInHand(item);
		}
	}
	public int getKills(Player p , EntityType t){
		return new PlayerYml(p.getUniqueId(),pl).getFile().getInt("Kills."+t);
	}


}
