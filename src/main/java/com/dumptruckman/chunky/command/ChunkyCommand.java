package com.dumptruckman.chunky.command;

import org.bukkit.command.CommandExecutor;

import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyCommand {

    private String name;
    private ChunkyCommand parentCommand;
    private List<String> aliases;
    private String description;
    private List<String> helpLines;
    private CommandExecutor executor;

    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, CommandExecutor executor) {
        this(name, aliases, description, helpLines, executor, null);
    }

    public ChunkyCommand(String name, List<String> aliases, String description, List<String> helpLines, CommandExecutor executor, ChunkyCommand parentCommand) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.helpLines = helpLines;
        this.parentCommand = parentCommand;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public ChunkyCommand getParentCommand() {
        return parentCommand;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getHelpLines() {
        return helpLines;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }
}
