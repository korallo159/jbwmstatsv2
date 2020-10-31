package koral.jbwmstatsv2;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JbwmStatsV2Database {

    JbwmstatsV2 plugin;

    public JbwmStatsV2Database(JbwmstatsV2 plugin) {
        this.plugin = plugin;
    }
//TODO database create advanced statistic
    public static HikariDataSource hikari;

    public void connectToDatabase() {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", plugin.getConfig().getString("host"));
        hikari.addDataSourceProperty("port", plugin.getConfig().getInt("port"));
        hikari.addDataSourceProperty("databaseName", plugin.getConfig().getString("database"));
        hikari.addDataSourceProperty("user", plugin.getConfig().getString("username"));
        hikari.addDataSourceProperty("password", plugin.getConfig().getString("password"));
    }
    public void createPlayerQuery(Player player) {
        Connection connection = null;

        String update = "INSERT INTO Stats (UUID, NICK) VALUES (?,?) ON DUPLICATE KEY UPDATE UUID=?";


        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();//SPRAWDZA ILE KOLUMN JEST
                statement = connection.prepareStatement(update);
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, player.getName());
            statement.setString(3, player.getUniqueId().toString());
                statement.execute();

   /*             for(int i = 3; i < columnsNumber + 1; i++) {
                    String update2 = "INSERT INTO Stats VALUES(?) ON DUPLICATE KEY UPDATE UUID=?";
                    statement = connection.prepareStatement(update2);
                    statement.setDouble(1, 0);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.execute();

    */


        } catch (SQLException e) {
                  e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void createTable() {
        Connection connection = null;
        String create = "CREATE TABLE IF NOT EXISTS Stats(UUID varchar(36), NICK VARCHAR(16), PRIMARY KEY (UUID))";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(create);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
    public void customStatisticCreate(String s) {
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " DOUBLE DEFAULT 0";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }
    public void statisticRemove(String s) {
        Connection connection = null;
        String update = "ALTER TABLE Stats DROP COLUMN " + s;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }
    public void pushCustomStats(Player player) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 3; i < columnsNumber + 1; i++) {
                if(!rsmd.getColumnName(i).contains("x")) {
                    String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                    statement = connection.prepareStatement(update);
                    double statystyka;
                    statystyka = player.getStatistic(Statistic.valueOf(rsmd.getColumnName(i)));
                    statement.setDouble(1, statystyka);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.execute();
                }
                else{
                    String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                    statement = connection.prepareStatement(update);
                    double statystyka;
                    String columnName = rsmd.getColumnName(i);
                    String[] split= columnName.split("x");
                    if(split[0].equals("KILL_ENTITY") || split[0].equals("ENTITY_KILLED_BY")) {
                        statystyka = player.getStatistic(Statistic.valueOf(split[0]), EntityType.valueOf(split[1]));
                        statement.setDouble(1, statystyka);
                        statement.setString(2, player.getUniqueId().toString());
                        statement.execute();
                    }
                   if(split[0].equals("MINE_BLOCK")){
                       statystyka = player.getStatistic(Statistic.valueOf(split[0]), Material.valueOf(split[1]));
                       statement.setDouble(1, statystyka);
                       statement.setString(2, player.getUniqueId().toString());
                       statement.execute();
                   }
                   if(split[0].equals("PICKUP") || split[0].equals("DROP") || split[0].equals("CRAFT_ITEM") || split[0].equals("BREAK_ITEM") || split[0].equals("USE_ITEM")){
                       statystyka = player.getStatistic(Statistic.valueOf(split[0]), Material.valueOf(split[1]));
                       statement.setDouble(1, statystyka);
                       statement.setString(2, player.getUniqueId().toString());
                       statement.execute();
                   }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }


    //TODO
    public void customAdvancedStatisticCreate(String s){
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " DOUBLE DEFAULT 0";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }

    //TODO porownywanie przez nazwe kolumny np Statistic_KILL_ENTITY EntityType.ZOMBIE
    public void pushCustomAdvancedStats(Player player) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 3; i < columnsNumber + 1; i++) {
                String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                statement = connection.prepareStatement(update);
                double statystyka;
                String columnName = rsmd.getColumnName(i);
                String[] split= columnName.split("x");
                    statystyka = player.getStatistic(Statistic.valueOf(split[0]), EntityType.valueOf(split[1]));
                    statement.setDouble(1, statystyka);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.execute();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }


    public List<String> getCurrentColumNames(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<String> currentCollumns = new ArrayList<>();
        try{
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats");
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for(int i = 3; i<columnsNumber + 1; i++){
                currentCollumns.add(rsmd.getColumnName(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return currentCollumns;
    }


    private String getFirstWord(String text) {

        int index = text.indexOf(' ');

        if (index > -1) { // Check if there is more than one word.

            return text.substring(0, index).trim(); // Extract first word.

        } else {

            return text; // Text is the first word itself.
        }
    }

}


/*
    public void customStatisticCreate(String s, String variable){
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " " + variable;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement(update);

            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }   finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null)
                try {
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

    }
    /*


    }

/**
 * Pobieranie wartosci z sql
 */

/*
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            int i = results.getInt("KILLED_ZOMBIES");
            results.close();
 */
