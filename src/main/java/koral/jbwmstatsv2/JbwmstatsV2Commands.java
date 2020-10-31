package koral.jbwmstatsv2;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class JbwmstatsV2Commands implements CommandExecutor {
    JbwmStatsV2Database database;
   JbwmstatsV2 plugin;
    public JbwmstatsV2Commands(final JbwmstatsV2 plugin) {
        this.plugin = plugin;
        database = new JbwmStatsV2Database(plugin);
    }
//TODO Command improvement, check exceptions.
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
                break;
            case"advancedstatadd":
                if(args.length == 3){
                    p.sendMessage("test");
                    String s = args[1] + "x" + args[2];
                    database.customAdvancedStatisticCreate(s);
                }
                break;
        }
        return false;
    }
    //TODO reset function
    public void resetStats(OfflinePlayer p){

    }

}
