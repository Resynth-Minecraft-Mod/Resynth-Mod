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
package com.ki11erwolf.resynth.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;

import java.util.Random;

public class MatchBlockListRuleTest extends RuleTest {

    // ***************
    //  General Tests
    // ***************

    public static final RuleTest MATCH_OVERWORLD_ROCK = new MatchBlockListRuleTest(
            Blocks.STONE, Blocks.DIORITE, Blocks.GRANITE, Blocks.ANDESITE
    );

    public static final RuleTest MATCH_NETHERWORLD_ROCK = new MatchBlockListRuleTest(
            Blocks.NETHERRACK
    );

    public static final RuleTest MATCH_ENDWORLD_ROCK = new MatchBlockListRuleTest(
            Blocks.END_STONE
    );

    // ***********
    //  Rule Test
    // ***********

    @SuppressWarnings("deprecation")
    private static final Codec<MatchBlockListRuleTest> CODEC =
            Registry.BLOCK.fieldOf("block").xmap(
                    MatchBlockListRuleTest::new, (p_237076_0_) -> p_237076_0_.blocks[0]
    ).codec();

    private static final IRuleTestType<MatchBlockListRuleTest> BLOCK_LIST_MATCH =
            IRuleTestType.func_237129_a_("block_list_match", CODEC);

    private final Block[] blocks;

    public MatchBlockListRuleTest(Block... blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean test(BlockState state, Random random) {
        for(Block block : blocks){
            if(state.getBlock() == block)
                return true;
        }
        return false;
    }

    @Override
    protected IRuleTestType<?> getType() {
        return BLOCK_LIST_MATCH;
    }
}
