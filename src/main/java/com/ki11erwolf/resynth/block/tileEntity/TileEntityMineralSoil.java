/*
 * Copyright 2018-2019 Ki11er_wolf
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
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityType;

/**
 * The tile entity for the block BlockMineralSoil.
 * This class acts as the container for the
 * blocks mineralPercentage value.
 */
public class TileEntityMineralSoil extends TileEntity {

    /**
     * Registry ID for this tile entity.
     */
    public static final String TE_ID = "te_mineral_soil";

    /**
     * TileEntityType object registered to this tile entity.
     */
    public static final TileEntityType<TileEntityMineralSoil> TE_MINERAL_SOIL
            = TileEntityType.register(TE_ID, TileEntityType.Builder.create(TileEntityMineralSoil::new));

    /**
     * The given blocks mineral mineralPercentage. Range: {@code 0.1 < x < 50.0}.
     */
    private float mineralPercentage = 1;

    /**
     * Default constructor.
     */
    public TileEntityMineralSoil() {
        super(TE_MINERAL_SOIL);
    }

    /**
     * Writes the mineralPercentage variable value to NBT.
     *
     * @param compound the nbt tag compound we will write the variable to.
     * @return the nbt tag compound.
     */
    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound.setFloat("mineralPercentage", mineralPercentage);
        return super.write(compound);
    }

    /**
     * Reads the mineralPercentage variable value from NBT.
     *
     * @param compound the nbt tag compound we will read the variable from.
     */
    @Override
    public void read(NBTTagCompound compound) {
        mineralPercentage = compound.getFloat("mineralPercentage");
        super.read(compound);
    }

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
    public void decreaseMineralPercentage(float percentage) {
        setMineralPercentage(getMineralPercentage() - percentage);
    }
}
