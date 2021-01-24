package me.spaceman.psilocin.handlers;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.KeyPressEvent;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.module.movement.Safestep;
import me.spaceman.psilocin.module.render.Nametags;
import me.spaceman.psilocin.module.render.Tracers;
import me.spaceman.psilocin.module.render.Wallhacks;
import me.spaceman.psilocin.module.world.Fullbright;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class ModuleHandler {

    private ArrayList<Module> loadedModules = new ArrayList<>();
    private int longestModName = 0;

    public ModuleHandler(Psilocin psilocin)
    {
        setupMods();
        psilocin.getEventHandler().addEventListener(this);
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
    }

}
