package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.network.Packet;

public class SendPacketEvent extends Event{
    private Packet packet;
    //NetworkManager#sendPacket
    public SendPacketEvent(Packet packet) {
        super(SEND_PACKET_EVENT);
        this.packet = packet;
        this.isCancellable = true;
    }

    public void setPacket(Packet packet)
    {
        this.packet = packet;
    }

    public Packet getPacket()
    {
        return this.packet;
    }
}
