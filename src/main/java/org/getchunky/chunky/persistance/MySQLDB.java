package org.getchunky.chunky.persistance;

import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.util.Logging;
import lib.PatPeter.SQLibrary.MySQL;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;

public class MySQLDB extends SQLDB {

    private MySQL db;

    @Override
    public ResultSet query(String query) {
        try {
            return db.query(query);
        } catch (Exception e) {return null;}}

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
            if(!db.checkConnection()) return false;
        } catch (Exception e) {
            Logging.severe("Connection to MySQL database failed.");
            return false;}

        try {checkTables();}
        catch (Exception e) {
            Logging.severe("Could not access MySQL tables.");
            return false;}
        Logging.info("Connected to MySQL database and verified tables.");
        return true;
    }

    private Boolean checkTables() throws Exception {
        if(!this.db.checkTable("chunky_ChunkyChunk")) {
            if(!db.createTable(QueryGen.getCreateChunkTable())) return false;
            Logging.info("Created chunky_ChunkyChunk table.");}

        if(!this.db.checkTable("chunky_ChunkyPlayer")) {
            if(!db.createTable(QueryGen.getCreatePlayerTable())) return false;
            Logging.info("Created chunky_ChunkyPlayer table.");}

        if(!this.db.checkTable("chunky_ownership")) {
            if(!db.createTable(QueryGen.getCreateOwnerShipTable())) return false;
            Logging.info("Created chunky_ownership table.");}

        if(!this.db.checkTable("chunky_permissions")) {
            if(!db.createTable(QueryGen.getCreatePermissionsTable())) return false;
            Logging.info("Created chunky_permissions table.");}
        return true;
    }

    public void disconnect() {
        db.close();
    }
}
