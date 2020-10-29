package koral.jbwmstatsv2;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class JbwmstatsV2 extends JavaPlugin implements Listener {
    private JbwmstatsV2Commands commandExecutor;

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
        this.getCommand("stats").setTabCompleter(new JbwmstatsV2Commands(this));
        database.connectToDatabase();
        database.createTable();

    }

    @Override
    public void onDisable() {
    }



@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        CreatePlayerAsync(p);
        p.sendMessage("test");
    }

@EventHandler
    public void onPlayerQuit(PlayerQuitEvent e ){
        Player p = e.getPlayer();
        database.pushCustomStats(p.getUniqueId(), p);
}

 /*   public void PushStatsAsync(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                pushStats(p);
            }
        });
    }

  */

    public void CreatePlayerAsync(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                database.createPlayerQuery(p);
            }
        });
    }


 /*   public void pushStats(Player p ){
        int ticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        long seconds = ticks/20;
        database.updateTime(p.getUniqueId(), seconds);
        int snowballsThrowed = p.getStatistic(Statistic.USE_ITEM, Material.SNOWBALL);
        database.updateThrowedSnowballs(p.getUniqueId(), snowballsThrowed);
        int standardzombieKilled = p.getStatistic(Statistic.KILL_ENTITY, EntityType.ZOMBIE);
        int huskKilled = p.getStatistic(Statistic.KILL_ENTITY, EntityType.HUSK);
        int ironGolem = p.getStatistic(Statistic.KILL_ENTITY, EntityType.IRON_GOLEM);
        int totalZombieKilled = standardzombieKilled + huskKilled + ironGolem;
        database.updateZombieQuery(p.getUniqueId(), totalZombieKilled);
        int kills = p.getStatistic(Statistic.PLAYER_KILLS);
        database.updateKillsQuery(p.getUniqueId(), kills);
    } */

 /*   @EventHandler
    public void snowball(ProjectileLaunchEvent e){
        if(e.getEntity() instanceof Snowball){
            Snowball snow = (Snowball) e.getEntity();
            if(snow.getShooter() instanceof Player) {
                Player p = (Player) snow.getShooter();
                p.sendMessage("test");
               if(!snowballs.containsKey(p.getUniqueId().toString())){
                   snowballs.put(p.getUniqueId().toString(), 1);
               }else {
                   snowballs.put(p.getUniqueId().toString(),snowballs.get(p.getUniqueId().toString()) + 1);
                   p.sendMessage("Rzucone Sniezki" + snowballs.get(p.getUniqueId().toString()));
               }
            }
        }
    } */








}
