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
    private JbwmstatsV2listener listener;

    JbwmStatsV2Database database = new JbwmStatsV2Database(this);
    @Override
    public void onEnable() {
     commandExecutor = new JbwmstatsV2Commands(this);
        new File(getDataFolder() + File.separator + "config.yml");
        saveDefaultConfig();;

        this.listener = new JbwmstatsV2listener(this);
        this.getServer().getPluginManager().registerEvents(listener, this);
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








}
