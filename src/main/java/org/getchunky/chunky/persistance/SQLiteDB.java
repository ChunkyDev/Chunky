package org.getchunky.chunky.persistance;

import lib.PatPeter.SQLibrary.SQLite;
import org.bukkit.Chunk;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.util.Logging;
import lib.PatPeter.SQLibrary.MySQL;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;

public class SQLiteDB extends SQLDB {

    private SQLite db;

    @Override
    public ResultSet query(String query) {
        try {
            return db.query(query);
        } catch (Exception e) {return null;}}

    public boolean connect(Plugin plugin) {
        Logging.info("Connecting to MySQL database.");
        db = new SQLite(
                Logging.getLog(),
                Logging.getNameVersion(),
                Chunky.getInstance().getDescription().getName(),
                Chunky.getInstance().getDataFolder().getPath());

        try {
            db.open();
            if(!db.checkConnection()) return false;
        } catch (Exception e) {
            Logging.severe("Connection to SQLite database failed.");
            return false;}

        try {checkTables();}
        catch (Exception e) {
            Logging.severe("Could not access SQLite tables.");
            return false;}
        Logging.info("Connected to SQLite database and verified tables.");
        return true;
    }

    private Boolean checkTables() throws Exception {
        if(!this.db.checkTable("chunky_objects")) {
            if(!db.createTable(QueryGen.createObjectTable())) return false;
            Logging.info("Created chunky_objects table.");}
        if(!this.db.checkTable("chunky_ownership")) {
            if(!db.createTable(QueryGen.createOwnerShipTable())) return false;
            Logging.info("Created chunky_ownership table.");}

        if(!this.db.checkTable("chunky_permissions")) {
            if(!db.createTable(QueryGen.createPermissionsTable())) return false;
            Logging.info("Created chunky_permissions table.");}
        return true;
    }

    public void disconnect() {
        db.close();
    }
}
