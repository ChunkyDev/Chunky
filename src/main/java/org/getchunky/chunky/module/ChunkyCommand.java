package org.getchunky.chunky.module;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.exceptions.ChunkyUnregisteredException;

import java.util.*;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommand {

    private String name;
    private ChunkyCommand parent = null;
    private HashSet<String> aliases = new HashSet<String>();
    private String description = null;
    private ArrayList<String> helpLines = new ArrayList<String>();
    private ChunkyCommandExecutor executor;

    private String fullName;
    private HashMap<String, ChunkyCommand> children = new HashMap<String, ChunkyCommand>();
    private String chatName;

     /**
     * Creates a Command that is registrable with Chunky.
     *
     * @param name          the name of the command. Example: "chunky" creates command /chunky
     * @param executor      the class that will contain the onCommand() for this command
     */
    public ChunkyCommand(String name, ChunkyCommandExecutor executor, ChunkyCommand parentCommand) {
        this.name = name.toLowerCase();
        this.executor = executor;
        this.parent = parentCommand;

        this.fullName = name;

        ChunkyCommand currentParent = parentCommand;
        while (true) {
            if (currentParent == null) break;
            this.fullName = currentParent.getName() + "." + this.fullName;
            currentParent = currentParent.getParent();
        }

        String[] splitName = this.fullName.split("\\.");
        chatName = "/";
        for (int i = 0; i < splitName.length; i++) {
            if (i != 0) chatName += " ";
            chatName += splitName[i];
        }
    }

    /**
     * Retrieves the name of this command.  This is only the individual command/sub command string.  It will not contain the names of any owner commands.
     *
     * @return the name of this command
     */
    public final String getName() {
        return name;
    }

    /**
     * Retrieves the "full name" of this command.  The full name includes the owner command chain separated by periods.  This looks something like chunky.command.radius
     *
     * @return the full name of this command
     */
    public final String getFullName() {
        return fullName;
    }

    /**
     * Retrieves the name of this command as the way you would type it.  Example: /chunky claim radius
     *
     * @return the chat name of this command
     */
    public final String getChatName() {
        return chatName;
    }

    /**
     * Retrieves the owner command that this command is a sub-command of.
     *
     * @return the owner command of this command or null if this is a top level command
     */
    public final ChunkyCommand getParent() {
        return parent;
    }

    /**
     * Retrieves any sub-commands registered to this command.
     *
     * @return all sub-commands of this command
     */
    public final HashMap<String, ChunkyCommand> getChildren() {
        return children;
    }

    public final String getAliasesAsString() {
        String aliases = "";
        for (String alias : this.aliases) {
            if (!aliases.isEmpty()) aliases += ", ";
            aliases += alias;
        }
        return aliases;
    }

    /**
     * Sets aliases for this command.  If you put a slash before the alias, it will indicate you wish it to act as a top level command.  This means it would be /youralias instead of /chunky youralias, for example.
     *
     * @param aliases any aliases you wish to register for the command
     * @return this command
     */
    public final ChunkyCommand setAliases(List<String> aliases) {
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        return this;
    }

    /**
     * Sets aliases for this command.  If you put a slash before the alias, it will indicate you wish it to act as a top level command.  This means it would be /youralias instead of /chunky youralias, for example.
     *
     * @param aliases any aliases you wish to register for the command
     * @return this command
     */
    public final ChunkyCommand setAliases(String...aliases) {
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        return this;
    }

    /**
     * Retrieves the aliases for this command, if any
     *
     * @return aliases of this command or null if no aliases
     */
    public final HashSet<String> getAliases() {
        return aliases;
    }

    /**
     * Sets the description for this command
     *
     * @param description the description that Chunky will show for this command
     * @return this command
     */
    public final ChunkyCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the description of this command that chunky will display with command lists.
     *
     * @return the description of this command
     */
    public final String getDescription() {
        if (description == null) return "NULL";
        return description;
    }

    /**
     * Sets the help information for this command.  This is always viewable by /%cmd% help
     *
     * @param helpLines a list of helpful information related to the command
     * @return this command
     */
    public final ChunkyCommand setHelpLines(List<String> helpLines) {
        this.helpLines.addAll(helpLines);
        return this;
    }

    /**
     * Sets the help information for this command.  This is always viewable by /%cmd% help
     *
     * @param helpLines a list of helpful information related to the command
     * @return this command
     */
    public final ChunkyCommand setHelpLines(String...helpLines) {
        this.helpLines.addAll(Arrays.asList(helpLines));
        return this;
    }

    /**
     * Gets all the lines of helpful information for this command.
     *
     * @return all of the help info for this command
     */
    public final List<String> getHelpLines() {
        return helpLines;
    }

    /**
     * Retrieves the class responsible for executing this command.
     *
     * @return the class responsible for executing this command
     */
    public final ChunkyCommandExecutor getExecutor() {
        return executor;
    }

    /**
     * Adds a child command to this command.  This method is generally the responsibility of the Chunky API.
     *
     * @param child sub-command to add
     * @return true if the sub command did not already exist
     */
    protected final boolean addChild(ChunkyCommand child) {
        if (!children.containsKey(child.getFullName())) {
            children.put(child.getFullName(), child);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a child command with the specific full name.
     *
     * @param fullName full name of child command
     * @return child command if registered to this command or null if not registered
     */
    public final ChunkyCommand getChild(String fullName) {
        return children.get(fullName);
    }

    /**
     * Checks whether this command already contains a specific child command.
     *
     * @param fullName the full name of the child command to hasPerm for
     * @return true if the child command is registered to this command
     */
    public final boolean hasChild(String fullName) {
        return getChild(fullName) != null;
    }

    public boolean equals(Object o) {
        return o instanceof ChunkyCommand && ((ChunkyCommand) o).getFullName().equals(this.getFullName());
    }

    public int hashCode() {
        return getFullName().hashCode();
    }

    public final ChunkyCommand register() throws ChunkyUnregisteredException {
        Chunky.getModuleManager().registerCommand(this);
        return this;
    }
}
