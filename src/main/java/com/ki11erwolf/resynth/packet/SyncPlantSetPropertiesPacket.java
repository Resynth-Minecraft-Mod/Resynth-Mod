package com.ki11erwolf.resynth.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncPlantSetPropertiesPacket extends Packet<SyncPlantSetPropertiesPacket> {

    @Override
    BiConsumer<SyncPlantSetPropertiesPacket, PacketBuffer> getEncoder() {
        return (propertiesPacket, packetBuffer) -> {

        };
    }

    @Override
    Function<PacketBuffer, SyncPlantSetPropertiesPacket> getDecoder() {
        return (packetBuffer) -> new SyncPlantSetPropertiesPacket();
    }

    @Override
    BiConsumer<SyncPlantSetPropertiesPacket, Supplier<NetworkEvent.Context>> getHandler() {
        return (propertiesPacket, supplier) -> Packet.handle(supplier, () -> {

        });
    }
}
