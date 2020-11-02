package koral.jbwmstatsv2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class JbwmstatsV2Gui implements Listener {
    JbwmstatsV2 plugin;
    JbwmStatsV2TabCompletion tabCompletion;
    JbwmStatsV2Database database;
    public JbwmstatsV2Gui(JbwmstatsV2 plugin) {
        this.plugin = plugin;
        tabCompletion = new JbwmStatsV2TabCompletion(plugin);
        database = new JbwmStatsV2Database(plugin);
    }

    public ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }


    public Inventory pageMain(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.RED + "Gui stats manager");
        inv.setItem(3, createItem(Material.BOOK, ChatColor.RED +"Stwórz statystykę", ChatColor.GRAY+ "Kliknij aby stworzyć statystykę"));
        inv.setItem(4, createItem(Material.BOOKSHELF, ChatColor.RED+ "Stwórz zaawansowaną statystykę", ChatColor.GRAY+ "Kliknij aby stworzyć zaawansowaną statystykę"));
        inv.setItem(5, createItem(Material.WRITABLE_BOOK, ChatColor.RED +"usuń kolumnę z bazy daanych", ChatColor.GRAY+ "Kliknij aby usunąć kolumnę z MYSQL"));
        inv.setItem(53, createItem(Material.BARRIER, "Powrót", "Kliknij aby powrócić"));
        return inv;
    }
    public Inventory pagestats1(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            inv.setItem(i, createItem(Material.WRITABLE_BOOK, tabCompletion.statistics().get(i)));
        }
        inv.setItem(52,createItem(Material.KELP, ChatColor.GREEN + "Strona 2"));
        inv.setItem(53, createItem(Material.BARRIER, "Powrót", "Kliknij aby powrócić"));
        return inv;
    }
    public Inventory pagestats2(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            int b = i + 52;
            if(b==74) break;
            inv.setItem(i, createItem(Material.WRITABLE_BOOK, tabCompletion.statistics().get(b)));
        }
        inv.setItem(53, createItem(Material.BARRIER, "Powrót", "Kliknij aby powrócić"));
        return inv;
    }

    public Inventory pageRemove(Player player){
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.RED + "Gui stats manager");
        for(int i=0; i<52; i++){
            if(database.getCurrentColumNames().size() > i) {
                inv.setItem(i, createItem(Material.BOOK, database.getCurrentColumNames().get(i)));
            }
            else break;
        }
        inv.setItem(53, createItem(Material.BARRIER, "Powrót", "Kliknij aby powrócić"));
        return inv;
    }

    public Inventory pageAdvancedStats(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, ChatColor.RED + "Gui stats manager");
        for(int i =0; i<52; i++){
            if(tabCompletion.advancedStatistics().size() > i) {
                inv.setItem(i, createItem(Material.WRITABLE_BOOK, tabCompletion.advancedStatistics().get(i)));
            }
            else break;
        }
        inv.setItem(53, createItem(Material.BARRIER, "Powrót", "Kliknij aby powrócić"));
        return inv;
    }



    @EventHandler
    public void clickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!e.getView().getTitle().equals(ChatColor.RED + "Gui stats manager"))
            return;
        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        switch (e.getCurrentItem().getType()) {
            case BOOK:
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED +"Stwórz statystykę"))
                p.openInventory(pagestats1(p));
                if(database.getCurrentColumNames().contains(e.getCurrentItem().getItemMeta().getDisplayName())){
                    database.statisticRemove(e.getCurrentItem().getItemMeta().getDisplayName());
                    p.sendMessage(ChatColor.RED + "Usunięto kolumnę: " + e.getCurrentItem().getItemMeta().getDisplayName());
                    p.openInventory(pageRemove(p));
                }
                break;
            case BOOKSHELF:
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED+ "Stwórz zaawansowaną statystykę"))
                    p.openInventory(pageAdvancedStats(p));


                break;


            case WRITABLE_BOOK:
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED +"usuń kolumnę z bazy daanych")){
                    p.openInventory(pageRemove(p));
                }
                if(tabCompletion.statistics().contains(e.getCurrentItem().getItemMeta().getDisplayName())) {
                  database.customStatisticCreate(e.getCurrentItem().getItemMeta().getDisplayName());
                  p.sendMessage(ChatColor.GREEN + "Utworzono kolumnę " + e.getCurrentItem().getItemMeta().getDisplayName());
                }
                break;
            case KELP:
                p.openInventory(pagestats2(p));
                break;
            case BARRIER:
                p.openInventory(pageMain(p));
                break;
            }
e.setCancelled(true);
        }


}
