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
package com.ki11erwolf.resynth.item;

import com.ki11erwolf.resynth.block.ResynthBlocks;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralSoilConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The item class for Mineral Rocks. Primarily used to
 * implement the Shift-Add feature where a stack can be
 * added to Mineral Soil with a shift-click.
 */
class ItemMineralRock extends ResynthItem<ItemMineralRock> {

    /**
     * Creates the item and defines its name.
     */
    ItemMineralRock() {
        super("mineral_rock");
    }

    /**
     * {@inheritDoc}
     *
     * <p/>Provides the Shift-Add feature.
     *
     * <p/>Handles adding a stack of Mineral Rocks to
     * the targeted Mineral Soil block with a
     * shift-right-click.
     *
     * @param context the context in which the item was used.
     *                Holds various information regarding
     *                the use of the item.
     * @return {@link ActionResultType#SUCCESS} if the item
     * was successfully used.
     */
    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        //Checks and definitions
        if(context.getPlayer() == null)
            return ActionResultType.FAIL;

        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState source = world.getBlockState(pos);

        if(!player.isCrouching() || source.getBlock() != ResynthBlocks.BLOCK_MINERAL_SOIL)
            return ActionResultType.FAIL;

        TileEntityMineralSoil soilEntity;

        if(!(world.getTileEntity(pos) instanceof TileEntityMineralSoil))
            return ActionResultType.FAIL;

        //Do increase
        soilEntity = (TileEntityMineralSoil) world.getTileEntity(pos);

        //noinspection ConstantConditions
        float concentration = soilEntity.getMineralPercentage();
        int count = context.getItem().getCount();
        int countUsed = 0;
        float worth = (float) ResynthConfig.GENERAL_CONFIG
                .getCategory(MineralSoilConfig.class).getMineralRockWorth();

        while(count > 0 && concentration < 50){
            concentration += worth;
            count--;
            countUsed++;
        }

        context.getItem().shrink(countUsed);
        soilEntity.setMineralPercentage(concentration);

        source.neighborChanged(world, pos, source.getBlock(), pos, false);

        return ActionResultType.SUCCESS;
    }
}
