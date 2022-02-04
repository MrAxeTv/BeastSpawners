package me.MrAxe.BeastSpawners.Utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemManager {
    private BeastSpawners pl;


    public ItemManager(BeastSpawners plugin) {
        pl = plugin;
    }



    public ItemStack getSpawnerItem(String mobType,  int amount, boolean signed) {

        ItemStack item = new ItemStack(pl.isServerVersionAtLeast(Version.V1_13) ? Material.SPAWNER : Material.valueOf("MOB_SPAWNER") , amount);
        if (signed) {

            ItemMeta meta = item.getItemMeta();
            String n =  pl.getConfig().getString("SpawnerItem.Name");
            String type = Type.fromName(mobType.toLowerCase()).toString();
            n = n.replaceAll("%mob%", ""+Type.valueOf(type).getDisplayName());
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', n));
            ArrayList<String> lore = new ArrayList<String>();
            for (String s : pl.getConfig().getStringList("SpawnerItem.Lore")) {
                type = Type.fromName(mobType).toString();
                s = s.replaceAll("%mob%", ""+Type.valueOf(type).getDisplayName());
                s = ChatColor.translateAlternateColorCodes('&', s);
                lore.add(s);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            NBTItem tag = new NBTItem(item);
            tag.addCompound("BlockEntityTag");
            tag.getCompound("BlockEntityTag").setString("EntityId", Type.valueOf(type).getEntityType());
            tag.addCompound("SilkSpawners");
            tag.getCompound("SilkSpawners").setShort("entityID", EntityType.fromName(mobType.toLowerCase()).getTypeId());
            tag.getCompound("BlockEntityTag").addCompound("SpawnData");
            tag.getCompound("BlockEntityTag").getCompound("SpawnData").setString("id", Type.valueOf(type).getEntityType());

            item = tag.getItem();
            return item;
        }
       return null;
    }


}
