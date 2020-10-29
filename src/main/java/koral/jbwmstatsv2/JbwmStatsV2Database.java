package koral.jbwmstatsv2;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
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

    public static HikariDataSource hikari;


    public void connectToDatabase() {
        ;
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", plugin.getConfig().getString("host"));
        hikari.addDataSourceProperty("port", plugin.getConfig().getInt("port"));
        hikari.addDataSourceProperty("databaseName", plugin.getConfig().getString("database"));
        hikari.addDataSourceProperty("user", plugin.getConfig().getString("username"));
        hikari.addDataSourceProperty("password", plugin.getConfig().getString("password"));
    }

//TODO fix print stack trace from less collumns, and player isn't created.
    public void createPlayerQuery(Player player) {
        Connection connection = null;

        String update = "INSERT INTO Stats VALUES(?,?) ON DUPLICATE KEY UPDATE UUID=?";

        PreparedStatement statement = null;

        try {
            connection = hikari.getConnection();

            statement = connection.prepareStatement(update);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setString(3, player.getUniqueId().toString());
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

    public void customStatisticCreate(String s) {
        Connection connection = null;
        String update = "ALTER TABLE Stats ADD " + s + " DOUBLE";
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

    public void pushCustomStats(UUID uuid, Player player) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Stats WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            ResultSetMetaData rsmd = results.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 3; i < columnsNumber + 1; i++) {
                String update = "UPDATE Stats SET " + rsmd.getColumnName(i) + "=? WHERE UUID=?";
                statement = connection.prepareStatement(update);
                double statystyka;
                statystyka = player.getStatistic(Statistic.valueOf(rsmd.getColumnName(i)));
                statement.setDouble(1, statystyka);
                statement.setString(2, uuid.toString());
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
