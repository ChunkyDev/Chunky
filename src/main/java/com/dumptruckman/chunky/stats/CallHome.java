package com.dumptruckman.chunky.stats;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.Plugin;

public class CallHome implements Runnable{

    Plugin plugin;

    public CallHome(Plugin plugin) {
        this.plugin = plugin;
    }


    public void run() {
        try {
            if(postUrl().contains("Success")) return;
        } catch (Exception ignored) {
        }
        System.out.print("Could not call home.");
    }

    private String postUrl() throws Exception {
        String url = String.format("http://plugins.blockface.org/usage/update.php?name=%s&build=%s&plugin=%s&port=%s",
                plugin.getServer().getName(),
                plugin.getDescription().getVersion(),
                plugin.getDescription().getName(),
                plugin.getServer().getPort());
        URL oracle = new URL(url);
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            inputLine += "";
        return inputLine;
    }
}
