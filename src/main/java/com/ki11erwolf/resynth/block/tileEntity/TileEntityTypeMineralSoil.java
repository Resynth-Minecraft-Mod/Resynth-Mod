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
package com.ki11erwolf.resynth.block.tileEntity;

import com.ki11erwolf.resynth.ResynthMod;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

/**
 * The tile entity type and registration class
 * for {@link TileEntityMineralSoil}.
 */
public class TileEntityTypeMineralSoil {

    /**
     * Reference to the tile entity type.
     */
    @ObjectHolder(ResynthMod.MOD_ID + ":" + TileEntityMineralSoil.TE_ID)
    static TileEntityType<?> TE_MINERAL_SOIL;

    //The below was copied from:
    //https://github.com/progwml6/ironchest/blob/1.13/src/main/java/com/progwml6/ironchest/common/tileentity/IronChestEntityType.java
    //and modified.

    /**
     * Registration class.
     */
    @SuppressWarnings("unused")//Reflection
    @Mod.EventBusSubscriber(modid = ResynthMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        /**
         * Handles registering the tile entities.
         *
         * @param e forge event.
         */
        @SubscribeEvent
        @SuppressWarnings("unused")//Reflection
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e) {
            registerTileEntityType(
                    e.getRegistry(), register(
                            TileEntityMineralSoil.TE_ID, TileEntityType.Builder.create(TileEntityMineralSoil::new)
                    ),
                    ResynthMod.MOD_ID + ":" + TileEntityMineralSoil.TE_ID
            );
        }
    }

    /**
     * Registers the given tile entity to the game.
     *
     * @param registry the tile entity registry.
     * @param tileEntityType the tile entity type.
     * @param name name of the tile entity.
     * @param <T> the tile entity type class.
     */
    private static <T extends TileEntityType<?>> void registerTileEntityType(
            IForgeRegistry<TileEntityType<?>> registry, T tileEntityType,
            @SuppressWarnings("SameParameterValue") String name){
        register(registry, tileEntityType, new ResourceLocation(name));
    }

    /**
     * Registers a given tile entities type to the game.
     *
     * @param registry the game registry.
     * @param thing the registry entry.
     * @param name the tile entity type name.
     * @param <T> the tile entity type.
     * @return {@code thing}.
     */
    protected static <T extends IForgeRegistryEntry<T>> T register(
            IForgeRegistry<T> registry, T thing, ResourceLocation name){
        thing.setRegistryName(name);
        registry.register(thing);
        return thing;
    }

    /**
     * Registers a given tile entity and its type
     * to the game.
     *
     * @param id tile entity name.
     * @param builder the builder used to create the tile entity.
     * @param <T> the tile entity.
     * @return the tile entity.
     */
    public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder) {
        Type<?> type = null;

        try {
            type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1519))
                    .getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        } catch (IllegalArgumentException ex) {
            if (SharedConstants.developmentMode) {
                throw ex;
            }
        }

        //noinspection ConstantConditions
        return builder.build(type);
    }
}
