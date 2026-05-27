package com.varshith.specialtools.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

public class ChunkBreaker {

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("break-chunk").executes(this::execute));
    }
    public int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerLevel level=context.getSource().getLevel();
        Vec3 posi=context.getSource().getPosition();


        BlockPos pos=new BlockPos((int)posi.x, (int)posi.y-1, (int)posi.z);
        ChunkPos chunkPos=new ChunkPos(pos);
        int xmin=chunkPos.getMinBlockX();
        int xmax=chunkPos.getMaxBlockX();
        int zmin=chunkPos.getMinBlockZ();
        int zmax=chunkPos.getMaxBlockZ();

        for(int i=xmin;i<=xmax;i++){
            for(int j=zmin;j<=zmax;j++){
                for(int k=-45;k<=45;k++){

                    BlockPos bp=new BlockPos(i,j,k);
                    level.destroyBlock(bp, false);
                }
            }
        }
        return 1;
    }
}
