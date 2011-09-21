package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.object.*;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.util.Logging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

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
                setDefaultPermissions(chunk);

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
                System.out.println(player.getName());
                addOwnedChunks(player);
                addOwnedPlayers(player);
                setPermissions(player);
                setDefaultPermissions(player);
            }
        } catch (SQLException ignored) {
        }
        Logging.info("Loaded data from tables.");
    }

    private void setPermissions(ChunkyPlayer player) {
        ResultSet perms = query(QueryGen.getSelectPermissions(player.getId()));
        try {
            while(perms.next()) {
                String object = perms.getString("ObjectId");
                Logging.debug("Setting perms for " + player.getName() + " on " + object);
                player.setPerm(object, ChunkyPermissions.Flags.BUILD, (perms.getInt("BUILD") == 1), false);
                player.setPerm(object, ChunkyPermissions.Flags.DESTROY, (perms.getInt("DESTROY") == 1), false);
                player.setPerm(object, ChunkyPermissions.Flags.SWITCH, (perms.getInt("SWITCH") == 1), false);
                player.setPerm(object, ChunkyPermissions.Flags.ITEMUSE, (perms.getInt("ITEMUSE") == 1), false);
            }
        } catch (SQLException ignored) {
        }

    }

    private void setDefaultPermissions(ChunkyObject object) {

        ResultSet perms = query(QueryGen.getSelectDefaultPermissions(object.getId()));
        try {
            while(perms.next()) {
                object.setDefaultPerm(ChunkyPermissions.Flags.BUILD, (perms.getInt("BUILD") == 1), false);
                object.setDefaultPerm(ChunkyPermissions.Flags.DESTROY, (perms.getInt("DESTROY") == 1), false);
                object.setDefaultPerm(ChunkyPermissions.Flags.SWITCH, (perms.getInt("SWITCH") == 1), false);
                object.setDefaultPerm(ChunkyPermissions.Flags.ITEMUSE, (perms.getInt("ITEMUSE") == 1), false);
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
        return query(QueryGen.getPlayersWithOwnership());
    }

    private ResultSet getOwnedChunks(ChunkyPlayer chunkyPlayer) {
        return getOwned(chunkyPlayer, ChunkyChunk.class.getName());
    }

    private ResultSet getOwnedPlayers(ChunkyPlayer chunkyPlayer) {
        return getOwned(chunkyPlayer, ChunkyPlayer.class.getName());
    }

    public ResultSet getOwned(ChunkyObject owner, String ownableType) {
        return query(QueryGen.getOwned(owner, ownableType));

    }

    public void addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.getAddOwnership(owner, ownable));
    }

    public void removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        query(QueryGen.getRemoveOwnership(owner, ownable));
    }

    public void updatePermissions(String permissibleId, String objectId, EnumSet<ChunkyPermissions.Flags> flags) {
        query(QueryGen.getUpdatePermissions(permissibleId, objectId, flags));
    }

    public void removePermissions(String permissibleId, String objectId) {
        query(QueryGen.getRemovePermissions(permissibleId, objectId));
    }

    public void removeAllPermissions(String objectId) {
        query(QueryGen.getRemoveAllPermissions(objectId));
    }

}
