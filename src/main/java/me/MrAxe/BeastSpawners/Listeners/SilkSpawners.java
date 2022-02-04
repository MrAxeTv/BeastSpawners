package me.MrAxe.BeastSpawners.Listeners;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.Utils.ItemManager;
import me.MrAxe.BeastSpawners.Utils.Version;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class SilkSpawners implements Listener {
    private BeastSpawners pl;
    private ItemManager itemManager;
    private boolean enabled;
    private boolean silktouch;
    private boolean toFloor;
    public SilkSpawners(BeastSpawners pl){
        this.pl = pl;
        itemManager = new ItemManager(pl);
        enabled = pl.getConfig().getBoolean("Options.SpawnerDrops.Enabled");
        silktouch = pl.getConfig().getBoolean("Options.SpawnerDrops.SilkTouch");
        toFloor = pl.getConfig().getBoolean("Options.SpawnerDrops.ToFloor");


    }

    @EventHandler
    public void silkingBlocks(BlockBreakEvent e){

        if(!e.getBlock().getType().equals(pl.isServerVersionAtLeast(Version.V1_13) ? Material.SPAWNER : Material.valueOf("MOB_SPAWNER"))) return;
            if(e.isCancelled()) return;
            if(!enabled) return;

          Player p = e.getPlayer();
          String type = pl.getUtils().getSpawnerUtils().getSpawnerType(e.getBlock());
         if(pl.getUtils().getPlayerUtils().getItemInMainHand(p).equals(null)){
             if(silktouch) return;

         }
         if(silktouch){
             ItemStack tool = pl.getUtils().getPlayerUtils().getItemInMainHand(p);
                      if(!tool.getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
                          return;
                      }
         }
         if(!p.hasPermission("BeastSpawners.mine."+Type.valueOf(type).name())){

             e.setCancelled(true);
             String s = pl.getMsg().getConfig().getString("Events.Mining.NoPermission");
             s = s.replaceAll("%mob%", Type.valueOf(type).getDisplayName());
             s = ChatColor.translateAlternateColorCodes('&',s);
             p.sendMessage(pl.getUtils().getPrefix()+ s);
             return;

         }

        int lv = pl.getUtils().getPlayerUtils().getLevel(p.getUniqueId());
        Type mob = Type.valueOf(type);

        //Skip level check if mob is not specified in config file
        if(pl.getUtils().getSpawnerUtils().isSpawnerLocked(mob.name())) {
            if (pl.getUtils().getSpawnerUtils().getSpawnerLv(mob.name()) > lv) {
                e.setCancelled(true);
                p.updateInventory();

                //To small player level message
                String s;
                s = pl.getMsg().getConfig().getString("Events.Mining.LowLevel");
                s = s.replaceAll("%mob%", Type.valueOf(type).getDisplayName());
                s = ChatColor.translateAlternateColorCodes('&', s);
                p.sendMessage(pl.getUtils().getPrefix() + s);
                return;
            }
        }

        ItemStack item = itemManager.getSpawnerItem(Type.valueOf(type).getEntityType(),1,true);

          p.getInventory().addItem(item);
          String s = pl.getMsg().getConfig().getString("Events.Mining.MinedSpawner");
          s = s.replaceAll("%mob%", Type.valueOf(type).getDisplayName());
          s = ChatColor.translateAlternateColorCodes('&',s);
          p.sendMessage(pl.getUtils().getPrefix()+s);








    }



}
