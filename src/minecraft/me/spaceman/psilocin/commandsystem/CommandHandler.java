package me.spaceman.psilocin.commandsystem;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.commandsystem.commands.Command;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.SendChatMessageEvent;
import net.minecraft.client.Minecraft;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CommandHandler {

    private Psilocin psilocin;
    public static String COMMAND_PREFIX = ".";
    private HashMap<Command, BiConsumer<Command, String[]>> loadedCommands = new HashMap<>();


    public CommandHandler(Psilocin psilocin)
    {
        psilocin.getEventHandler().addEventListener(this);
        this.psilocin = psilocin;
    }

    public void addCommand(Command command, BiConsumer<Command, String[]> callbackFunction)
    {
        this.loadedCommands.put(command, callbackFunction);
    }

    @EventSubscriber
    public void onChatMessage(final SendChatMessageEvent event)
    {
        if(event.getMessage().startsWith(COMMAND_PREFIX))
        {
            event.setCancelled(true);
            String[] splitMessage = event.getMessage().replaceFirst(".", "").split(" ");
            for(Command command : this.loadedCommands.keySet())
            {
                if(command.getCommand().equalsIgnoreCase(splitMessage[0]))
                {
                    if(splitMessage.length - 1 == command.getArgumentCount()) {
                        if (command.getArgumentCount() > 0) {
                            String args = "";
                            for (int i = 1; i < splitMessage.length; i++) {
                                args += splitMessage[0 + i] + " ";
                            }
                            args = args.trim();
                            this.psilocin.log("RegEx: " + command.getArgumentRegEx() + " Args: " + args, Psilocin.Level.INFO);
                            if (args.matches(command.getArgumentRegEx())) {

                                this.loadedCommands.get(command).accept(command, args.split(" "));
                                return;
                            } else {
                                this.psilocin.log("Arguments are incorrect format.", Psilocin.Level.ERROR);
                                return;
                            }
                        } else {
                            this.loadedCommands.get(command).accept(command, null);
                            return;
                        }
                    } else {
                        this.psilocin.log("Incorrect amount of arguments.", Psilocin.Level.ERROR);
                        return;
                    }
                }
            }

            this.psilocin.log("Unknown Command.", Psilocin.Level.ERROR);
        }
    }

}
