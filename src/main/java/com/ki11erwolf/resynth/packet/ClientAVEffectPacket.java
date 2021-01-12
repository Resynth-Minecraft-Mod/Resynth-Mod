package com.ki11erwolf.resynth.packet;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.util.EffectsUtil;
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
public class ClientAVEffectPacket extends Packet<ClientAVEffectPacket>{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    private final AVEffect audioVisualEffect;

    public ClientAVEffectPacket(AVEffect audioVisualEffect) {
        this.audioVisualEffect = Objects.requireNonNull(audioVisualEffect);
    }

    @Override
    BiConsumer<ClientAVEffectPacket, PacketBuffer> getEncoder() {
        return ClientAVEffectPacket::encode;
    }

    private static void encode(ClientAVEffectPacket effectPacket, PacketBuffer packetBuffer) {
        writeString(effectPacket.audioVisualEffect.getSerialID(), packetBuffer);
    }

    @Override
    Function<PacketBuffer, ClientAVEffectPacket> getDecoder() {
        return ClientAVEffectPacket::decode;
    }

    private static ClientAVEffectPacket decode(PacketBuffer packetBuffer) {
        AVEffect effect = AVEffect.fromSerialID(readString(packetBuffer));

        if(effect == null) {
            LOG.error("Attempted to decode null ClientAudioVisualEffect! Ignoring...");
            return new ClientAVEffectPacket(AVEffect.NONE);
        } else return new ClientAVEffectPacket(effect);
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
            effectPacket.audioVisualEffect.play();
        });
    }

    public enum AVEffect {

        NONE("none"),

        SEEDS_SPAWNED("seeds_spawned") {

            @Override
            protected void play() {
                if(Minecraft.getInstance().player == null) {
                    LOG.error("Cannot play AudioVisualEffect! Client Player instance is null.");
                    return;
                }

                BlockPos playerPos = Minecraft.getInstance().player.getPosition();

                EffectsUtil.displayStandardEffectsOnClient(playerPos, 10, ParticleTypes.FLASH);
                EffectsUtil.playNormalSoundWithVolumeOnClient(
                        playerPos, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundCategory.NEUTRAL, 8.0F
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

        protected void play() {
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
}
