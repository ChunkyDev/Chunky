package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import lib.PatPeter.SQLibrary.MySQL;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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
        db = new MySQL(Logging.getLog(),Logging.getNameVersion(), Config.getHost(),Config.getDatabase(),Config.getUsername(),Config.getPassword(),Config.getPort());
        try {
            if(!db.open()) return false;
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
        Logging.info("Reading MySQL tables.");
        loadData();
        Logging.info("Loaded data from MySQL tables.");
        return true;
    }

    private void checkTables() throws Exception {
        if(!this.db.checkTable("chunky-chunks")) {
            Logging.info("Creating chunky-chunks table.");
            //TODO Figure out table stuff.
        }

        if(!this.db.checkTable("chunky-players")) {
            Logging.info("Creating chunky-players table.");
            //TODO Figure out table stuff.
        }

        if(!this.db.checkTable("chunky-ownership")) {
            Logging.info("Creating chunky-chunks table.");
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
        try {
            return db.query(SQLstatements.getAllPlayers());
        } catch (Exception e) {
            return null;
        }
    }

    private ResultSet getChunks(String chunkyPlayer) {
        try {
            String query = SQLstatements.getOwnedChunks(chunkyPlayer);
            return db.query(query);
        } catch (Exception e) {
            return null;
        }
    }
}
