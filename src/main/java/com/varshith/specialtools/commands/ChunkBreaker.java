package com.varshith.specialtools.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ChunkBreaker {

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("break-chunk").executes(this::execute));
    }

    public int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level = context.getSource().getLevel();

        // BlockPos.containing() safely converts the exact decimal Vec3 to the integer grid
        BlockPos playerPos = BlockPos.containing(context.getSource().getPosition());

        ChunkPos chunkPos = new ChunkPos(playerPos);
        int xmin = chunkPos.getMinBlockX();
        int xmax = chunkPos.getMaxBlockX();
        int zmin = chunkPos.getMinBlockZ();
        int zmax = chunkPos.getMaxBlockZ();

        int blocksRemoved = 0;
        level.destroyBlock(playerPos, false);
        for(int i = xmin; i <= xmax; i++) {
            for(int j = zmin; j <= zmax; j++) {
                for(int k = -64; k <= playerPos.getY() - 1; k++) {

                    BlockPos bp = new BlockPos(i, k, j);

                    if(level.getBlockState(bp).isAir() || level.getBlockState(bp).getBlock() == Blocks.BEDROCK) {
                        continue;
                    }
                    BlockState state=level.getBlockState(bp);

                    if(state.is(BlockTags.DIAMOND_ORES)) {
                        System.out.println("well!!");
                        level.destroyBlock(bp, true);
                    }else{
                        level.removeBlock(bp, false);
                    }

                    blocksRemoved++;
                }
            }
        }
        String s="Blocks removed: " + blocksRemoved;
        // Send a success message to the chat instead of System.out.println
        context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(s), false);

        return 1;
    }
}