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
package com.ki11erwolf.resynth.util;

import com.ki11erwolf.resynth.block.BlockEnhancer;
import com.ki11erwolf.resynth.block.BlockMineralSoil;
import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
import com.ki11erwolf.resynth.plant.block.BlockBiochemicalPlant;
import com.ki11erwolf.resynth.plant.block.BlockCrystallinePlant;
import com.ki11erwolf.resynth.plant.block.BlockMetallicPlant;
import com.ki11erwolf.resynth.plant.block.BlockPlant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Implemented by both {@link com.ki11erwolf.resynth.block.BlockMineralSoil}
 * and {@link com.ki11erwolf.resynth.plant.block.BlockPlant} to provide
 * information to the Mineral Hoe. The implementing class merely needs to
 * implement this interface for it to take effect.
 *
 * <p/>This implementation choice was made as it seamed a best fit for
 * providing the same functionality to the 2 specific classes.
 *
 * <p/>All logic is handled in the less visible {@link PPIPHelper}
 * class. This interface simply serves as a way to "subscribe" to the
 * desired functionality.
 */
//TODO: Server side localization, better implementation. Perhaps use packets.
public interface PlantPatchInfoProvider extends BlockInfoProvider {

    /**
     * {@inheritDoc}
     *
     * <p/>Will provide both the plant and soil block information
     * to the Mineral Hoe when requested. This method is capable
     * of determining which block was used to get the information.
     */
    @Override
    default void appendBlockInformation(List<String> information, World world, BlockPos pos, BlockState block) {
        PPIPHelper.appendBlockInformation(information, world, pos, block);
    }

    // *************
    // END INTERFACE
    // *************

    /**
     * The helper class that serves to accomplish the task of
     * the PlantPatchInfoProvider. All logic is done in this
     * class to make it less visible (lack of sub-public members
     * in an interface). This class should be treated as private.
     */
    final class PPIPHelper {

        //Private constructor - util class.
        private PPIPHelper(){ }

        /**
         * Determines what part of the plant patch (soil or
         * plant) was used when getting the information
         * and appends the information from both to the
         * provided information array.
         *
         * @param information the provided information array.
         * @param world the world the plant patch is in.
         * @param pos the position of the plant patch in the world.
         */
        private static void appendBlockInformation(List<String> information, World world, BlockPos pos,
                                                   BlockState block) {
            BlockPos plantPos, soilPos;

            //Determine which block (plant or soil) was used.
            if(block.getBlock() instanceof BlockMineralSoil){
                soilPos = pos;
                plantPos = pos.up();
            } else if (block.getBlock() instanceof BlockPlant){
                plantPos = pos;
                soilPos = pos.down();
            } else {
                //Not supported.
                information.add("Error!");
                return;
            }

            //Actually get the information.
            appendMineralSoilInformation(information, world, soilPos);
            appendPlantInformation(information, world, plantPos);
        }

        /**
         * Will append the given information from the given
         * Mineral Soil block to the provided information array.
         *
         * @param information the provided information array.
         * @param world the world the block is in.
         * @param pos the position of the block in the world.
         */
        private static void appendMineralSoilInformation(List<String> information, World world, BlockPos pos){
            //Begin
            information.add(
                    TextFormatting.GRAY + "---------- Mineral Soil Info -----------"
            );

            //Mineral Content
            TileEntityMineralSoil entity = getTileEntity(world, pos);
            information.add(
                    TextFormatting.GREEN + "Mineral Content: " +
                    TextFormatting.GOLD + entity.getMineralPercentage() + "%"
            );

            //Enhancer & combined
            BlockState enhancer = world.getBlockState(pos.down());
            String enhancerIncrease;
            float combined = entity.getMineralPercentage();
            boolean hasEnhancer = false;

            if(enhancer.getBlock() instanceof BlockEnhancer){
                hasEnhancer = true;
                //Make sure we take 50% concentration needed.
                if(entity.getMineralPercentage() < 50.0F){
                    enhancerIncrease = TextFormatting.RED + "Not Enabled  (Need 50% concentration)!";
                    combined += 0;
                } else {
                    BlockEnhancer enhancerBlock = (BlockEnhancer)enhancer.getBlock();
                    enhancerIncrease = enhancerBlock.getIncrease() + "%";
                    combined += enhancerBlock.getIncrease();
                }
            } else enhancerIncrease = "No Enhancer!";

            if(hasEnhancer){
                information.add(
                        TextFormatting.LIGHT_PURPLE + "Enhancer Increase: " + TextFormatting.GOLD + enhancerIncrease
                );

                if(combined != entity.getMineralPercentage())
                    information.add(
                            TextFormatting.AQUA + "Combined Mineral Content: " + TextFormatting.GOLD + combined + "%"
                    );
            }
        }

