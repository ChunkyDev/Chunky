package com.dumptruckman.chunky.module;

import org.bukkit.command.CommandExecutor;

import java.util.HashSet;
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
    private CommandExecutor executor;

    private String fullName;
    private HashSet<ChunkyCommand> children;

    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, CommandExecutor executor) {
        this(name, aliases, description, helpLines, executor, null);
    }

    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, CommandExecutor executor, ChunkyCommand parentCommand) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.helpLines = helpLines;
        this.parent = parentCommand;
        this.executor = executor;
        this.children = new HashSet<ChunkyCommand>();
        this.fullName = name;
        ChunkyCommand currentParent = parentCommand;
        while (true) {
            if (currentParent == null) break;
            this.fullName  = currentParent.getName() + "." + this.fullName;
            currentParent = currentParent.getParent();
        }
    }

    public final String getName() {
        return name;
    }

    public final String getFullName() {
        return fullName;
    }

    public final ChunkyCommand getParent() {
        return parent;
    }

    public final HashSet<ChunkyCommand> getChildren() {
        @SuppressWarnings("unchecked")
        HashSet<ChunkyCommand> children = (HashSet<ChunkyCommand>)this.children.clone();
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

    public final CommandExecutor getExecutor() {
        return executor;
    }

    protected final void addChild(ChunkyCommand child) {
        children.add(child);
    }

    public boolean equals(Object o) {
        return o instanceof ChunkyCommand && ((ChunkyCommand)o).getFullName().equals(this.getFullName());
    }

    public int hashCode() {
        return getFullName().hashCode();
    }
}
