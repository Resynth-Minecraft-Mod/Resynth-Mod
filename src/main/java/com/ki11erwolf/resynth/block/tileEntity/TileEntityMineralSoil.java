/*
 * Copyright 2018 Ki11er_wolf
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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * The tile entity for the block BlockMineralSoil.
 * This class acts as the object container for the
 * blocks percentage value.
 */
public class TileEntityMineralSoil extends TileEntity {

    /**
     * The given blocks mineral percentage. Range: {@code 0.1 < x < 50.0}.
     */
    private float percentage = 1;

    /**
     * Writes the percentage variable value to file.
     *
     * @param compound the nbt tag compound we will write the variable to.
     * @return the nbt tag compound.
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setFloat("percentage", percentage);
        return super.writeToNBT(compound);
    }

    /**
     * Reads the percentage variable value from file.
     *
     * @param compound the nbt tag compound we will write the variable to.
     */
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        percentage = compound.getFloat("percentage");
        super.readFromNBT(compound);
    }

    /**
     * @return The given blocks mineral percentage.
     * Range: {@code 0.1 < x < 50.0}.
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     * Sets the given blocks mineral percentage.
     *
     * @param percentage the percentage to set the value to.
     * Range: {@code 0.1 < x < 50.0}.
     */
    public void setPercentage(float percentage) {
        if(percentage < 0.1F)
            percentage = 1.0F;
        if(percentage > 50.0F)
            percentage = 50.0F;

        this.percentage = percentage;
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
    public void incrementPercentage(float percentage) {
        setPercentage(getPercentage() + percentage);
    }

    /**
     * Decreases the given blocks mineral percentage
     * by a given amount.
     * Range: {@code 0.1 < x < 50.0}.
     *
     * @param percentage the percentage to decrease the
     * blocks mineral percentage by.
     */
    public void decrementPercentage(float percentage) {
        setPercentage(getPercentage() - percentage);
    }
}
