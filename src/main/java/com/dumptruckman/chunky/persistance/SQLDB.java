package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.util.Logging;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class SQLDB implements Database {

    public abstract ResultSet query(String query);

    private void addOwnedChunks(ChunkyPlayer chunkyPlayer) {
        ResultSet chunks = getOwnedChunks(chunkyPlayer);
        try {
            while(chunks.next()) {
                ChunkyCoordinates coordinates = new ChunkyCoordinates(chunks.getString("World"),chunks.getInt("x"),chunks.getInt("z"));
                ChunkyChunk chunk = ChunkyManager.getChunk(coordinates);
                chunk.setName(chunks.getString("Name"));
                chunk.setOwner(chunkyPlayer, true);

            }
        } catch (SQLException ignored) {
        }
    }

    private void addOwnedPlayers(ChunkyPlayer chunkyPlayer) {
        ResultSet players = getOwnedPlayers(chunkyPlayer);
        try {
            while(players.next()) {
                ChunkyPlayer player = ChunkyManager.getChunkyPlayer(players.getString("name"));
                player.setOwner(chunkyPlayer, true);
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
                addOwnedPlayers(player);
                setPermissions(player);
            }
        } catch (SQLException ignored) {
        }
        Logging.info("Loaded data from tables.");
    }

    private void setPermissions(ChunkyPlayer player) {
        ResultSet perms = query(QueryGen.getSelectPermissions(player.hashCode()));
        try {
            while(perms.next()) {
                int object = perms.getInt("ObjectHash");
                player.loadPermFromDB(object, ChunkyPermissions.Flags.BUILD, (perms.getInt("BUILD") == 1));
                player.loadPermFromDB(object, ChunkyPermissions.Flags.DESTROY, (perms.getInt("DESTROY") == 1));
                player.loadPermFromDB(object, ChunkyPermissions.Flags.SWITCH, (perms.getInt("SWITCH") == 1));
                player.loadPermFromDB(object, ChunkyPermissions.Flags.ITEMUSE, (perms.getInt("ITEMUSE") == 1));
            }
        } catch (SQLException ignored) {
        }

    }

    public void updateChunk(ChunkyChunk chunky, String name) {
        query(QueryGen.getUpdateChunk(chunky, name));
    }

    public void addPlayer(ChunkyPlayer player) {
        query(QueryGen.getAddPlayer(player));
    }

    private ResultSet getPlayers() {
        return query(QueryGen.getAllPlayers());
    }

    private ResultSet getOwnedChunks(ChunkyPlayer chunkyPlayer) {
        return getOwned(chunkyPlayer, ChunkyChunk.class.getName().hashCode());
    }

    private ResultSet getOwnedPlayers(ChunkyPlayer chunkyPlayer) {
        return getOwned(chunkyPlayer, ChunkyPlayer.class.getName().hashCode());
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

    public void updatePermissions(int permissiblehash, int objecthash, ChunkyPermissions.Flags type, boolean status) {
        query(QueryGen.getUpdatePermissions(permissiblehash,objecthash,type,status));
    }

}
