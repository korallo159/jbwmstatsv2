package koral.jbwmstatsv2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JbwmstatsV2listener implements Listener {
    JbwmstatsV2 plugin;
    JbwmStatsV2Database database;

    public JbwmstatsV2listener(final JbwmstatsV2 plugin) {
        this.plugin = plugin;
        database = new JbwmStatsV2Database(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        CreatePlayerAsync(p);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        pushCustomStatsAsync(p);
    }


    public void pushCustomStatsAsync(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                database.pushCustomStats(p);
            }
        });
    }

    public void CreatePlayerAsync(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                database.createPlayerQuery(p);
            }
        });
    }




}
