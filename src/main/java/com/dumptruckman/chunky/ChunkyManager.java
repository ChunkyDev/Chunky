package com.dumptruckman.chunky;

import com.dumptruckman.chunky.object.ChunkyPlayer;

import java.util.HashMap;

public class ChunkyManager {

    private static HashMap<String, ChunkyPlayer> players = new HashMap<String, ChunkyPlayer>();

    public static ChunkyPlayer getChunkyPlayer(String name)
    {
        if(players.containsKey(name)) return players.get(name);
        ChunkyPlayer player = new ChunkyPlayer();
        players.put(name,player);
        return player;
    }

}
