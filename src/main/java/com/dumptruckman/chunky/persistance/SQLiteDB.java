package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import lib.PatPeter.SQLibrary.SQLite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
        if(!db.open())
        {
            Logging.severe("Unable to access flatfile: " + db.name);
            return false;
        }
        checkTables();
        Logging.severe("Successfully connected to SQLite database.");
        Logging.info("Reading SQLite tables.");
        loadData();
        Logging.info("Loaded data from SQLite tables.");
        return true;
    }

    private void checkTables() {
        if(!db.checkTable("chunky-chunks")) {
            Logging.info("Creating chunky-chunks table.");
            //TODO Figure out table stuff.
        }

        if(!db.checkTable("chunky-players")) {
            Logging.info("Creating chunky-players table.");
            //TODO Figure out table stuff.
        }

        if(!this.db.checkTable("chunky-ownership")) {
            Logging.info("Creating chunky-ownership table.");
            //TODO Figure out table stuff.
        }

    }

    private void addOwnedChunks(ChunkyPlayer chunkyPlayer) {
        ResultSet chunks = getChunks(chunkyPlayer.getName());
        try {
            while(chunks.next()) {
                ChunkyCoordinates coordinates = new ChunkyCoordinates(chunks.getString("world"),chunks.getInt("x"),chunks.getInt("y"));
                ChunkyChunk chunk = new ChunkyChunk(coordinates);
                ChunkyManager.addChunk(chunk);
                chunkyPlayer.addChunk(chunk);
            }
        } catch (SQLException ignored) {
        }
    }

    private void loadData() {
        ResultSet rows = getPlayers();
        try {
            while(rows.next()) {
                ChunkyPlayer player = ChunkyManager.getChunkyPlayer(rows.getString("name"));
                addOwnedChunks(player);
            }
        } catch (SQLException ignored) {
        }
    }

    private ResultSet getPlayers() {
        return db.query(SQLstatements.getAllPlayers());
    }

    private ResultSet getChunks(String chunkyPlayer) {
        return db.query(SQLstatements.getOwnedChunks(chunkyPlayer));
    }



}
