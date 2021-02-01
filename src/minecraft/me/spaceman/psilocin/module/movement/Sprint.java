package me.spaceman.psilocin.module.movement;

import me.spaceman.psilocin.Psilocin;
import me.spaceman.psilocin.eventsystem.EventSubscriber;
import me.spaceman.psilocin.eventsystem.events.PlayerLivingUpdateEvent;
import me.spaceman.psilocin.module.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", Keyboard.KEY_H, Category.MOVEMENT, 0xFF00ff59);
        Psilocin.getInstance().getEventHandler().addEventListener(this);
    }

    @EventSubscriber
    public void onPlayerUpdate(final PlayerLivingUpdateEvent event)
    {
        if(!this.isEnabled())
            return;
        if(event.getPlayer().onGround)
            event.getPlayer().setSprinting(true);
    }
}
