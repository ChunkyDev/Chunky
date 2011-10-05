package org.getchunky.chunky.module;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.event.ChunkyListener;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyModuleManager {

    /**
     * Calls a Chunky event with the given details
     *
     * @param event Event details
     */
    public void callEvent(ChunkyEvent event);

    /**
     * Registers the given Chunky event to the specified listener
     *
     * @param type     ChunkyEventType to register
     * @param listener ChunkyListener to register
     * @param priority Priority of this event
     * @param plugin   Plugin to register
     */
    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Plugin plugin);

    /**
     * Registers a command with Chunky.  Any command you register will automatically be given the "help" and "?" sub-commands.
     *
     * @param command Command to register
     * @return True if the command has not already been registered
     * @throws ChunkyUnregisteredException if the owner command has not been registered
     */
    public boolean registerCommand(ChunkyCommand command) throws ChunkyUnregisteredException;

    /**
     * Retrieve a ChunkyCommand by looking it up by it's full name.  Example: /chunky.claim.radius
     *
     * @param fullName Full name of command to look up
     * @return The command found or null if none found by specified full name
     */
    public ChunkyCommand getCommandByName(String fullName);

    /**
     * Verifies if a command with given full name is registered.
     *
     * @param fullName Full name of command to hasPerm.  Example: /chunky.claim.radius
     * @return true if the command is registered
     */
    public boolean isCommandRegistered(String fullName);

    /**
     * Gets all base commands registered.  This will only return commands with a null parent.
     *
     * @return base commands
     */
    public HashMap<String, ChunkyCommand> getRegisteredCommands();

    /**
     * Retrieves a command by an alias.  You must know the parent command in order to use this.
     *
     * @param parentCommand Parent command of command to look up
     * @param alias         Alias of command to look up
     * @return Command found if any or null if none found
     */
    public ChunkyCommand getCommandByAlias(ChunkyCommand parentCommand, String alias);

    /**
     * Mostly used internally to parse commands from PlayerCommandPreprocessEvents to see if they should fire a Chunky Command.
     *
     * @param sender   Sender of command
     * @param commands Array of words used in command
     */
    public void parseCommand(CommandSender sender, String[] commands);

    /**
     * Register your module with Chunky.  The main purpose of this method is so your module shows up in lists.
     *
     * @param module Plugin class of your Chunky Module.
     */
    public void registerModule(JavaPlugin module);

    /**
     * Retrieves all modules registered with Chunky.
     *
     * @return Set of registered Chunky Modules
     */
    public HashSet<JavaPlugin> getRegisteredModules();
}
