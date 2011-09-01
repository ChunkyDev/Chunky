package com.dumptruckman.chunky.module;

import com.dumptruckman.chunky.util.Logging;

import java.util.HashMap;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommand {

    private String name;
    private ChunkyCommand parent;
    private List<String> aliases;
    private String description;
    private List<String> helpLines;
    private ChunkyCommandExecutor executor;

    private String fullName;
    private HashMap<String, ChunkyCommand> children;
    private String chatName;

    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, ChunkyCommandExecutor executor) {
        this(name, aliases, description, helpLines, executor, null);
    }

    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, ChunkyCommandExecutor executor, ChunkyCommand parentCommand) {
        this.name = name.toLowerCase();
        if (aliases != null) {
            for (int i = 0; i < aliases.size(); i++) {
                aliases.set(i, aliases.get(i).toLowerCase());
            }
        }
        this.aliases = aliases;
        this.description = description;
        this.helpLines = helpLines;
        this.parent = parentCommand;
        this.executor = executor;
        this.children = new HashMap<String, ChunkyCommand>();
        this.fullName = name;
        ChunkyCommand currentParent = parentCommand;
        while (true) {
            if (currentParent == null) break;
            this.fullName  = currentParent.getName() + "." + this.fullName;
            currentParent = currentParent.getParent();
        }

        String[] splitName = this.fullName.split(".");
        chatName = "/";
        if (splitName.length != 0) {
            for (int i = 0; i < splitName.length; i++) {
                Logging.debug(splitName[i]);
                if (i != 0) chatName += " ";
                chatName += splitName[i];
            }
        } else {
            chatName += this.fullName;
        }
    }

    public final String getName() {
        return name;
    }

    public final String getFullName() {
        return fullName;
    }

    public final String getChatName() {
        return chatName;
    }

    public final ChunkyCommand getParent() {
        return parent;
    }

    public final HashMap<String, ChunkyCommand> getChildren() {
        @SuppressWarnings("unchecked")
        HashMap<String, ChunkyCommand> children = (HashMap<String, ChunkyCommand>)this.children.clone();
        return children;
    }

    public final List<String> getAliases() {
        return aliases;
    }

    public final String getDescription() {
        return description;
    }

    public final List<String> getHelpLines() {
        return helpLines;
    }

    public final ChunkyCommandExecutor getExecutor() {
        return executor;
    }

    protected final boolean addChild(ChunkyCommand child) {
        if (!children.containsKey(child.getFullName())) {
            children.put(child.getFullName(), child);
            return true;
        }
        return false;
    }

    public final ChunkyCommand getChild(String fullName) {
        return children.get(fullName);
    }

    public final boolean hasChild(String fullName) {
        return getChild(fullName) != null;
    }

    public boolean equals(Object o) {
        return o instanceof ChunkyCommand && ((ChunkyCommand)o).getFullName().equals(this.getFullName());
    }

    public int hashCode() {
        return getFullName().hashCode();
    }
}
