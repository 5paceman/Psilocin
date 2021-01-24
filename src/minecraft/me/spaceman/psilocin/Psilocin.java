package me.spaceman.psilocin;

import me.spaceman.psilocin.eventsystem.EventHandler;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.RenderGameOverlayEvent;
import me.spaceman.psilocin.eventsystem.events.RenderMainMenuEvent;
import me.spaceman.psilocin.handlers.ModuleHandler;
import me.spaceman.psilocin.module.Module;
import me.spaceman.psilocin.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

public class Psilocin {

    private static Psilocin instance;

    private ModuleHandler moduleHandler;
    private EventHandler eventHandler;
    private String name;

    public Psilocin() {
        this.name =
                EnumChatFormatting.BLUE + "P"
                        + EnumChatFormatting.GRAY + "s"
                        + EnumChatFormatting.GREEN  + "i"
                        + EnumChatFormatting.RED + "l"
                        + EnumChatFormatting.OBFUSCATED + "o"
                        + EnumChatFormatting.GOLD + "c"
                        + EnumChatFormatting.LIGHT_PURPLE + "i"
                        + EnumChatFormatting.GREEN + "n";
    }

    public void setup()
    {
        this.eventHandler = new EventHandler();
        this.moduleHandler = new ModuleHandler(this);
        getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onRenderMainMenu(final RenderMainMenuEvent event)
    {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        event.fontRenderer.drawString( name, 2, scaledResolution.getScaledHeight() - 10 - event.fontRenderer.FONT_HEIGHT - event.fontRenderer.FONT_HEIGHT - 4, -1);
        event.fontRenderer.drawString( "Loaded Modules: " + Psilocin.getInstance().getModuleHandler().getLoadedModules().size(), 2, scaledResolution.getScaledHeight() - 10 - event.fontRenderer.FONT_HEIGHT - 2, -1);
    }

    @EventSubscriber
    public void onRenderOverlay(final RenderGameOverlayEvent event)
    {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        event.fontRenderer.drawStringWithShadow(name, 2.f, 2.f, 0xFFFFFFFF);
        Tessellator tessellator = Tessellator.getInstance();
        int y = 2;
        for(Module mod : this.getModuleHandler().getLoadedModules())
        {
            if(mod.isEnabled())
            {
                RenderUtils.drawRect(scaledResolution.getScaledWidth() - Psilocin.getInstance().getModuleHandler().getLongestModName() - 4, y - 2, scaledResolution.getScaledWidth(), y + event.fontRenderer.FONT_HEIGHT, 0x55111111);
                event.fontRenderer.drawStringWithShadow(mod.getName(), scaledResolution.getScaledWidth() - event.fontRenderer.getStringWidth(mod.getName()) - 2f, y, mod.getColor());
                y += event.fontRenderer.FONT_HEIGHT + 2f;
            }
        }
    }

    public static Psilocin getInstance()
    {
        if(Psilocin.instance == null)
            Psilocin.instance = new Psilocin();

        return Psilocin.instance;
    }

    public ModuleHandler getModuleHandler()
    {
        return this.moduleHandler;
    }

    public EventHandler getEventHandler()
    {
        return this.eventHandler;
    }
}
