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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class SQLDB implements Database {

    public abstract ResultSet query(String query);

    private void addOwnedChunks(ChunkyPlayer chunkyPlayer) {
        ResultSet chunks = getChunks(chunkyPlayer);
        try {
            while(chunks.next()) {
                ChunkyCoordinates coordinates = new ChunkyCoordinates(chunks.getString("World"),chunks.getInt("x"),chunks.getInt("z"));
                ChunkyChunk chunk = ChunkyManager.getChunk(coordinates);
                chunk.setName(chunks.getString("Name"));
                chunk.addOwner(chunkyPlayer);

            }
        } catch (SQLException ignored) {
        }
    }

    public void loadData() {
        Logging.info("Reading tables.");
        ResultSet rows = getPlayers();
        try {
            while(rows.next()) {
                ChunkyPlayer player = ChunkyManager.getChunkyPlayer(rows.getString("name"));
                addOwnedChunks(player);
            }
        } catch (SQLException ignored) {
        }
        Logging.info("Loaded data from tables.");
    }

    public void updateChunk(ChunkyChunk chunky) {
        query(QueryGen.getUpdateChunk(chunky));
    }

    public void addPlayer(ChunkyPlayer player) {
        query(QueryGen.getAddPlayer(player));
    }

    private ResultSet getPlayers() {
        return query(QueryGen.getAllPlayers());
    }

    private ResultSet getChunks(ChunkyPlayer chunkyPlayer) {
        return getOwned(chunkyPlayer, ChunkyChunk.class.getName().hashCode());
    }

    public ResultSet getOwned(ChunkyObject owner, int ownableType) {
        return query(QueryGen.getOwned(owner, ownableType));
    }

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.getAddOwnership(owner, ownable));
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.getRemoveOwnership(owner, ownable));
    }

    public void addType(int hash, String name) {
        query(QueryGen.getAddType(hash, name));
    }

    public ResultSet getTypeName(int hash) {
        return query(QueryGen.getGetType(hash));
    }
}
