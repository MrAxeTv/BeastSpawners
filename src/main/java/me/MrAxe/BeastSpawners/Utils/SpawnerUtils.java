package me.MrAxe.BeastSpawners.Utils;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnerUtils {
    private BeastSpawners pl;
    private HashMap<String, Integer> spawnerLv;

    public SpawnerUtils(BeastSpawners pl){
        this.pl = pl;
        spawnerLv = new HashMap<String,Integer>();
        setSpawnerAllLv();
    }


    public String getSpawnerType(Block b){

        CreatureSpawner cs = (CreatureSpawner) b.getState();
        return cs.getSpawnedType().name();


    }

    public int getSpawnerLv(String name){
        return spawnerLv.get(name);
    }
    public boolean isSpawnerLocked(String name) {
        if(spawnerLv.containsKey(name)) {
            return true;
        }
        return false;
    }
    public void setSpawnerAllLv() {
        int maxLv = pl.getLdata().getConfig().getConfigurationSection("Levels").getKeys(false).size();
        for (int i = 0; i <= maxLv; i++) {
            List<String> list = pl.getLdata().getConfig().getStringList("Levels."+i+".Mobs");
            for(String s: list) {
                //Bukkit.broadcastMessage(s);
                spawnerLv.put(s, i);

            }
        }
    }
    public List<String> getList(int lv){

        List<String> s = new ArrayList<String>();

        for(String a : pl.getLdata().getConfig().getStringList("Levels."+lv+".Mobs")) {
            if(!checkEnum(a)) {
                continue;
            }
            Type t = Type.valueOf(a);
            if(!s.contains(t.getDisplayName())){
                s.add(t.getDisplayName());
            }
        }
        return s;
    }


    public boolean checkEnum(String s) {

        try {
            Type.valueOf(s).getName();
            return true;
        } catch (IllegalArgumentException ex) {
            pl.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Wrong entity name"+ s);
        }


        return false;
    }

}
