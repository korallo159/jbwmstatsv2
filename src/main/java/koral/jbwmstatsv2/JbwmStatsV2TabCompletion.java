package koral.jbwmstatsv2;

import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class JbwmStatsV2TabCompletion implements TabCompleter {
    JbwmStatsV2Database database;
    JbwmstatsV2 plugin;
    public JbwmStatsV2TabCompletion(final JbwmstatsV2 plugin) {
        this.plugin = plugin;
        database = new JbwmStatsV2Database(plugin);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equals("stats") && args.length == 1){
            List<String> commands = new ArrayList<>();
            commands.add("add");
            commands.add("remove");
            commands.add("advancedstatadd");
            return commands;
        }
        if (command.getName().equals("stats") && args[0].equals("add")) {
            return statistics();
        }
        if (command.getName().equals("stats") && args[0].equals("remove")){
            return database.getCurrentColumNames();
        }
        if(command.getName().equals("stats") && args[0].equals("advancedstatadd") && args.length <3){
            return advancedStatistics();
        }
        return null;
    }

    public List<String> statistics(){
        List<String> stats = new ArrayList<>();
        for (Statistic s : Statistic.values()) {
            stats.add(s.toString());
        }
        List<String> blackStatsList = new ArrayList<>();
        blackStatsList.add(Statistic.MINE_BLOCK.toString()); blackStatsList.add(Statistic.USE_ITEM.toString()); blackStatsList.add(Statistic.BREAK_ITEM.toString());
        blackStatsList.add(Statistic.CRAFT_ITEM.toString());blackStatsList.add(Statistic.KILL_ENTITY.toString()); blackStatsList.add(Statistic.PICKUP.toString());
        blackStatsList.add(Statistic.DROP.toString());
        stats.removeAll(blackStatsList);
        return stats;
    }

    public List<String> advancedStatistics(){
        List<String> advstats = new ArrayList<>();
        advstats.add(Statistic.MINE_BLOCK.toString()); advstats.add(Statistic.USE_ITEM.toString()); advstats.add(Statistic.BREAK_ITEM.toString());
        advstats.add(Statistic.CRAFT_ITEM.toString()); advstats.add(Statistic.KILL_ENTITY.toString()); advstats.add(Statistic.PICKUP.toString());
        advstats.add(Statistic.DROP.toString());
        return advstats;
    }

}
