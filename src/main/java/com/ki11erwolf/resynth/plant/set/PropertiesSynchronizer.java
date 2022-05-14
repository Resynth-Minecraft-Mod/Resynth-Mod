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

package com.ki11erwolf.resynth.plant.set;

import com.ki11erwolf.resynth.ResynthMod;
import com.ki11erwolf.resynth.packet.Packet;
import com.ki11erwolf.resynth.packet.SyncSetPropertiesPacket;
import com.ki11erwolf.resynth.plant.set.properties.AbstractPlantSetProperties;
import com.ki11erwolf.resynth.plant.set.properties.AbstractProduceProperties;
import com.ki11erwolf.resynth.util.SideUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.Logger;

//TODO: Document
enum PropertiesSynchronizer {

    INSTANCE;

    /**
     * The logger for this class.
     */
    private static final Logger LOG = ResynthMod.getNewLogger();

    // ***
    // API
    // ***

    protected void handleClientConnection(PlayerEntity playerEntity, MinecraftServer server) {
        ServerPlayerEntity player;
        if(!(playerEntity instanceof ServerPlayerEntity)){
            LOG.error("[Server] Cannot synchronize with connected client! Invalid PlayerEntity type!");
            return;
        }

        player = (ServerPlayerEntity) playerEntity;
        if(server instanceof DedicatedServer)
            synchronizeWithClient(player);
        else if(!player.getIpAddress().equals("local"))
            synchronizeWithClient(player);
        else restoreClient();
    }

    protected void handlePropertiesSynchronizing(String setName, AbstractPlantSetProperties properties,
                                                 AbstractProduceProperties produceProperties) {
        if(SideUtil.isClientSafe())
            synchronizeSetProperties(PlantSetAPI.getSetByName(setName), properties, produceProperties);
        else LOG.error("[Server] Cannot perform synchronization on Dedicated Server!");
    }

    // **************
    // Implementation
    // **************

    private void synchronizeWithClient(ServerPlayerEntity connectedClient) {
        LOG.info("[Server] A Client(ip=" + connectedClient.getIpAddress()+ ") has connected! " +
                "Attempting to synchronize the client  with the servers PlantSet Properties...");

        PlantSetRegistry.streamPlantSets().forEach(serverPlantSet -> {
            LOG.debug("[Server] Sending synchronization request for the '" + serverPlantSet.getSetName() + "' PlantSet Properties");
            Packet.send(PacketDistributor.PLAYER.with(() -> connectedClient),
                    new SyncSetPropertiesPacket(
                            serverPlantSet.getSetName(),
                            serverPlantSet.getPlantSetProperties(),
                            serverPlantSet.getProduceProperties()
                    )
            );
        });
    }

    @OnlyIn(Dist.CLIENT)
    private void synchronizeSetProperties(PlantSet<?, ?> plantSet, AbstractPlantSetProperties properties,
                                          AbstractProduceProperties produce) {
        if(plantSet != null){
            LOG.info("[Client] Synchronizing own '" + plantSet.getSetName() + "' PlantSet with the Properties sent by the servers.");
            plantSet.setServerPlantSetProperties(properties);
            plantSet.setServerPlantSetProduceProperties(produce);
        } else LOG.error("[Client] Failed to synchronize! The requested PlantSet is unknown, invalid, or not registered!");
    }

    @OnlyIn(Dist.CLIENT)
    private void restoreClient() {
        LOG.info("[Client] Connected to local world! Ensuring PlantSets use Client Properties...");
        long count = PlantSetRegistry.streamPlantSets().peek((clientPlantSet -> {
            clientPlantSet.clearServerPlantSetProperties();
            clientPlantSet.clearServerPlantSetProduceProperties();
        })).count();

        LOG.info("[Client] Processed " + count + " PlantSet Properties!");
    }

    // *****
    // Hooks
    // *****

    @Mod.EventBusSubscriber(modid = ResynthMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class Hooks {

        @SubscribeEvent
        public static void onPlayerServerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            PlayerEntity player = event.getPlayer();
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

            INSTANCE.handleClientConnection(event.getPlayer(), server);
        }
    }

}