        /**
         * Will append the given information from the given plant
         * to the provided information array.
         *
         * @param information the provided information array.
         * @param world the world the plant is in.
         * @param pos the position of the plant in the world.
         */
        private static void appendPlantInformation(List<String> information, World world, BlockPos pos){
            BlockState plantBlockState = world.getBlockState(pos);
            Block plantBlock = plantBlockState.getBlock();

            //Begin
            information.add(
                    TextFormatting.GRAY + "------------- Plant Info -------------"
            );

            //No plant on soil.
            if(!(plantBlock instanceof BlockPlant)){
                information.add(
                        TextFormatting.RED + "No Plant!"
                );

                //End
                information.add(
                        TextFormatting.GRAY + "------------------------------------"
                );

                return;
            }

            BlockPlant<?> blockPlant = (BlockPlant<?>) plantBlock;

            //Exact plant
            information.add(
                    TextFormatting.YELLOW + "Specific Plant: " + TextFormatting.GOLD + blockPlant.getRegistryName()
            );

            //Plant type
            String plantType;

            if(blockPlant instanceof BlockBiochemicalPlant) {
                plantType = "Biochemical";
            } else if (blockPlant instanceof BlockMetallicPlant) {
                plantType = "Metallic";
            } else if (blockPlant instanceof BlockCrystallinePlant) {
                plantType = "Crystalline";
            } else plantType = "Error!";

            information.add(
                    TextFormatting.DARK_AQUA + "Plant Type: " + TextFormatting.GOLD + plantType
            );

            //Growth Stage
            information.add(
                    TextFormatting.DARK_PURPLE + "Growth Stage: " +
                            TextFormatting.GOLD + (blockPlant.getGrowthStage(plantBlockState) + 1) +
                            TextFormatting.DARK_PURPLE + " of " + TextFormatting.GOLD +
                            (blockPlant.getMaxGrowthStage() + 1)
            );

            //Growth chance
            information.add(
                    TextFormatting.BLUE + "Growth Chance: " +
                            TextFormatting.GOLD + ((BlockPlant<?>) plantBlock).getProperties().chanceToGrow() + "%"
            );

            //Combined Growth Chance
            float plantGrowthChance = ((BlockPlant<?>) plantBlock).getProperties().chanceToGrow() / 100;

            TileEntityMineralSoil entity = getTileEntity(world, pos.down());
            float mineralSoilConcentration = entity.getMineralPercentage();

            BlockState enhancer = world.getBlockState(pos.down().down());
            float enhancerIncrease = 0;

            if(enhancer.getBlock() instanceof BlockEnhancer){
                enhancerIncrease = ((BlockEnhancer)enhancer.getBlock()).getIncrease();
            }

            float combinedChance = (enhancerIncrease + mineralSoilConcentration) / 100;

            //Final Chance
            float finalChance = (combinedChance * plantGrowthChance) * 100;
            double roundedFinalChance = (double) Math.round(finalChance * 100) / 100;
            information.add(
                    TextFormatting.DARK_GREEN + "Final Growth Chance: " +
                            TextFormatting.GOLD + roundedFinalChance + "%"
            );

            //End
            information.add(
                    TextFormatting.GRAY + "------------------------------------"
            );
        }

        /**
         * Casts the tile entity at the given position
         * in the given world to a TileEntityMineralSoil
         * and returns it.
         *
         * @param world the world the tile entity is in.
         * @param pos the position in the world the tile entity is at.
         * @return the cast tile entity.
         */
        private static TileEntityMineralSoil getTileEntity(World world, BlockPos pos){
            return (TileEntityMineralSoil)world.getTileEntity(pos);//Faith in the cast...
        }
    }
}
