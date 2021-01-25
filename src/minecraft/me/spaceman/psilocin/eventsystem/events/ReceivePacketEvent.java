package me.spaceman.psilocin.eventsystem.events;

import net.minecraft.network.Packet;

public class ReceivePacketEvent extends Event{
    private Packet packet;
    //NetworkManager#channelReceive0
    public ReceivePacketEvent(Packet packet) {
        super(RECEIVE_PACKET_EVENT);
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
