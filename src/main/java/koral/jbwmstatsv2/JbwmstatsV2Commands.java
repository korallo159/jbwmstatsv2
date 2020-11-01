package koral.jbwmstatsv2;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class JbwmstatsV2Commands implements CommandExecutor {
    JbwmStatsV2Database database;
   JbwmstatsV2 plugin;
   JbwmstatsV2listener listener;
   JbwmstatsV2Gui gui;
    public JbwmstatsV2Commands(final JbwmstatsV2 plugin) {
        this.plugin = plugin;
        database = new JbwmStatsV2Database(plugin);
        listener = new JbwmstatsV2listener(plugin);
        gui = new JbwmstatsV2Gui(plugin);

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
        if(command.getName().equals("stats") && args.length == 0){
            p.openInventory(gui.pageMain(p));
            p.sendMessage("GUITest");
            return true;
        }

        if(command.getName().equals("stats") && args.length > 0)
        switch(args[0]){
            case "add":
                if(args.length == 2){
                    Statistic s = Statistic.valueOf(String.valueOf((args[1])));
                        CustomStatAsyncCreate(s.toString());
                        p.sendMessage(ChatColor.GREEN + "Utworzono kolumne " + s.toString());
                }
                else
                    sender.sendMessage(ChatColor.RED + "poprawne użycie: /stats add <Statystyka>");
                break;
            case"remove":
                if(args.length == 2) {
                    if(args[1].equals("UUID") || args[1].equals("NICK")) {
                        sender.sendMessage(ChatColor.RED + "Nie możesz usunąć głównych kolumn, uszkodziłoby to tabelę.");
                        break;
                    }
                    ColumnRemoveAsync(args[1]);
                    p.sendMessage(ChatColor.RED + "Usunięto kolumnę: " + args[1]);
                }
                else
                    sender.sendMessage(ChatColor.RED + "poprawne użycie: /stats remove <nazwa_kolumny>");
                break;
            case"advancedstatadd":
                if(args.length == 3){
                    String s = args[1] + "x" + args[2];
                    if(args[1].equals("BREAK_ITEM") || args[1].equals("CRAFT_ITEM") || args[1].equals("ENTITY_KILLED_BY") || args[1].equals("PICKUP")
                            || args[1].equals("DROP") || args[1].equals("KILL_ENTITY") || args[1].equals("MINE_BLOCK") || args[1].equals("USE_ITEM")) {
                        CustomAdvancedStatisticAsyncCreate(s);
                        sender.sendMessage(ChatColor.GREEN + "Utworzono kolumnę" + s);
                    }
                    else sender.sendMessage("Niepoprawny pierwszy argument!");
                }
                else
                    sender.sendMessage(ChatColor.RED + "poprawne użycie: /stats advancedstatadd <NAZWA_STATYSTYKI> <TYP>");
                break;
        }
        return false;
    }
    //TODO reset function
    public void resetStats(OfflinePlayer p){

    }


    public void CustomStatAsyncCreate(String s) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                database.customStatisticCreate(s);
            }
        });
    }

    public void CustomAdvancedStatisticAsyncCreate(String s) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                database.customAdvancedStatisticCreate(s);
            }
        });
    }

    public void ColumnRemoveAsync(String s) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                database.statisticRemove(s);
            }
        });
    }
}

