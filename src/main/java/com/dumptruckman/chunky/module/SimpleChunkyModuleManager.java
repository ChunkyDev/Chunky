package com.dumptruckman.chunky.module;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import com.dumptruckman.chunky.event.CustomChunkyEventListener;
import com.dumptruckman.chunky.event.command.ChunkyCommandEvent;
import com.dumptruckman.chunky.event.command.ChunkyCommandListener;
import com.dumptruckman.chunky.event.object.ChunkyObjectListener;
import com.dumptruckman.chunky.event.object.ChunkyObjectNameEvent;
import com.dumptruckman.chunky.event.object.ChunkyObjectOwnershipEvent;
import com.dumptruckman.chunky.event.object.player.*;
import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Level;

/**
 * @author dumptruckman, SwearWord
 */
public class SimpleChunkyModuleManager implements ChunkyModuleManager {

    private HashMap<String, ChunkyCommand> registeredCommands;
    private HashSet<JavaPlugin> registeredModules;
    private final Map<ChunkyEvent.Type, SortedSet<RegisteredChunkyListener>> listeners = new EnumMap<ChunkyEvent.Type, SortedSet<RegisteredChunkyListener>>(ChunkyEvent.Type.class);
    private final Comparator<RegisteredChunkyListener> comparer = new Comparator<RegisteredChunkyListener>() {
        public int compare(RegisteredChunkyListener i, RegisteredChunkyListener j) {
            int result = i.getPriority().compareTo(j.getPriority());

            if ((result == 0) && (i != j)) {
                result = 1;
            }

            return result;
        }
    };

    public SimpleChunkyModuleManager() {
        registeredCommands = new HashMap<String, ChunkyCommand>();
        registeredModules = new HashSet<JavaPlugin>();
    }

    /**
     * Calls a Chunky event with the given details
     *
     * @param event Event details
     */
    public synchronized void callEvent(ChunkyEvent event) {
        SortedSet<RegisteredChunkyListener> eventListeners = listeners.get(event.getType());

        if (eventListeners != null) {
            for (RegisteredChunkyListener registration : eventListeners) {
                try {
                    registration.callEvent(event);
                } catch (Throwable ex) {
                    Logging.getLog().log(Level.SEVERE, "Could not pass event " + event.getType() + " to "
                            + registration.getPlugin().getDescription().getName(), ex);
                }
            }
        }
    }

    /**
     * Registers the given Chunky event to the specified listener
     *
     * @param type ChunkyEventType to register
     * @param listener ChunkyListener to register
     * @param priority Priority of this event
     * @param plugin Plugin to register
     */
    public void registerEvent(ChunkyEvent.Type type, ChunkyListener listener,
                              ChunkyEvent.Priority priority, Plugin plugin) {
        getEventListeners(type).add(new RegisteredChunkyListener(listener, priority, plugin, type));
    }

    /**
     * Returns a SortedSet of RegisteredChunkyListener for the specified event type creating a new queue if needed
     *
     * @param type ChunkyEventType to lookup
     * @return SortedSet<RegisteredChunkyListener> the looked up or create queue matching the requested type
     */
    private SortedSet<RegisteredChunkyListener> getEventListeners(ChunkyEvent.Type type) {
        SortedSet<RegisteredChunkyListener> eventListeners = listeners.get(type);

        if (eventListeners != null) {
            return eventListeners;
        }

        eventListeners = new TreeSet<RegisteredChunkyListener>(comparer);
        listeners.put(type, eventListeners);
        return eventListeners;
    }

