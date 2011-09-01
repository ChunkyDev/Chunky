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
import sun.security.util.Debug;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dumptruckman, SwearWord
 */
public class MySQLDB implements Database {

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

    private void checkTables() throws Exception {

        if(!this.db.checkTable("chunky_types")) {
            db.createTable(QueryGen.getCreateTypeTable());
            Logging.info("Created chunky_types table.");
        }

        if(!this.db.checkTable("chunky_ChunkyChunk")) {
            db.createTable(QueryGen.getCreateChunkTable());
            Logging.info("Created chunky_ChunkyChunk table.");
        }

        if(!this.db.checkTable("chunky_ChunkyPlayer")) {
            db.createTable(QueryGen.getCreatePlayerTable());
            Logging.info("Created chunky_ChunkyPlayer table.");
        }

        if(!this.db.checkTable("chunky_ownership")) {
            db.createTable(QueryGen.getCreateOwnerShipTable());
            Logging.info("Created chunky_ownership table.");
        }
    }

    private void addOwnedChunks(ChunkyPlayer chunkyPlayer) {
        ResultSet chunks = getChunks(chunkyPlayer);
        try {
            while(chunks.next()) {
                ChunkyCoordinates coordinates = new ChunkyCoordinates(chunks.getString("World"),chunks.getInt("x"),chunks.getInt("z"));
                ChunkyChunk chunk = ChunkyManager.getChunk(coordinates);
                chunk.addOwner(chunkyPlayer);

            }
        } catch (SQLException ignored) {
        }
    }

    public void loadData() {
        Logging.info("Reading MySQL tables.");
        ResultSet rows = getPlayers();
        try {
            while(rows.next()) {
                ChunkyPlayer player = ChunkyManager.getChunkyPlayer(rows.getString("name"));
                addOwnedChunks(player);
            }
        } catch (SQLException ignored) {
        }
        Logging.info("Loaded data from MySQL tables.");
    }

    public void addChunk(ChunkyChunk chunky) {
        try {
            db.query(QueryGen.getAddChunk(chunky));
        } catch (Exception ignored) {}
    }

    public void addPlayer(ChunkyPlayer player) {
        try {
            db.query(QueryGen.getAddPlayer(player));
        } catch (Exception ignored) {}
    }

    private ResultSet getPlayers() {
        try {
            return db.query(QueryGen.getAllPlayers());
        } catch (Exception e) {
            return null;
        }
    }

    private ResultSet getChunks(ChunkyPlayer chunkyPlayer) {
        return getOwned(chunkyPlayer, ChunkyChunk.class.getName().hashCode());
    }

    public ResultSet getOwned(ChunkyObject owner, int ownableType) {
        try {
            String query = QueryGen.getOwned(owner, ownableType);
            return db.query(query);
        } catch (Exception e) {
            Logging.debug(e.getMessage());
            return null;
        }
    }

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        try {
            db.query(QueryGen.getAddOwnership(owner, ownable));
        } catch (Exception ignored) {}
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        try {
            db.query(QueryGen.getRemoveOwnership(owner, ownable));
        } catch (Exception ignored) {}
    }

    public void addType(int hash, String name) {
        try {
            db.query(QueryGen.getAddType(hash, name));
        } catch (Exception ignored) {
        }
    }

    public ResultSet getTypeName(int hash) {
        try {
            return db.query(QueryGen.getGetType(hash));
        } catch (Exception ignored) {
            return null;
        }
    }

    public void closeDB() {
        db.close();
    }
}
