package org.getchunky.chunky.persistance;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.permission.PermissionRelationship;

public class QueryGen {

    public static String selectAllPermissions() {
        return "SELECT * FROM chunky_permissions";
    }

    public static String selectAllObjects() {
        return "SELECT * FROM chunky_objects";
    }

    public static String selectAllOwnership() {
        return format("SELECT * FROM chunky_ownership");
    }

    public static String updateObject(ChunkyObject object) {
        return format("REPLACE INTO chunky_objects (id,type,data) VALUES ('%s','%s','%s')", object.getId(), object.getType(), object.toJSONString());
    }

    public static String createPermissionsTable() {
        return
                "CREATE TABLE chunky_permissions (" +
                        "PermissibleId VARCHAR(255) NOT NULL," +
                        "ObjectId VARCHAR(255) NOT NULL," +
                        "PermissibleType VARCHAR(128) NOT NULL,  " +
                        "ObjectType VARCHAR(128) NOT NULL,  " +
                        "data TEXT NULL," +
                        "PRIMARY KEY (PermissibleId, ObjectId) )";
    }

    public static String createOwnerShipTable() {
        return
                "CREATE TABLE chunky_ownership (" +
                        "OwnerId VARCHAR(255) NOT NULL,  " +
                        "OwnableId VARCHAR(255) NOT NULL,  " +
                        "OwnerType VARCHAR(128) NOT NULL,  " +
                        "OwnableType VARCHAR(128) NOT NULL,  " +
                        "PRIMARY KEY (OwnableType, OwnableId) )";
    }

    public static String createObjectTable() {
        return
                "CREATE TABLE chunky_objects (" +
                        "id VARCHAR(50) NOT NULL," +
                        "type VARCHAR(50) NOT NULL," +
                        "data TEXT NULL," +
                        "PRIMARY KEY (id, type)" +
                        ")";
    }

    public static String updatePermissions(ChunkyObject permissible, ChunkyObject object, PermissionRelationship perms) {

        return
                format("REPLACE INTO chunky_permissions (" +
                        "PermissibleId, " +
                        "ObjectId, " +
                        "PermissibleType, " +
                        "ObjectType, " +
                        "data)" +
                        "VALUES ('%s','%s','%s','%s','%s')",
                        permissible.getId(), object.getId(),
                        permissible.getType(), object.getType(),
                        perms.toJSONString());
    }

    public static String removePermissions(ChunkyObject permissible, ChunkyObject object) {
        return
                format("DELETE FROM chunky_permissions where " +
                        "PermissibleId = '%s' " +
                        "AND ObjectId = '%s'" +
                        "AND PermissibleType = '%s'" +
                        "AND ObjectType = '%s'",
                        permissible.getId(), object.getId(),
                        permissible.getType(), object.getType());
    }

    public static String removeAllPermissions(ChunkyObject object) {
        return
                format("DELETE FROM chunky_permissions where " +
                        "ObjectId = '%s'" +
                        "AND ObjectType = '%s'", object.getId(), object.getType());
    }

    public static String addOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
                format("REPLACE INTO chunky_ownership (" +
                        "OwnerId, " +
                        "OwnableId, " +
                        "OwnerType, " +
                        "OwnableType) " +
                        "VALUES ('%s','%s','%s','%s')", owner.getId(), ownable.getId(), owner.getType(), ownable.getType());
    }

    public static String removeOwnership(ChunkyObject owner, ChunkyObject ownable) {
        return
                format("DELETE FROM chunky_ownership where " +
                        "OwnerId = '%s' " +
                        "AND OwnableId = '%s'", owner.getId(), ownable.getId());

    }

    public static String deleteObject(ChunkyObject chunkyObject) {
        return format("DELETE FROM chunky_objects WHERE id = %s AND type='%s'",chunkyObject.getId(),chunkyObject.getType());

    }

    public static String deleteAllPermissions(ChunkyObject chunkyObject) {
        return format("DELETE FROM chunky_permissions WHERE PermissibleId = %s OR ObjectId = %s",chunkyObject.getId(), chunkyObject.getId());
    }

    public static String deleteAllOwnership(ChunkyObject chunkyObject) {
        return format("DELETE FROM chunky_ownership WHERE OwnerId = %s OR OwnableId = %s",chunkyObject.getId(), chunkyObject.getId());
    }

    private static String format(String input, String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = sanitize(args[i]);
        }
        return String.format(input, args);
    }

    private static String sanitize(String input) {
        return input.replace("'", "\\'");
    }
}
