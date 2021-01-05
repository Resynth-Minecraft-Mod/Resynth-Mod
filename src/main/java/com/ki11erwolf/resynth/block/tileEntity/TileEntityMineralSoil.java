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
package com.ki11erwolf.resynth.block.tileEntity;

import com.ki11erwolf.resynth.config.ResynthConfig;
import com.ki11erwolf.resynth.config.categories.MineralSoilConfig;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

/**
 * The tile entity for the block BlockMineralSoil.
 * This class acts as the container for the
 * blocks mineralPercentage value.
 */
public class TileEntityMineralSoil extends TileEntity {

    /**
     * Configuration settings for this tile entity.
     */
    private static final MineralSoilConfig CONFIG
            = ResynthConfig.GENERAL_CONFIG.getCategory(MineralSoilConfig.class);

    /**
     * The NBT tag key for Mineral Content.
     */
    public static final String MINERAL_CONTENT_TAG = "mineralPercentage";

    /**
     * Registry ID for this tile entity.
     */
    static final String TE_ID = "te_mineral_soil";

    /**
     * The given blocks mineral mineralPercentage. Range: {@code 0.1 < x < 50.0}.
     */
    private float mineralPercentage = (float) CONFIG.getStartingMineralContent();

    /**
     * Default Constructor.
     */
    public TileEntityMineralSoil(){
        super(TileEntityTypeMineralSoil.TE_MINERAL_SOIL);
    }

    // **************
    // Read and write
    // **************

    /**
     * Writes the mineralPercentage variable value to NBT.
     *
     * @param compound the nbt tag compound we will write the variable to.
     * @return the nbt tag compound.
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putFloat(MINERAL_CONTENT_TAG, mineralPercentage);
        return compound;
    }

    /**
     * Reads the mineralPercentage variable value from NBT.
     *
     * @param compound the nbt tag compound we will read the variable from.
     */
    @Override //used to be read()
    public void func_230337_a_(BlockState state, CompoundNBT compound) {
        super.func_230337_a_(state, compound);
        mineralPercentage = compound.getFloat(MINERAL_CONTENT_TAG);
    }

    // ***
    // API
    // ***

    /**
     * @return The given blocks mineral percentage.
     * Range: {@code 0.1 < x < 50.0}.
     */
    public float getMineralPercentage() {
        return mineralPercentage;
    }

    /**
     * Sets the given blocks mineral percentage.
     *
     * @param mineralPercentage the mineral percentage to set the value to.
     * Range: {@code 0.1 < x < 50.0}.
     */
    @SuppressWarnings("WeakerAccess")
    public void setMineralPercentage(float mineralPercentage) {
        if(mineralPercentage < 0.1F)
            mineralPercentage = 1.0F;
        if(mineralPercentage > 50.0F)
            mineralPercentage = 50.0F;

        this.mineralPercentage = mineralPercentage;
        markDirty();
    }

    /**
     * Increases the given blocks mineral percentage
     * by a given amount.
     * Range: {@code 0.1 < x < 50.0}.
     *
     * @param percentage the percentage to increase the
     * blocks mineral percentage by.
     */
    public void increaseMineralPercentage(float percentage) {
        setMineralPercentage(getMineralPercentage() + percentage);
    }

    /**
     * Decreases the given blocks mineral percentage
     * by a given amount.
     * Range: {@code 0.1 < x < 50.0}.
     *
     * @param percentage the percentage to decrease the
     * blocks mineral percentage by.
     */
    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void decreaseMineralPercentage(float percentage) {
        setMineralPercentage(getMineralPercentage() - percentage);
    }
}
