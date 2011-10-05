package org.getchunky.chunky.module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommand {

    private String name;
    private ChunkyCommand parent;
    private HashSet<String> aliases;
    private String description;
    private List<String> helpLines;
    private ChunkyCommandExecutor executor;

    private String fullName;
    private HashMap<String, ChunkyCommand> children;
    private String chatName;

    /**
     * Creates a Command that is registrable with Chunky.
     *
     * @param name        the name of the command. Example: "chunky" creates command /chunky
     * @param aliases     any aliases you wish to register for the command
     * @param description the description that Chunky will show for this command
     * @param helpLines   a list of helpful information related to the command
     * @param executor    the class that will contain the onCommand() for this command
     */
    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, ChunkyCommandExecutor executor) {
        this(name, aliases, description, helpLines, executor, null);
    }

    /**
     * Creates a Command that is registrable with Chunky.
     *
     * @param name          the name of the command. Example: "chunky" creates command /chunky
     * @param aliases       any aliases you wish to register for the command
     * @param description   the description that Chunky will show for this command
     * @param helpLines     a list of helpful information related to the command
     * @param executor      the class that will contain the onCommand() for this command
     * @param parentCommand the command to register this command as a subcommand of. Example, if you registered a command with name "claim" to an already register command "chunky" you will end up with /chunky claim
     */
    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, ChunkyCommandExecutor executor, ChunkyCommand parentCommand) {
        this.name = name.toLowerCase();
        this.aliases = new HashSet<String>();
        if (aliases != null) {
            for (int i = 0; i < aliases.size(); i++) {
                this.aliases.add(aliases.get(i).toLowerCase());
            }
        }
        this.description = description;
        this.helpLines = helpLines;
        this.parent = parentCommand;
        this.executor = executor;
        this.children = new HashMap<String, ChunkyCommand>();
        this.fullName = name;
        ChunkyCommand currentParent = parentCommand;
        while (true) {
            if (currentParent == null) break;
            this.fullName = currentParent.getName() + "." + this.fullName;
            currentParent = currentParent.getParent();
        }

        String[] splitName = this.fullName.split("\\.");
        chatName = "/";
        //if (splitName.length != 0) {
        for (int i = 0; i < splitName.length; i++) {
            if (i != 0) chatName += " ";
            chatName += splitName[i];
        }
        //} else {
        //    chatName += this.fullName;
        //}
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
     * Retrieves the aliases for this command, if any.
     *
     * @return aliases of this command or null if no aliases
     */
    public final HashSet<String> getAliases() {
        return aliases;
    }

    /**
     * Gets the description of this command that chunky will display with command lists.
     *
     * @return the description of this command
     */
    public final String getDescription() {
        return description;
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
}
