package koral.jbwmstatsv2;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class JbwmStatsV2Database {

    JbwmstatsV2 plugin;

    public JbwmStatsV2Database(JbwmstatsV2 plugin) {
        this.plugin = plugin;
    }

    public static HikariDataSource hikari;


    public void connectToDatabase() { ;
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

        String update = "INSERT INTO Stats VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE UUID=?";

        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();

            statement = connection.prepareStatement(update);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setLong(5, 0);
            statement.setInt(6, 0);
            statement.setString(7, player.getUniqueId().toString());
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
        try (Connection connection = hikari.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Stats(UUID varchar(36), NICK VARCHAR(16), KILLS INT, KILLED_ZOMBIES INT, TIME_SPENT LONG, AMMO_USED INT, PRIMARY KEY (UUID))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKillsQuery(UUID uuid, Integer kills) {
        Connection connection = null;
        String update = "UPDATE Stats SET KILLS=? WHERE UUID=?";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            /**
             *Wstrzykiwanie do SQL
             *
             */
            statement = connection.prepareStatement(update);
            statement.setInt(1, kills);
            statement.setString(2, uuid.toString());
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

    public void updateZombieQuery(UUID uuid, int zombiekilled) {
        Connection connection = null;
        String update = "UPDATE Stats SET KILLED_ZOMBIES=? WHERE UUID=?";
        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();
            /**
             *Wstrzykiwanie do SQL
             *
             */
            statement = connection.prepareStatement(update);
            statement.setInt(1, zombiekilled);
            statement.setString(2, uuid.toString());
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

    public void updateTime(UUID uuid, long seconds) {
        Connection connection = null;
        String update = "UPDATE Stats SET TIME_SPENT=? WHERE UUID=?";
        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();

            /**
             *Wstrzykiwanie do SQL
             *
             */
            statement = connection.prepareStatement(update);
            statement.setLong(1, seconds);
            statement.setString(2, uuid.toString());
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

    public void updateThrowedSnowballs(UUID uuid, Integer snowballsThrowed) {
        Connection connection = null;
        String update = "UPDATE Stats SET AMMO_USED=? WHERE UUID=?";
        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();

            /**
             *Wstrzykiwanie do SQL
             *
             */
            statement = connection.prepareStatement(update);
            statement.setInt(1, snowballsThrowed);
            statement.setString(2, uuid.toString());
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
    //TODO make it that custom stat will know, what type of variable is it.
    public void customStatisticCreate(String s){
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " INT";
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
