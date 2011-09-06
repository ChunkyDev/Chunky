package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import lib.PatPeter.SQLibrary.MySQL;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dumptruckman, SwearWord
 */
public class MySQLDB extends SQLDB{

    private MySQL db;
    private Chunky plugin;

    public MySQLDB() {
        this.plugin = Chunky.getInstance();
    }


    public Boolean load() {

        Logging.info("Connecting to MySQL database.");
        db = new MySQL(Logging.getLog(),Logging.getNameVersion(), Config.getHost(),Config.getPort(),Config.getDatabase(),Config.getUsername(),Config.getPassword());
        try {
            db.open();
            if(!db.checkConnection()) {
                return false;
            }
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

    public ResultSet query(String query) {
        try {
            return db.query(query);
        } catch (Exception e) { return null;}
    }

    private Boolean checkTables() throws Exception {

        if(!this.db.checkTable("chunky_types")) {
            if(!db.createTable(QueryGen.getCreateTypeTable())) return false;
            Logging.info("Created chunky_types table.");
        }

        if(!this.db.checkTable("chunky_ChunkyChunk")) {
            if(!db.createTable(QueryGen.getCreateChunkTable())) return false;
            Logging.info("Created chunky_ChunkyChunk table.");
        }

        if(!this.db.checkTable("chunky_ChunkyPlayer")) {
            if(!db.createTable(QueryGen.getCreatePlayerTable())) return false;
            Logging.info("Created chunky_ChunkyPlayer table.");
        }

        if(!this.db.checkTable("chunky_ownership")) {
            if(!db.createTable(QueryGen.getCreateOwnerShipTable())) return false;
            Logging.info("Created chunky_ownership table.");
        }

        if(!this.db.checkTable("chunky_permissions")) {
            if(!db.createTable(QueryGen.getCreatePermissionsTable())) return false;
            Logging.info("Created chunky_permissions table.");
        }

        return true;
    }

    public void closeDB() {
        db.close();
    }
}
