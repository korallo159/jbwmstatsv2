package koral.jbwmstatsv2;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class JbwmstatsV2Commands implements CommandExecutor, TabCompleter {
    JbwmStatsV2Database database;
   JbwmstatsV2 plugin;
    public JbwmstatsV2Commands(final JbwmstatsV2 plugin) {
        this.plugin = plugin;
        database = new JbwmStatsV2Database(plugin);
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
//TODO tab complete of current Tables.
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equals("stats") && args.length == 1){
            List<String> commands = new ArrayList<>();
            commands.add("add");
            commands.add("remove");
            return commands;
        }
        if (command.getName().equals("stats") && args[0].equals("add")) {
            return statistics();
        }
        if (command.getName().equals("stats") && args[0].equals("remove")){
           return database.getCurrentColumNames();
        }
        return null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if(command.getName().equals("statspurge") &&args.length > 0){
            OfflinePlayer target2 = Bukkit.getOfflinePlayer(args[0]);
            if(!target2.hasPlayedBefore()){
                p.sendMessage(ChatColor.RED + "Taki gracz nigdy tutaj nie gral!");
                return true;
            }
            else {
                resetStats(target2);
                p.sendMessage(ChatColor.GREEN + "Wyczyszczono statystyki gracza: " + ChatColor.YELLOW + target2.getName());
                return true;
            }
        }
//TODO check type of variabile
        if(command.getName().equals("stats"))
        switch(args[0]){
            case "add":
                if(args.length == 2){
                    Statistic s = Statistic.valueOf(String.valueOf((args[1])));
                        database.customStatisticCreate(s.toString());
                        p.sendMessage(ChatColor.GREEN + "Utworzono kolumne " + s.toString());
                }
                break;
            case"remove":
                if(args.length == 2) {
                    Statistic s = Statistic.valueOf(String.valueOf((args[1])));
                    database.statisticRemove(s.toString());
                    p.sendMessage(ChatColor.RED + "Usunięto kolumne " + s);
                }
        }
        return false;
    }
    public void resetStats(OfflinePlayer p){
        p.setStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE, 0);
        p.setStatistic(Statistic.KILL_ENTITY, EntityType.HUSK, 0);
        p.setStatistic(Statistic.KILL_ENTITY, EntityType.IRON_GOLEM, 0);
        p.setStatistic(Statistic.PLAYER_KILLS, 0);
        p.setStatistic(Statistic.PLAY_ONE_MINUTE, 0);
        p.setStatistic(Statistic.USE_ITEM, Material.SNOWBALL, 0);
    }

}
