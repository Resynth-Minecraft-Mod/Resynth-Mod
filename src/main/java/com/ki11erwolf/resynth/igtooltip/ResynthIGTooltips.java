///*
// * Copyright 2018-2019 Ki11er_wolf
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.ki11erwolf.resynth.igtooltip;
//
//import com.ki11erwolf.resynth.block.BlockMineralSoil;
//import com.ki11erwolf.resynth.block.tileEntity.TileEntityMineralSoil;
//import com.ki11erwolf.resynth.plant.block.BlockPlantBiochemical;
//import com.ki11erwolf.resynth.plant.block.BlockPlantCrystalline;
//import com.ki11erwolf.resynth.plant.block.BlockPlantMetallic;
//import net.minecraftforge.fml.common.Loader;
//
///**
// * Public initializer class and registry for
// * Hwyla/The One Probe and related classes (e.g. block
// * classes that provide custom information).
// */
//public class ResynthIGTooltips {
//
//    /**
//     * List of classes that provide custom Hwyla tooltip information.
//     */
//    //Maybe one day turn this into an annotation system...
//    static final Class[] TOOLTIP_CLASSES = new Class[]{
//            BlockMineralSoil.class,
//            BlockPlantMetallic.class,
//            BlockPlantCrystalline.class,
//            BlockPlantBiochemical.class
//    };
//
//    /**
//     * List of tile entities for blocks that provide custom Hwyla
//     * tooltip information.
//     */
//    static final Class[] TOOLTIP_TILE_ENTITY_CLASSES = new Class[]{
//            TileEntityMineralSoil.class
//    };
//
//    /**
//     * <b>Public Hwyla initialization/register method.</b>
//     *
//     * Use this method to set up Hwyla.
//     */
//    public static void registerHwyla() {
//        if (Loader.isModLoaded("waila" /*Actually Hwyla*/ )) {
//            HwylaCompatibility.register();
//        }
//    }
//
//    /**
//     * <b>Public TOP initialization/register method.</b>
//     *
//     * Use this method to set up The One Probe.
//     */
//    public static void registerTOP(){
//        if (Loader.isModLoaded("theoneprobe" )) {
//            TOPCompatibility.register();
//        }
//    }
//}
