/*
 * Copyright 2018-2021 Ki11er_wolf
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

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.item.ItemMineralHoe;
import com.ki11erwolf.resynth.util.JSerializer;
import com.ki11erwolf.resynth.util.SideUtil;
import com.ki11erwolf.resynth.util.Tooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DisplayHoeInfoPacket extends Packet<DisplayHoeInfoPacket> {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    private final ItemMineralHoe.MineralHoeInformation information;

    public DisplayHoeInfoPacket(ItemMineralHoe.MineralHoeInformation information) {
        this.information = information;
    }

    @Override
    BiConsumer<DisplayHoeInfoPacket, PacketBuffer> getEncoder() {
        return (mineralHoeInfoPacket, packetBuffer) -> {
            try {
                String jsonHoeInfo = ItemMineralHoe.MineralHoeInformation.INFORMATION_SERIALIZER_INSTANCE
                        .serializeObject(mineralHoeInfoPacket.information).getDataAsJsonString();
                writeString(jsonHoeInfo, packetBuffer);
            } catch (JSerializer.SerializeException e) {
                LOG.error("Failed to encode DisplayHoeInfoPacket - exception when serializing data.", e);
            }
        };
    }

    @Override
    Function<PacketBuffer, DisplayHoeInfoPacket> getDecoder() {
        try {
            return (packetBuffer) -> new DisplayHoeInfoPacket(
                    ItemMineralHoe.MineralHoeInformation.INFORMATION_SERIALIZER_INSTANCE.deserializeData(
                            JSerializer.JSerialData.fromJsonString(readString(packetBuffer))
                    )
            );
        } catch (JSerializer.SerializeException e) {
            LOG.error("Failed to decode DisplayHoeInfoPacket - exception when deserializing data.", e);
            return (packetBuffer) -> new DisplayHoeInfoPacket(null);
        }
    }

    @Override
    BiConsumer<DisplayHoeInfoPacket, Supplier<NetworkEvent.Context>> getHandler() {
        return (displayHoeInfoPacket, supplier) -> {
            if(displayHoeInfoPacket.information == null) {
                LOG.error("Skipping handling of DisplayHoeInfoPacket - information is null.");
                return;
            }

            if(!SideUtil.isClientTrueSafe()) {
                LOG.error("Skipping handling of DisplayHoeInfoPacket - not executing on physical client.");
                return;
            }

            Map<String, Object[]> hoeInfo = displayHoeInfoPacket.information.getInformation();
            StringBuilder message = new StringBuilder();

            for (Map.Entry<String, Object[]> lineInfo : hoeInfo.entrySet()) {
                String key = "message.resynth.mineral_hoe_information." + lineInfo.getKey();
                Object[] values = lineInfo.getValue();

                for(int i = 0; i < values.length; i++) {
                    if(values[i].toString().startsWith("$"))
                        values[i] = I18n.format(
                                ("value.resynth.mineral_hoe_information." + values[i]).replace("$", "")
                        );
                }

                message.append(I18n.format(key, values)).append("\n");
            }

            ClientPlayerEntity player = Objects.requireNonNull(Minecraft.getInstance().player);
            for(ITextComponent msg : Tooltip.formatLineFeeds(new StringTextComponent(message.toString())))
                player.sendMessage(msg, player.getUniqueID());
        };
    }
}
