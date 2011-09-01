package com.dumptruckman.chunky.module;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.event.ChunkyEvent;
import com.dumptruckman.chunky.event.ChunkyListener;
import com.dumptruckman.chunky.event.CustomChunkyEventListener;
import com.dumptruckman.chunky.event.command.ChunkyCommandEvent;
import com.dumptruckman.chunky.event.command.ChunkyCommandListener;
import com.dumptruckman.chunky.exceptions.ChunkyUnregisteredException;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

/**
 * @author dumptruckman, SwearWord
 */
public class SimpleChunkyModuleManager implements ChunkyModuleManager {

    private Chunky plugin;
    private HashMap<String, ChunkyCommand> registeredCommands;
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

    public SimpleChunkyModuleManager(Chunky plugin) {
        this.plugin = plugin;
        registeredCommands = new HashMap<String, ChunkyCommand>();
    }

    /**
     * Calls an event with the given details
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
     * Registers the given event to the specified listener
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

            // TODO: MAKE EVENTS ACTUALLY WORK LOL

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
     * Registers a command with Chunky
     *
     * @param command Command to register
     * @return True if the command has not already been registered
     * @throws ChunkyUnregisteredException if the parent command has not been registered
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

    public ChunkyCommand getCommandByName(String fullName) {
        String[] commands = fullName.split(".");
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

    public ChunkyCommand getCommandByAlias(ChunkyCommand parentCommand, String alias) {
        if (parentCommand == null) {
            for (Map.Entry<String, ChunkyCommand> registeredCommand : registeredCommands.entrySet()) {
                if (registeredCommand.getValue().getName().equals(alias.toLowerCase())
                        || (registeredCommand.getValue().getAliases() != null
                            &&registeredCommand.getValue().getAliases().contains(alias.toLowerCase()))) {
                    return registeredCommand.getValue();
                }
            }
        } else {
            for (Map.Entry<String,ChunkyCommand> childCommand : parentCommand.getChildren().entrySet()) {
                if (childCommand.getValue().getName().equals(alias.toLowerCase())
                        || (childCommand.getValue().getAliases() != null
                            && childCommand.getValue().getAliases().contains(alias.toLowerCase()))) {
                    return childCommand.getValue();
                }
            }
        }
        return null;
    }

    public boolean isCommandRegistered(String fullName) {
        return getCommandByName(fullName) != null;
    }

    public void parseCommand(CommandSender sender, String[] commands) {
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
                if (!event.isCancelled()) {
                    // TODO add Help event
                }
            }
            if (argsList.get(0).equalsIgnoreCase("?")) {
                ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_LIST, sender, chunkyCommand, label, args);
                callEvent(event);
                if (!event.isCancelled()) {
                    // TODO add command list event
                }
            }
        }


        ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_PROCESS, sender, chunkyCommand, label, args);
        callEvent(event);
        if (!event.isCancelled())  {
            chunkyCommand.getExecutor().onCommand(sender, chunkyCommand, label, args);
        }
    }
}
