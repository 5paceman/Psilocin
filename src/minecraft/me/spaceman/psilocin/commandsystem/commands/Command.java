package me.spaceman.psilocin.commandsystem.commands;

public class Command {
    private String command;
    private int argumentCount;
    private String argumentRegEx;

    public Command(String command, int argumentCount, String argumentRegEx)
    {
        this.command = command;
        this.argumentCount = argumentCount;
        this.argumentRegEx = argumentRegEx;
    }

    public String getCommand()
    {
        return this.command;
    }

    public int getArgumentCount()
    {
        return this.argumentCount;
    }

    public String getArgumentRegEx()
    {
        return this.argumentRegEx;
    }

}
