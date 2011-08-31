package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
import lib.PatPeter.SQLibrary.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dumptruckman, SwearWord
 */
public class SQLiteDB implements Database {

    private SQLite db;
    private Chunky plugin;

    public SQLiteDB() {
        this.plugin = Chunky.getInstance();
    }

    public Boolean load() {
        Logging.info("Connecting to SQLite database.");
        this.db = new SQLite(Logging.getLog(), Logging.getNameVersion(), "data", plugin.getDataFolder().getPath());
        db.open();
        if(!db.checkConnection())
        {
            Logging.severe("Unable to access flatfile: " + db.name);
            return false;
        }
        checkTables();
        Logging.info("Successfully connected to SQLite database.");
        return true;
    }

    private void checkTables() {
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
            Logging.info("Creating chunky_ownership table.");
            db.createTable(QueryGen.getCreateOwnerShipTable());
        }

    }

    private void addOwnedChunks(ChunkyPlayer chunkyPlayer) {
        ResultSet chunks = getOwned(chunkyPlayer, ChunkyChunk.class.hashCode());
        try {
            while(chunks.next()) {
                ChunkyCoordinates coordinates = new ChunkyCoordinates(chunks.getString("world"),chunks.getInt("x"),chunks.getInt("y"));
                ChunkyChunk chunk = new ChunkyChunk(coordinates);
                ChunkyManager.addChunk(chunk);
                chunkyPlayer.addOwnable(chunk);
            }
        } catch (SQLException ignored) {
        }
    }

    public void loadData() {
        Logging.info("Reading SQLite tables.");
        ResultSet rows = getPlayers();
        try {
            while(rows.next()) {
                ChunkyPlayer player = ChunkyManager.getChunkyPlayer(rows.getString("name"));
                addOwnedChunks(player);
            }
        } catch (SQLException ignored) {
        }
        Logging.info("Loaded data from SQLite tables.");
    }

    private ResultSet getPlayers() {
        return db.query(QueryGen.getAllPlayers());
    }


    public ResultSet getOwned(ChunkyObject owner, int ownableType) {
        String query = QueryGen.getOwned(owner, ownableType);
        return db.query(query);
    }


    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        db.query(QueryGen.getAddOwnership(owner, ownable));
        Logging.debug("Executed: " + QueryGen.getAddOwnership(owner, ownable));
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        db.query(QueryGen.getRemoveOwnership(owner,ownable));
    }

    public void addType(int hash, String name) {
        db.query(QueryGen.getAddType(hash,name));
    }

    public ResultSet getTypeName(int Hash) {
        return db.query(QueryGen.getGetType(Hash));
    }

    public void closeDB() {
        this.db.close();
    }
}
