package me.spaceman.psilocin.hooks;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.events.RenderGameOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class PsyGuiIngame extends GuiIngame {

    public PsyGuiIngame(Minecraft mcIn) {
        super(mcIn);
    }

    @Override
    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);
        final RenderGameOverlayEvent event = new RenderGameOverlayEvent(partialTicks, getFontRenderer());
        Psilocin.getInstance().getEventHandler().callEvent(event);
    }
}
