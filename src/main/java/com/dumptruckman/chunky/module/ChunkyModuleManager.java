package com.dumptruckman.chunky.module;

import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyModuleManager {

    /**
     * Calls an event with the given details
     *
     * @param event Event details
     */
    public void callEvent(ChunkyEvent event);

    /**
     * Registers the given event to the specified listener
     *
     * @param type ChunkyEventType to register
     * @param listener ChunkyListener to register
     * @param priority Priority of this event
     * @param plugin Plugin to register
     */
    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Plugin plugin);

    /**
     * Registers a command with Chunky
     *
     * @param command Command to register
     * @return True if the command has not already been registered
     * @throws ChunkyUnregisteredException if the parent command has not been registered
     */
    public boolean registerCommand(ChunkyCommand command) throws ChunkyUnregisteredException;

    public ChunkyCommand getCommandByName(String fullName);

    public boolean isCommandRegistered(String fullName);

    public ChunkyCommand getCommandByAlias(ChunkyCommand parentCommand, String alias);

    public void parseCommand(CommandSender sender, String[] commands);
}
