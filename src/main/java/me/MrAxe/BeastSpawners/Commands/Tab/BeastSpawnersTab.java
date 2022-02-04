package me.MrAxe.BeastSpawners.Commands.Tab;

import me.MrAxe.BeastSpawners.BeastSpawners;
import me.MrAxe.BeastSpawners.WorldUtils.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeastSpawnersTab implements TabCompleter {
    private static final String[] COMMANDS = { "level", "get", "give", "help","reload" };
    private BeastSpawners pl;
    private final ArrayList<String> mobtypes;
    public BeastSpawnersTab(BeastSpawners pl){
        this.pl = pl;
        mobtypes = new ArrayList<>();
        for(String s : Type.getMobList().keySet()){
            mobtypes.add(s);
        }
    }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //create new array
        final List<String> completions = new ArrayList<>();

        if(args.length == 1) StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
        if(args[0].equalsIgnoreCase("get") && !args[0].equals(null)) {
            if(args.length == 2 ) StringUtil.copyPartialMatches(args[1], mobtypes, completions);
        }
        if(args[0].equalsIgnoreCase("give") ||  args[0].equalsIgnoreCase("level")  ) {
            if(args.length == 2 ) return null;
            if(args[0].equalsIgnoreCase("give")) {
                if (args.length == 3) StringUtil.copyPartialMatches(args[2], mobtypes, completions);
            }

        }
        //sort the list
        Collections.sort(completions);
        return completions;
    }

}
