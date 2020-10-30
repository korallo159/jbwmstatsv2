package koral.jbwmstatsv2;

import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

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
        if(command.getName().equals("stats") && args[0].equals("advancedstatadd") && args.length ==2){
            return advancedStatistics();
        }
        if(command.getName().equals("stats") && args[0].equals("advancedstatadd") &&  args[1].equals("MINE_BLOCK") && args.length ==3 ){
            return statsMine_Block();
        }

        if(command.getName().equals("stats") && args[0].equals("advancedstatadd") &&  args[1].equals("USE_ITEM") || args[1].equals("BREAK_ITEM")  || args[1].equals("CRAFT_ITEM")
               ||args[1].equals("PICKUP") || args[1].equals("DROP") && args.length ==3 ){
            return statsITEMS();
        }
        if(command.getName().equals("stats") && args[0].equals("advancedstatadd") && args[1].equals("KILL_ENTITY") || args[1].equals("ENTITY_KILLED_BY") && args.length == 3){
            return statsENTITY();
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
        blackStatsList.add(Statistic.CRAFT_ITEM.toString());blackStatsList.add(Statistic.KILL_ENTITY.toString()); blackStatsList.add(Statistic.ENTITY_KILLED_BY.toString());
        blackStatsList.add(Statistic.PICKUP.toString());
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
//TODO tab completion of use item, break item craft item, kill entity, pickup, drop
    public List<String> statsMine_Block(){
        List<String> list = new ArrayList<>();
        for(Material material: Material.values()){
            if(material.isBlock()){
                list.add(material.toString());
            }
        }
       return list;
    }


    public List<String> statsITEMS(){
        List<String> list = new ArrayList<>();
        for(Material material: Material.values()){
            if(material.isItem()){
                list.add(material.toString());
            }
        }
        return list;
    }


    public List<String> statsENTITY(){
        List<String> list = new ArrayList<>();
        for(EntityType entity: EntityType.values()){
            if(entity.isSpawnable())
                list.add(entity.toString());
        }
        return list;
    }




}
