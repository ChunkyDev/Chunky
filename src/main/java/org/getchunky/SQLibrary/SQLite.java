/**
 * SQLite
 * Inherited subclass for reading and writing to and from an SQLite file.
 *
 * Date Created: 2011-08-26 19:08
 * @author PatPeter
 */
package org.getchunky.SQLibrary;

/*
 * SQLite
 */

import java.io.File;
import java.sql.*;
import java.util.logging.Logger;

/*
 * Both
 */
//import java.net.MalformedURLException;

public class SQLite extends DatabaseHandler {
    public String location;
    public String name;
    private File sqlFile;

    public SQLite(Logger log, String prefix, String name, String location) {
        super(log, prefix, "[SQLite] ");
        this.name = name;
        this.location = location;
        File folder = new File(this.location);
        if (this.name.contains("/") ||
                this.name.contains("\\") ||
                this.name.endsWith(".db")) {
            this.writeError("The database name can not contain: /, \\, or .db", true);
        }
        if (!folder.exists()) {
            folder.mkdir();
        }

        sqlFile = new File(folder.getAbsolutePath() + File.separator + name + ".db");
    }

    /*@Override
     public void writeInfo(String toWrite) {
         if (toWrite != null) {
             this.log.info(this.PREFIX + this.DATABASE_PREFIX + toWrite);
         }
     }

     @Override
     public void writeError(String toWrite, boolean severe) {
         if (severe) {
             if (toWrite != null) {
                 this.log.severe(this.PREFIX + this.DATABASE_PREFIX + toWrite);
             }
         } else {
             if (toWrite != null) {
                 this.log.warning(this.PREFIX + this.DATABASE_PREFIX + toWrite);
             }
         }
     }*/

    protected boolean initialize() {
        try {
            Class.forName("org.sqlite.JDBC");

            return true;
        } catch (ClassNotFoundException e) {
            this.writeError("You need the SQLite library " + e, true);
            return false;
        }
    }

    @Override
    public Connection open() {
        if (initialize()) {
            try {
                this.connection = DriverManager.getConnection("jdbc:sqlite:" +
                        sqlFile.getAbsolutePath());
                return this.connection;
            } catch (SQLException e) {
                this.writeError("SQLite exception on initialize " + e, true);
            }
        }
        return this.connection;
    }

    @Override
    public void close() {
        if (this.connection != null)
            try {
                this.connection.close();
            } catch (SQLException ex) {
                this.writeError("Error on Connection close: " + ex, true);
            }
    }

    @Override
    public Connection getConnection() {
        if (this.connection == null)
            return open();
        return this.connection;
    }

    @Override
    public boolean checkConnection() {
        //Connection con = this.getConnection(); // Why reopen the connection if the user only wants to hasPerm it?
        if (this.connection != null)
            return true;
        return false;
    }

    @Override
    public ResultSet query(String query) {
        //Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            this.connection = this.getConnection();
            statement = this.connection.createStatement();

            switch (this.getStatement(query)) {
                case SELECT:
                    result = statement.executeQuery(query);
                    return result;

                default:
                    statement.executeUpdate(query);
                    return result;
            }
        } catch (SQLException ex) {
            if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                return retryResult(query);
                //this.writeError("",false);
            } else {
                this.writeError("Error at SQL Query: " + ex.getMessage(), false);
            }

        }
        return null;
    }

    @Override
    public boolean createTable(String query) {
        Statement statement = null;
        try {
            if (query.equals("") || query == null) {
                this.writeError("SQL Create Table query empty.", true);
                return false;
            }

            statement = this.connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException ex) {
            this.writeError(ex.getMessage(), true);
            return false;
        }
    }

    @Override
    public boolean checkTable(String table) {
        DatabaseMetaData dbm = null;
        try {
            dbm = this.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, table, null);
            if (tables.next())
                return true;
            else
                return false;
        } catch (SQLException e) {
            this.writeError("Failed to hasPerm if table \"" + table + "\" exists: " + e.getMessage(), true);
            return false;
        }
    }

    @Override
    public boolean wipeTable(String table) {
        Statement statement = null;
        String query = null;
        try {
            if (!this.checkTable(table)) {
                this.writeError("Error at Wipe Table: table, " + table + ", does not exist", true);
                return false;
            }
            //Connection connection = getConnection();
            this.connection = this.getConnection();
            statement = this.connection.createStatement();
            query = "DELETE FROM " + table + ";";
            statement.executeQuery(query);
            return true;
        } catch (SQLException ex) {
            if (!(ex.getMessage().toLowerCase().contains("locking") ||
                    ex.getMessage().toLowerCase().contains("locked")) &&
                    !ex.toString().contains("not return ResultSet"))
                this.writeError("Error at SQL Wipe Table Query: " + ex, false);
            return false;
        }
    }

    /*
      * <b>retry</b><br>
      * <br>
      * Retries.
      * <br>
      * <br>
      * @param query The SQL query.
      */
    public void retry(String query) {
        boolean passed = false;
        Statement statement = null;

        while (!passed) {
            try {
                //Connection connection = getConnection();
                this.connection = this.getConnection();
                statement = this.connection.createStatement();
                statement.executeQuery(query);
                passed = true;
            } catch (SQLException ex) {
                if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                    passed = false;
                } else {
                    this.writeError("Error at SQL Query: " + ex.getMessage(), false);
                }
            }
        }
    }

    /*
      * Retries a result.
      *
      * @param query The SQL query to retry.
      * @return The SQL query result.
      */
    public ResultSet retryResult(String query) {
        boolean passed = false;
        Statement statement = null;
        ResultSet result = null;

        while (!passed) {
            try {
                //Connection connection = getConnection();
                this.connection = this.getConnection();
                statement = this.connection.createStatement();
                result = statement.executeQuery(query);
                passed = true;
                return result;
            } catch (SQLException ex) {
                if (ex.getMessage().toLowerCase().contains("locking") || ex.getMessage().toLowerCase().contains("locked")) {
                    passed = false;
                } else {
                    this.writeError("Error at SQL Query: " + ex.getMessage(), false);
                }
            }
        }

        return null;
    }
}