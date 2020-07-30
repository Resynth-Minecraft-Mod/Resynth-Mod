/*
 * Copyright 2018-2020 Ki11er_wolf
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
package com.ki11erwolf.resynth.proxy;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.plant.set.PublicPlantSetRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Client side initialization class.
 */
public class ClientProxy extends ServerProxy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetup(FMLCommonSetupEvent event) {
        super.onSetup(event); //Always setup (dedicated) server when on client side.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ResynthBlocks.BLOCK_SEED_POD, RenderType.func_228641_d_());

        PublicPlantSetRegistry.foreach(PublicPlantSetRegistry.SetType.ALL, plantSet -> {
            RenderTypeLookup.setRenderLayer(plantSet.getPlantBlock(), RenderType.func_228641_d_());
            return null;
        });
    }
}