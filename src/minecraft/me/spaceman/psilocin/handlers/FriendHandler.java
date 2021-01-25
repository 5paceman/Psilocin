package me.spaceman.psilocin.handlers;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.commandsystem.commands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class FriendHandler {

    private Psilocin psilocin;
    private ArrayList<String> friends = new ArrayList<>();

    public FriendHandler(Psilocin psilocin)
    {
        this.psilocin = psilocin;
        this.psilocin.getCommandHandler().addCommand(new Command("friend", 1, "\\w+.+"), this::handleFriendCommand);
    }

    public void handleFriendCommand(Command command, String[] args)
    {
        String subCommand = args[0];
        if(subCommand.equalsIgnoreCase("list"))
        {
            String friendString = "";
            for(String string : this.friends)
            {
                friendString += string + "\n";
            }
            this.psilocin.log("Friends: " + friendString, Psilocin.Level.INFO);
        } else if(subCommand.equalsIgnoreCase("add"))
        {
            if(args.length > 1)
            {
                this.addFriend(args[1]);
            } else {
                this.psilocin.log("You need to specify a friend.", Psilocin.Level.ERROR);
            }
        } else if(subCommand.equalsIgnoreCase("remove"))
        {
            if(args.length > 1)
            {
                this.removeFriend(args[1]);
            } else {
                this.psilocin.log("You need to specify a friend.", Psilocin.Level.ERROR);
            }
        }

    }

    public void removeFriend(String name)
    {
        for(int i = 0; i < this.friends.size(); i++)
        {
            if(this.friends.get(i).equals(name)) {
                this.friends.remove(i);
                this.psilocin.log("Friend removed: "  + name, Psilocin.Level.INFO);
            }
        }
    }

    public void addFriend(String name)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.theWorld != null)
            this.psilocin.log("Added friend: " + name, Psilocin.Level.INFO);

        this.friends.add(name);
    }

    public boolean isFriend(String name)
    {
        boolean result = false;
        for(String friend : this.friends)
        {
            if(friend.equals(name))
            {
                result = true;
            }
        }
        return result;
    }

    public boolean isFriend(EntityPlayer player)
    {
        return this.isFriend(player.getName());
    }
}
