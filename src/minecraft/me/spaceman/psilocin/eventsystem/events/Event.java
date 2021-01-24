package me.spaceman.psilocin.eventsystem.events;

public class Event {

    /* This is just for debugging purposes this isnt needed at all you can always use a value of 0 when
     * declaring variables if you wish */
    public static final int BASIC_EVENT = 0;
    public static final int RENDER_GAME_OVERLAY_EVENT = 1;
    public static final int KEY_PRESS_EVENT = 2;
    public static final int RENDER_PLAYER_EVENT = 3;
    public static final int RENDER_DROPPED_ITEM = 4;
    public static final int RENDER_WORLD_EVENT = 5;
    public static final int RENDER_NAMETAGS = 6;

    private int eventId;

    protected boolean isCancellable = false;
    private boolean cancelled = false;

    public Event(int id)
    {
        this.eventId = id;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    /**
     * Returns the event ID associated with this event
     */
    public int getEventID()
    {
        return this.eventId;
    }
}
