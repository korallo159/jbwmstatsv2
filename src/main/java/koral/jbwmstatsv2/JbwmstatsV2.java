package koral.jbwmstatsv2;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class JbwmstatsV2 extends JavaPlugin implements Listener {
    private JbwmstatsV2Commands commandExecutor;
    private JbwmStatsV2TabCompletion tabCompleter;

    JbwmStatsV2Database database = new JbwmStatsV2Database(this);
    @Override
    public void onEnable() {
     commandExecutor = new JbwmstatsV2Commands(this);
        File file = new File(getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();;
        } else {
            saveDefaultConfig();
            reloadConfig();
        }
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("statspurge").setExecutor(commandExecutor);
        this.getCommand("stats").setExecutor(commandExecutor);
        this.getCommand("createtable").setExecutor(commandExecutor);
        this.getCommand("stats").setTabCompleter(new JbwmStatsV2TabCompletion(this));
        database.connectToDatabase();
        database.createTable();

    }

    @Override
    public void onDisable() {
        for(Player p: Bukkit.getOnlinePlayers())
        database.pushCustomStats(p);
    }

@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        CreatePlayerAsync(p);
    }

@EventHandler
    public void onPlayerQuit(PlayerQuitEvent e ) {
    Player p = e.getPlayer();
    pushCustomStatsAsync(p);
}

    public void pushCustomStatsAsync(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                database.pushCustomStats(p);
            }
        });
    }

    public void CreatePlayerAsync(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                database.createPlayerQuery(p);
            }
        });
    }







}
