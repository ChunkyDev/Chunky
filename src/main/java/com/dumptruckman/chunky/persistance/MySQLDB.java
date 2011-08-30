package com.dumptruckman.chunky.persistance;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.config.Config;
import com.dumptruckman.chunky.util.Logging;
import lib.PatPeter.SQLibrary.MySQL;

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
        return true;
    }

    private void checkTables() throws Exception {
        if(!this.db.checkTable("chunky-chunks")) {
            Logging.info("Creating chunky-chunks table.");
            //TODO Figure out table stuff.
        }

        if(!this.db.checkTable("chunky-players")) {
            Logging.info("Creating chunky-chunks table.");
            //TODO Figure out table stuff.
        }
    }
}
