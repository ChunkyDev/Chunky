package org.getchunky.chunky.module;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.ChunkyEvent;
import org.getchunky.chunky.event.ChunkyListener;
import org.getchunky.chunky.event.CustomChunkyEventListener;
import org.getchunky.chunky.event.command.ChunkyCommandEvent;
import org.getchunky.chunky.event.command.ChunkyCommandListener;
import org.getchunky.chunky.event.object.ChunkyObjectListener;
import org.getchunky.chunky.event.object.ChunkyObjectNameEvent;
import org.getchunky.chunky.event.object.ChunkyObjectOwnershipEvent;
import org.getchunky.chunky.event.object.player.*;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.util.Logging;

import java.util.*;
import java.util.logging.Level;

/**
 * @author dumptruckman, SwearWord
 */
public class SimpleChunkyModuleManager implements ChunkyModuleManager {

    private HashMap<String, ChunkyCommand> registeredCommands;
    private HashMap<String, ChunkyCommand> superAliases;
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
        superAliases = new HashMap<String, ChunkyCommand>();
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
     * @param type     ChunkyEventType to register
     * @param listener ChunkyListener to register
     * @param priority Priority of this event
     * @param plugin   Plugin to register
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

            case PLAYER_CHUNK_CLAIM:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerChunkClaim((ChunkyPlayerChunkClaimEvent) event);
                    }
                };

            case PLAYER_CHUNK_UNCLAIM:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerChunkUnclaim((ChunkyPlayerChunkUnclaimEvent) event);
                    }
                };

            case PLAYER_CLAIM_LIMIT_QUERY:
                return new ChunkyEventExecutor() {
                    public void execute(ChunkyListener listener, ChunkyEvent event) {
                        ((ChunkyPlayerListener) listener).onPlayerClaimLimitQuery((ChunkyPlayerClaimLimitQueryEvent) event);
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
                registerSuperAliases(command);
                return true;
            }
            return false;
        } else {
            ChunkyCommand parentCommand = Chunky.getModuleManager().getCommandByName(command.getParent().getFullName());
            if (command.inheritsPermission() && command.getPermission() == null) {
                command.setPermission(parentCommand.getPermission());
            }
            if (parentCommand != null) {
                registerSuperAliases(command);
                return parentCommand.addChild(command);
            } else {
                throw new ChunkyUnregisteredException("Parent command not registered!");
            }
        }
    }

    private void registerSuperAliases(ChunkyCommand command) {
        for (String alias : command.getAliases()) {
            if (alias.startsWith("/"))
                superAliases.put(alias.toLowerCase(), command);
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
     * @param alias         Alias of command to look up
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
                        && registeredCommand.getValue().getAliases().contains(alias.toLowerCase())) {
                    command = registeredCommand.getValue();
                }
            }
        } else {
            for (Map.Entry<String, ChunkyCommand> childCommand : parentCommand.getChildren().entrySet()) {
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
     * @param fullName Full name of command to hasPerm.  Example: /chunky.claim.radius
     * @return true if the command is registered
     */
    public boolean isCommandRegistered(String fullName) {
        return getCommandByName(fullName) != null;
    }

    private String getCommandBySuperAlias(ChunkyCommand command, String commandString) {
        for (Map.Entry<String, ChunkyCommand> superAlias : superAliases.entrySet()) {
            if (commandString.toLowerCase().startsWith(superAlias.getKey())) {
                command = superAlias.getValue();
                return superAlias.getKey();
            }
        }
        return null;
    }

    /**
     * Mostly used internally to parse commands from PlayerCommandPreprocessEvents to see if they should fire a Chunky Command.
     *
     * @param sender   Sender of command
     * @param commands Array of words used in command
     */
    public void parseCommand(CommandSender sender, String[] commands) {
        Logging.debug(sender + " sent " + Arrays.asList(commands));

        String label = null;
        List<String> argsList = new ArrayList<String>();

        ChunkyCommand chunkyCommand = getCommandByAlias(null, commands[0].substring(1));
        if (chunkyCommand == null) {
            String commandString = Language.combineStringArray(commands, " ");
            label = getCommandBySuperAlias(chunkyCommand, commandString);
            if (chunkyCommand == null) return;
            argsList.addAll(Arrays.asList(commandString.substring(label.length()).trim().split("//s")));
        } else {
            int i;
            for (i = 1; i < commands.length; i++) {
                ChunkyCommand currentCommand = getCommandByAlias(chunkyCommand, commands[i]);
                if (currentCommand == null) {
                    break;
                }
                chunkyCommand = currentCommand;
            }

            if (commands.length > 1 && i < commands.length) {
                for (i = i; i < commands.length; i++) {
                    argsList.add(commands[i]);
                }
            }
            label = commands[i - 1];
        }

        String[] args = argsList.toArray(new String[argsList.size()]);
        if (!argsList.isEmpty()) {
            if (argsList.get(0).equalsIgnoreCase("help")) {
                ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_HELP, sender, chunkyCommand, label, args);
                callEvent(event);
                return;
            }
            if (argsList.get(0).equalsIgnoreCase("?")) {
                ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_LIST, sender, chunkyCommand, label, args);
                callEvent(event);
                return;
            }
        }

        if (chunkyCommand.isCombiningQuotedArgs()) {
            int quotedAt = -1;
            List<String> tempArgs = new ArrayList<String>();
            argsList.clear();
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("\\\"")) {
                    args[i] = args[i].substring(1);
                } else {
                    if (quotedAt != -1) {
                        if (args[i].startsWith("\"") && args[i].length() > 1) {
                            quotedAt = i;
                            tempArgs.add(args[i].substring(1));
                        } else
                            argsList.add(args[i]);
                    } else {
                        if (args[i].endsWith("\"")) {
                            tempArgs.add(args[i].substring(0, args[i].length() - 1));
                            argsList.add(Language.combineStringList(tempArgs, " "));
                            tempArgs.clear();
                            quotedAt = -1;
                        }
                    }
                }
            }
            if (!tempArgs.isEmpty()) {
                tempArgs.set(0, "\"" + tempArgs.get(0));
                argsList.addAll(tempArgs);
            }
        }

        Logging.debug(sender + "'s command translated to: " + chunkyCommand.getFullName() + "[" + chunkyCommand.getChatName() + "] with alias: " + label + " and args: " + Arrays.asList(args));
        ChunkyCommandEvent event = new ChunkyCommandEvent(ChunkyEvent.Type.COMMAND_PROCESS, sender, chunkyCommand, label, args);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (chunkyCommand.isInGameOnly() && !ChunkyManager.getChunkyWorld(player.getWorld().getName()).isEnabled()) {
                Language.WORLD_DISABLED.bad(sender);
                event.setCancelled(true);
            } else if (chunkyCommand.getPermission() != null && !player.hasPermission(chunkyCommand.getPermission())) {
                Language.NO_COMMAND_PERMISSION.bad(sender);
                event.setCancelled(true);
            }
        } else {
            if (chunkyCommand.isInGameOnly()) {
                Language.IN_GAME_ONLY.bad(sender);
                event.setCancelled(true);
            }
        }

        callEvent(event);
        if (!event.isCancelled()) {
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
