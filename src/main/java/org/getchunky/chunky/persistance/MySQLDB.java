package org.getchunky.chunky.persistance;

import org.bukkit.plugin.Plugin;
import org.getchunky.SQLibrary.MySQL;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.util.Logging;

import java.sql.ResultSet;

public class MySQLDB extends SQLDB {

    private MySQL db;

    @Override
    public ResultSet query(String query) {
        try {
            return db.query(query);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean connect(Plugin plugin) {
        Logging.info("Connecting to MySQL database.");
        db = new MySQL(
                Logging.getLog(),
                Logging.getNameVersion(),
                Config.getHost(),
                Config.getPort(),
                Config.getDatabase(),
                Config.getUsername(),
                Config.getPassword());

        try {
            db.open();
            if (!db.checkConnection()) return false;
        } catch (Exception e) {
            Logging.severe("Connection to MySQL database failed.");
            return false;
        }

        try {
            checkTables();
        } catch (Exception e) {
            Logging.severe("Could not access MySQL tables.");
            return false;
        }
        Logging.info("Connected to MySQL database and verified tables.");
        return true;
    }

    private Boolean checkTables() throws Exception {
        if (!this.db.checkTable("chunky_objects")) {
            if (!db.createTable(QueryGen.createObjectTable())) return false;
            Logging.info("Created chunky_objects table.");
        }

        if (!this.db.checkTable("chunky_ownership")) {
            if (!db.createTable(QueryGen.createOwnerShipTable())) return false;
            Logging.info("Created chunky_ownership table.");
        }

        if (!this.db.checkTable("chunky_permissions")) {
            if (!db.createTable(QueryGen.createPermissionsTable())) return false;
            Logging.info("Created chunky_permissions table.");
        }

        if (!this.db.checkTable("chunky_groups")) {
            if (!db.createTable(QueryGen.createGroupsTable())) return false;
            Logging.info("Created chunky_groups table.");
        }
        return true;
    }

    public void disconnect() {
        db.close();
    }
}