    protected static ChunkyEventExecutor createExecutor(ChunkyEvent.Type type, ChunkyListener listener) {

        switch (type) {

            // Object Events
            case OBJECT_NAME:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyObjectListener) listener).onObjectNameChange((ChunkyObjectNameEvent) event);
                    }
                };
            case OBJECT_SET_OWNER:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyObjectListener) listener).onObjectSetOwner((ChunkyObjectOwnershipEvent) event);
                    }
                };
            /*case OBJECT_ADD_OWNER:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyObjectListener) listener).onObjectAddOwner((ChunkyObjectOwnershipEvent) event);
                    }
                };
            case OBJECT_REMOVE_OWNER:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyObjectListener) listener).onObjectRemoveOwner((ChunkyObjectOwnershipEvent) event);
                    }
                };*/

            // Player Events
            case PLAYER_BUILD:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerUnownedBuild((ChunkyPlayerBuildEvent) event);
                    }
                };
            case PLAYER_DESTROY:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerUnownedBreak((ChunkyPlayerDestroyEvent) event);
                    }
                };
            case PLAYER_CHUNK_CHANGE:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerChunkChange((ChunkyPlayerChunkChangeEvent) event);
                    }
                };

            case PLAYER_ITEM_USE:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerItemUse((ChunkyPlayerItemUseEvent) event);
                    }
                };

            case PLAYER_SWITCH:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerSwitch((ChunkyPlayerSwitchEvent) event);
                    }
                };

            // Command Events
            case COMMAND_PROCESS:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyCommandListener) listener).onCommandProcess((ChunkyCommandEvent) event);
                    }
                };
            case COMMAND_HELP:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyCommandListener) listener).onCommandHelp((ChunkyCommandEvent) event);
                    }
                };
            case COMMAND_LIST:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyCommandListener) listener).onCommandList((ChunkyCommandEvent) event);
                    }
                };
            
            // Custom Events
            case CUSTOM_EVENT:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((CustomChunkyEventListener) listener).onCustomEvent(event);
                    }
                };
        }

        throw new IllegalArgumentException("Event " + type + " is not supported");
    }

    /**
     * Registers a command with Chunky.  Any command you register will automatically be given the "help" and "?" sub-commands.
     *
     * @param command Command to register
     * @return True if the command has not already been registered
     * @throws ChunkyUnregisteredException if the owner command has not been registered
     */
    public boolean registerCommand(ChunkyCommand command) throws ChunkyUnregisteredException {
        if (command.getParent() == null) {
            if (!registeredCommands.containsKey(command.getFullName())) {
                registeredCommands.put(command.getFullName(), command);
                return true;
            }
            return false;
        } else {
            ChunkyCommand parentCommand = registeredCommands.get(command.getParent().getFullName());
            if (parentCommand != null) {
                return parentCommand.addChild(command);
            } else {
                throw new ChunkyUnregisteredException("Parent command not registered!");
            }
        }
    }

    /**
     * Gets all base commands registered.  This will only return commands with a null parent.
     *
     * @return base commands
     */
    public HashMap<String, ChunkyCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    /**
     * Retrieve a ChunkyCommand by looking it up by it's full name.  Example: /chunky.claim.radius
     *
     * @param fullName Full name of command to look up
     * @return The command found or null if none found by specified full name
     */
    public ChunkyCommand getCommandByName(String fullName) {
        String[] commands = fullName.split("\\.");
        String currentName = commands[0];
        ChunkyCommand currentCommand = registeredCommands.get(commands[0]);
        for (int i = 0; i < commands.length; i++) {
            if (currentCommand == null) break;
            
            if (i != 0) {
                currentName += "." + commands[i];
                currentCommand = currentCommand.getChild(currentName);
            }
        }
        return currentCommand;
    }

    /**
     * Retrieves a command by an alias (or the normal name).  You must know the parent command in order to use this.
     *
     * @param parentCommand Parent command of command to look up
     * @param alias Alias of command to look up
     * @return Command found if any or null if none found
     */
    public ChunkyCommand getCommandByAlias(ChunkyCommand parentCommand, String alias) {
        ChunkyCommand command = null;
        if (parentCommand == null) {
            for (Map.Entry<String, ChunkyCommand> registeredCommand : registeredCommands.entrySet()) {
                if (registeredCommand.getValue().getName().equals(alias.toLowerCase())) {
                    return registeredCommand.getValue();
                }
                if (registeredCommand.getValue().getAliases() != null
                            &&registeredCommand.getValue().getAliases().contains(alias.toLowerCase())) {
                    command = registeredCommand.getValue();
                }
            }
        } else {
            for (Map.Entry<String,ChunkyCommand> childCommand : parentCommand.getChildren().entrySet()) {
                if (childCommand.getValue().getName().equals(alias.toLowerCase())) {
                    return childCommand.getValue();
                }
                if (childCommand.getValue().getAliases() != null
                            && childCommand.getValue().getAliases().contains(alias.toLowerCase())) {
                    command = childCommand.getValue();
                }
            }
        }
        return command;
    }

    /**
     * Verifies if a command with given full name is registered.
     *
     * @param fullName Full name of command to check.  Example: /chunky.claim.radius
     * @return true if the command is registered
     */
    public boolean isCommandRegistered(String fullName) {
        return getCommandByName(fullName) != null;
    }

    /**
     * Mostly used internally to parse commands from PlayerCommandPreprocessEvents to see if they should fire a Chunky Command.
     *
     * @param sender Sender of command
     * @param commands Array of words used in command
     */
    public void parseCommand(CommandSender sender, String[] commands) {
        Logging.debug(sender.getName() + " sent " + Arrays.asList(commands));
        ChunkyCommand chunkyCommand = getCommandByAlias(null, commands[0]);
        if (chunkyCommand == null) return;

        int i;
        for (i = 1; i < commands.length; i++) {
            ChunkyCommand currentCommand = getCommandByAlias(chunkyCommand, commands[i]);
            if (currentCommand == null) {
                break;
            }
            chunkyCommand = currentCommand;
        }

        ArrayList<String> argsList = new ArrayList<String>();
        if (commands.length > 1 && i < commands.length) {
            for (i = i; i < commands.length; i++) {
                argsList.add(commands[i]);
            }
        }

        String label = commands[i-1];
        String[] args = argsList.toArray(new String[argsList.size()]);
        if (!argsList.isEmpty()) {
            if (argsList.get(0).equalsIgnoreCase("help")) {
                ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_HELP, sender, chunkyCommand, label, args);
                callEvent(event);
            }
            if (argsList.get(0).equalsIgnoreCase("?")) {
                ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_LIST, sender, chunkyCommand, label, args);
                callEvent(event);
            }
        }

        Logging.debug(sender.getName() + "'s command translated to: " + chunkyCommand.getFullName() + "[" + chunkyCommand.getChatName() + "] with alias: " + label + " and args: " + Arrays.asList(args));
        ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_PROCESS, sender, chunkyCommand, label, args);
        callEvent(event);
        if (!event.isCancelled())  {
            chunkyCommand.getExecutor().onCommand(sender, chunkyCommand, label, args);
        }
    }

    /**
     * Register your module with Chunky.  The main purpose of this method is so your module shows up in lists.
     *
     * @param module Plugin class of your Chunky Module.
     */
    public void registerModule(JavaPlugin module) {
        registeredModules.add(module);
    }

    /**
     * Retrieves all modules registered with Chunky.
     *
     * @return Set of registered Chunky Modules
     */
    public HashSet<JavaPlugin> getRegisteredModules() {
        return registeredModules;
    }
}
