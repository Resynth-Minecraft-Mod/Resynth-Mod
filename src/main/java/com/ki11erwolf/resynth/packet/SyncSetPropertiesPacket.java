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
import com.ki11erwolf.resynth.plant.set.PlantSetAPI;
import com.ki11erwolf.resynth.plant.set.properties.*;
import com.ki11erwolf.resynth.util.JSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncSetPropertiesPacket extends Packet<SyncSetPropertiesPacket> {

    private static final Map<String, JSerializer<? extends AbstractPlantSetProperties>> TYPENAME_TO_SERIALIZER
            = new HashMap<String, JSerializer<? extends AbstractPlantSetProperties>>(){{
        put("biochemical", AbstractBiochemicalProperties.SERIALIZER);
        put("crystalline", AbstractCrystallineProperties.SERIALIZER);
        put("metallic", AbstractMetallicProperties.SERIALIZER);
    }};

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    private final String setName;

    private final AbstractPlantSetProperties setProperties;

    private final AbstractProduceProperties produceProperties;

    public SyncSetPropertiesPacket(String setName, AbstractPlantSetProperties setProperties, AbstractProduceProperties produceProperties) {
        this.setName = Objects.requireNonNull(setName);
        this.setProperties = Objects.requireNonNull(setProperties);
        this.produceProperties = Objects.requireNonNull(produceProperties);
    }

    @Override
    BiConsumer<SyncSetPropertiesPacket, PacketBuffer> getEncoder() {
        return (propertiesPacket, packetBuffer) -> {
            AbstractPlantSetProperties props = propertiesPacket.setProperties;
            if (props instanceof AbstractBiochemicalProperties) {
                writeString("biochemical", packetBuffer);
                writeString(JSerializer.serialize((AbstractBiochemicalProperties) props).getDataAsJsonString(), packetBuffer);
            } else if (props instanceof AbstractCrystallineProperties) {
                writeString("crystalline", packetBuffer);
                writeString(JSerializer.serialize((AbstractCrystallineProperties) props).getDataAsJsonString(), packetBuffer);
            } else if (props instanceof AbstractMetallicProperties) {
                writeString("metallic", packetBuffer);
                writeString(JSerializer.serialize((AbstractMetallicProperties) props).getDataAsJsonString(), packetBuffer);
            } else {
                LOG.error("Cannot serialize and encode PlantSet Properties! Unknown set type.");
                return;
            }

            writeString(JSerializer.serialize(propertiesPacket.produceProperties).getDataAsJsonString(), packetBuffer);
            writeString(propertiesPacket.setName, packetBuffer);
        };
    }

    @Override
    Function<PacketBuffer, SyncSetPropertiesPacket> getDecoder() {
        return (packetBuffer) -> {
            String type = readString(packetBuffer);
            JSerializer<? extends AbstractPlantSetProperties> serializer = TYPENAME_TO_SERIALIZER.get(type);

            if(serializer == null) {
                LOG.error("Cannot deserialize and decode PlantSet Properties! Unknown set typename.");
                return null;
            }

            try {
                AbstractPlantSetProperties deserializedSetProperties = serializer.deserializeData(
                        JSerializer.JSerialData.fromJsonString(readString(packetBuffer))
                );
                AbstractProduceProperties deserializedProduceProperties = AbstractProduceProperties.SERIALIZER.deserializeData(
                        JSerializer.JSerialData.fromJsonString(readString(packetBuffer))
                );

                String deserializedSetName = readString(packetBuffer);

                return new SyncSetPropertiesPacket(deserializedSetName, deserializedSetProperties, deserializedProduceProperties);
            } catch (JSerializer.SerializeException | IllegalArgumentException | JsonSyntaxException exception) {
                LOG.error("Failed to deserialize PlantSet Properties!", exception);
                return null;
            }
        };
    }

    @Override
    BiConsumer<SyncSetPropertiesPacket, Supplier<NetworkEvent.Context>> getHandler() {
        return (packet, supplier) -> Packet.handle(supplier,
                () -> PlantSetAPI.synchronizePlantSetProperties(packet.setName, packet.setProperties, packet.produceProperties)
        );
    }
}
