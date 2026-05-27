package com.varshith.specialtools.client.commands;

import net.minecraft.world.level.block.state.BlockState;

public class PaletteBlock {
    private final BlockState blockState;
    private final int red;
    private final int green;
    private final int blue;

    public PaletteBlock(BlockState defaultBlockState, int red, int green, int blue) {
        this.blockState = defaultBlockState;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getDistance(int bred, int bblue, int bgreen){
        return Math.abs(red - bred) + Math.abs(green - bgreen) + Math.abs(blue - bblue);
    }

    public BlockState getBlockState() {
        return this.blockState;
    }


}
