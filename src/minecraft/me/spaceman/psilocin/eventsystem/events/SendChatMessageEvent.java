package me.spaceman.psilocin.eventsystem.events;

public class SendChatMessageEvent extends Event{

    private String message;
    // EntityPlayerSP#sendChatMessage
    public SendChatMessageEvent(String msg) {
        super(SEND_CHAT_MESSAGE);
        this.message = msg;
        this.isCancellable = true;
    }

    public String getMessage()
    {
        return this.message;
    }
}
