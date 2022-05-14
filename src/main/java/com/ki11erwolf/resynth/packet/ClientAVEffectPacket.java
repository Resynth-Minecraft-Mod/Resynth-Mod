/*
 * Copyright (c) 2018 - 2021 Ki11er_wolf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ki11erwolf.resynth.packet;

import com.google.gson.JsonSyntaxException;
import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.util.EffectsUtil;
import com.ki11erwolf.resynth.util.JSerializer;
import com.ki11erwolf.resynth.util.SideUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Display Client-side Audio Visual Effect.
 */
//TODO: Document
public class ClientAVEffectPacket extends Packet<ClientAVEffectPacket>
        implements JSerializer.JSerializable<ClientAVEffectPacket>{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    private final AVEffect audioVisualEffect;

    private final BlockPos position;

    public ClientAVEffectPacket(AVEffect audioVisualEffect, BlockPos position) {
        this.audioVisualEffect = Objects.requireNonNull(audioVisualEffect);
        this.position = Objects.requireNonNull(position);
    }

    @Override
    BiConsumer<ClientAVEffectPacket, PacketBuffer> getEncoder() {
        return ClientAVEffectPacket::encode;
    }

    private static void encode(ClientAVEffectPacket effectPacket, PacketBuffer packetBuffer) {
        try{
            writeString(JSerializer.serialize(effectPacket).getDataAsJsonString(), packetBuffer);
        } catch (JSerializer.SerializeException exception) {
            LOG.error("Failed to serialize and encode ClientAudioVisualEffectPacket!", exception);
        }
    }

    @Override
    Function<PacketBuffer, ClientAVEffectPacket> getDecoder() {
        return ClientAVEffectPacket::decode;
    }

    private static ClientAVEffectPacket decode(PacketBuffer packetBuffer) {
        try {
            JSerializer.JSerialData packetSerialData = JSerializer.JSerialData.fromJsonString(readString(packetBuffer));
            ClientAVEffectPacket deserializedPacket = JSerializer.deserialize(packetSerialData, PacketSerializer.SERIALIZER);

            if(deserializedPacket == null || deserializedPacket.audioVisualEffect == AVEffect.NONE) {
                throw new IllegalArgumentException("Deserialized ClientAudioVisualEffectPacket is null or has no effect.");
            }

            return deserializedPacket;
        } catch (IllegalArgumentException | JsonSyntaxException | JSerializer.SerializeException exception) {
            LOG.error("Failed to deserialize and decode ClientAudioVisualEffectPacket!", exception);
            return new ClientAVEffectPacket(AVEffect.NONE, new BlockPos(0, 0, 0));
        }
    }

    @Override
    BiConsumer<ClientAVEffectPacket, Supplier<NetworkEvent.Context>> getHandler() {
        return ClientAVEffectPacket::handle;
    }

    private static void handle(ClientAVEffectPacket effectPacket, Supplier<NetworkEvent.Context> supplier) {
        Packet.handle(supplier, () -> {
            if(!SideUtil.isClientTrueSafe()) {
                LOG.error("Attempted to handle ClientAudioVisualEffect on server! Skipping...");
                return;
            } else if (effectPacket.audioVisualEffect == AVEffect.NONE){
                LOG.error("ClientAudioVisualEffect: " + AVEffect.NONE.getSerialID() + " requested.");
                return;
            }

            LOG.debug("Playing ClientAudioVisualEffect: " + effectPacket.audioVisualEffect.getSerialID());
            effectPacket.audioVisualEffect.play(effectPacket.position);
        });
    }

    @Override
    public PacketSerializer getSerializer() {
        return PacketSerializer.SERIALIZER;
    }

    public enum AVEffect {

        NONE("none"),

        SEEDS_SPAWNED("seeds_spawned") {

            @Override
            protected void play(BlockPos pos) {
                if(Minecraft.getInstance().player == null) {
                    LOG.error("Cannot play AudioVisualEffect! Client Player instance is null.");
                    return;
                }

                //BlockPos playerPos = Minecraft.getInstance().player.getPosition();

                EffectsUtil.displayStandardEffectsOnClient(pos, 10, ParticleTypes.FLASH);
                EffectsUtil.playNormalSoundWithVolumeOnClient(
                        pos, SoundEvents.NOTE_BLOCK_BELL, SoundCategory.PLAYERS, 8.0F
                );
            }
        };

        private final String serialID;

        AVEffect(String serialID) {
            this.serialID = Objects.requireNonNull(serialID);
        }

        protected String getSerialID() {
            return serialID;
        }

        protected void play(BlockPos pos) {
            throw new UnsupportedOperationException();
        }

        protected static AVEffect fromSerialID(String serialID) {
            for (AVEffect effect : AVEffect.values()) {
                if (effect.getSerialID().equals(serialID))
                    return effect;
            }

            return null;
        }
    }

    private static class PacketSerializer extends JSerializer<ClientAVEffectPacket> {

        private static final PacketSerializer SERIALIZER = new PacketSerializer("client-effect-packet");

        public PacketSerializer(String identification) {
            super(identification);
        }

        @Override
        protected void objectToData(ClientAVEffectPacket object, JSerialDataIO dataIO) throws Exception {
            dataIO.add("effect-id", object.audioVisualEffect.getSerialID());
            dataIO.add("pos-x", object.position.getX());
            dataIO.add("pos-y", object.position.getY());
            dataIO.add("pos-z", object.position.getZ());
        }

        @Override
        protected ClientAVEffectPacket dataToObject(ClientAVEffectPacket suggestedObject, JSerialDataIO dataIO) throws Exception {
            return new ClientAVEffectPacket(
                    AVEffect.fromSerialID(dataIO.getString("effect-id")),
                    new BlockPos(
                            dataIO.getInteger("pos-x"),
                            dataIO.getInteger("pos-y"),
                            dataIO.getInteger("pos-z")
                    )
            );
        }

        @Override
        protected ClientAVEffectPacket createInstance() {
            return new ClientAVEffectPacket(AVEffect.NONE, new BlockPos(0, 0, 0));
        }
    }
}
