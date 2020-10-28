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



    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equals("stats") && args.length == 1){
            List<String> commands = new ArrayList<>();
            commands.add("add");
            return commands;
        }

        if (command.getName().equals("stats") && args.length == 2) {
            List<String> stats = new ArrayList<>();
            for (Statistic s : Statistic.values()) {
                stats.add(s.toString());
            }
            return stats;
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
                    p.sendMessage("Table create");
                    Statistic s = Statistic.valueOf(String.valueOf((args[1])));
                      database.customStatisticCreate(s.toString());
            //         s.toString();
              //      p.sendMessage("test" + s);
                }
                break;
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
