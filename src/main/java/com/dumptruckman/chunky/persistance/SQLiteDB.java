package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyCoordinates;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.util.Logging;
import lib.PatPeter.SQLibrary.SQLite;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dumptruckman, SwearWord
 */
public class SQLiteDB extends SQLDB {

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
        if(!checkTables()) {
            Logging.info("Table generation failed.");
            return false;
        }
        Logging.info("Successfully connected to SQLite database.");
        return true;
    }

    private Boolean checkTables() {
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

        return true;

    }

    @Override
    public ResultSet query(String query) {
        return db.query(query);
    }

    private void addOwnedChunks(ChunkyPlayer chunkyPlayer) {
        ResultSet chunks = getOwned(chunkyPlayer, ChunkyChunk.class.hashCode());
        try {
            while(chunks.next()) {
                ChunkyCoordinates coordinates = new ChunkyCoordinates(chunks.getString("world"),chunks.getInt("x"),chunks.getInt("z"));
                ChunkyChunk chunk = ChunkyManager.getChunk(coordinates);
                chunk.setName(chunks.getString("Name"));
                chunkyPlayer.addOwnable(chunk);
            }
        } catch (SQLException ignored) {
        }
    }

    public void closeDB() {
        this.db.close();
    }
}
