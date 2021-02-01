package me.spaceman.psilocin.handlers;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.commandsystem.commands.Command;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.KeyPressEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.module.minigame.BedESP;
import me.spaceman.psilocin.module.movement.Safestep;
import me.spaceman.psilocin.module.movement.Sprint;
import me.spaceman.psilocin.module.render.*;
import me.spaceman.psilocin.module.minigame.FireballAura;
import me.spaceman.psilocin.module.world.AutoTool;
import me.spaceman.psilocin.module.world.Fullbright;
import me.spaceman.psilocin.utils.TimeHelper;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleHandler {

    private ArrayList<Module> loadedModules = new ArrayList<>();
    private int longestModName = 0;

    public ModuleHandler(Psilocin psilocin)
    {
        setupMods();
        psilocin.getEventHandler().addEventListener(this);

        psilocin.getCommandHandler().addCommand(new Command("mod", 1, "\\w+"), ((command, args) -> {
            if(args[0].equalsIgnoreCase("list"))
            {
                psilocin.log("Loaded Modules: ", Psilocin.Level.INFO);
                for(Module mod : this.loadedModules)
                {
                    psilocin.log("- " + mod.getName() + "- Keybind: " + Keyboard.getKeyName(mod.getKeyCode()), Psilocin.Level.INFO);
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                TimeHelper timeHelper = new TimeHelper();
                try {
                    timeHelper.start();
                    for(Module mod : this.loadedModules)
                    {
                        psilocin.getEventHandler().removeEventListener(mod);
                    }
                    this.loadedModules.clear();
                    setupMods();
                    timeHelper.stop();
                    psilocin.log("Reloaded mods in " + timeHelper.getTimedTimeInMilliseconds() + "ms.", Psilocin.Level.INFO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        psilocin.getCommandHandler().addCommand(new Command("keybind", 2, "\\w+ c"), ((command, args) -> {
            if(args.length > 1)
            {
                String modName = args[1];
                String key = args[2];
                for(Module mod : this.loadedModules)
                {
                    if(modName.equalsIgnoreCase(mod.getName()))
                    {
                        mod.setKeybind(Keyboard.getKeyIndex(key));
                        psilocin.log("Set " + key + " as keybind for " + modName, Psilocin.Level.INFO);
                        break;
                    }
                }
            }
        }));
    }

    @EventSubscriber
    public void onKeyPress(final KeyPressEvent event)
    {
        for(Module mod : this.loadedModules)
        {
            if(mod.getKeyCode() == event.getKeyCode() && event.getKeyState())
            {
                mod.toggle();
            }
        }
    }

    public int getLongestModName()
    {
        return this.longestModName;
    }

    public Module getModule(Class moduleClass)
    {
        for(Module module : this.loadedModules)
        {
            if(module.getClass() == moduleClass)
                return module;
        }

        return null;
    }

    public ArrayList<Module> getLoadedModules()
    {
        return this.loadedModules;
    }

    private void sort()
    {
        ArrayList<Module> sortedModules = new ArrayList<>();
        int size = this.loadedModules.size();
        while(sortedModules.size() != size)
        {
            int longestString = 0;
            Module longestMod = null;
            for(Module mod : this.loadedModules) {
                if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(mod.getName()) > longestString) {
                    longestString = Minecraft.getMinecraft().fontRendererObj.getStringWidth(mod.getName());
                    longestMod = mod;
                }
            }

            if(longestString > this.longestModName)
                this.longestModName = longestString;

            sortedModules.add(longestMod);
            this.loadedModules.remove(longestMod);
        }

        this.loadedModules = sortedModules;
    }

    private void addModule(Module mod)
    {
        this.loadedModules.add(mod);
        this.sort();
    }

    private void setupMods()
    {
        addModule(new Fullbright());
        addModule(new Safestep());
        addModule(new Wallhacks());
        addModule(new Tracers());
        addModule(new Nametags());
        addModule(new FireballAura());
        addModule(new Trajectory());
        addModule(new BedESP());
        addModule(new Sprint());
        addModule(new AutoTool());
    }

}
