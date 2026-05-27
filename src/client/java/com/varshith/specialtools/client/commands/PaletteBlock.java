package com.varshith.specialtools.client.commands;

import net.minecraft.world.level.block.Blocks;
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


    public static BlockState getClosestColor(int red, int blue, int green) {
        int minDistance = Integer.MAX_VALUE;
        BlockState closestBlock = null;
        for (PaletteBlock paletteBlock : MASTER_PALETTE) {
            int dist = paletteBlock.getDistance(red, blue, green);
            if (dist < minDistance) {
                closestBlock = paletteBlock.getBlockState();
                minDistance = dist;
            }
        }

        return closestBlock;
    }

    public static final PaletteBlock[] MASTER_PALETTE = new PaletteBlock[]{
            // --- WOOL (Highly textured, vibrant but noisy) ---
            new PaletteBlock(Blocks.WHITE_WOOL.defaultBlockState(), 233, 236, 236),
            new PaletteBlock(Blocks.ORANGE_WOOL.defaultBlockState(), 240, 118, 19),
            new PaletteBlock(Blocks.MAGENTA_WOOL.defaultBlockState(), 189, 68, 179),
            new PaletteBlock(Blocks.LIGHT_BLUE_WOOL.defaultBlockState(), 58, 175, 217),
            new PaletteBlock(Blocks.YELLOW_WOOL.defaultBlockState(), 248, 197, 39),
            new PaletteBlock(Blocks.LIME_WOOL.defaultBlockState(), 112, 185, 25),
            new PaletteBlock(Blocks.PINK_WOOL.defaultBlockState(), 237, 141, 172),
            new PaletteBlock(Blocks.GRAY_WOOL.defaultBlockState(), 62, 68, 71),
            new PaletteBlock(Blocks.LIGHT_GRAY_WOOL.defaultBlockState(), 142, 142, 134),
            new PaletteBlock(Blocks.CYAN_WOOL.defaultBlockState(), 21, 137, 145),
            new PaletteBlock(Blocks.PURPLE_WOOL.defaultBlockState(), 121, 42, 172),
            new PaletteBlock(Blocks.BLUE_WOOL.defaultBlockState(), 53, 57, 157),
            new PaletteBlock(Blocks.BROWN_WOOL.defaultBlockState(), 114, 71, 40),
            new PaletteBlock(Blocks.GREEN_WOOL.defaultBlockState(), 84, 109, 27),
            new PaletteBlock(Blocks.RED_WOOL.defaultBlockState(), 160, 39, 34),
            new PaletteBlock(Blocks.BLACK_WOOL.defaultBlockState(), 20, 21, 25),

            // --- CONCRETE (Smooth, highly saturated, great for deep shadows/highlights) ---
            new PaletteBlock(Blocks.WHITE_CONCRETE.defaultBlockState(), 207, 213, 214),
            new PaletteBlock(Blocks.ORANGE_CONCRETE.defaultBlockState(), 224, 97, 0),
            new PaletteBlock(Blocks.MAGENTA_CONCRETE.defaultBlockState(), 169, 48, 153),
            new PaletteBlock(Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState(), 35, 137, 198),
            new PaletteBlock(Blocks.YELLOW_CONCRETE.defaultBlockState(), 240, 175, 21),
            new PaletteBlock(Blocks.LIME_CONCRETE.defaultBlockState(), 94, 168, 24),
            new PaletteBlock(Blocks.PINK_CONCRETE.defaultBlockState(), 213, 101, 142),
            new PaletteBlock(Blocks.GRAY_CONCRETE.defaultBlockState(), 54, 57, 61),
            new PaletteBlock(Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState(), 125, 125, 115),
            new PaletteBlock(Blocks.CYAN_CONCRETE.defaultBlockState(), 21, 119, 136),
            new PaletteBlock(Blocks.PURPLE_CONCRETE.defaultBlockState(), 100, 31, 156),
            new PaletteBlock(Blocks.BLUE_CONCRETE.defaultBlockState(), 44, 46, 143),
            new PaletteBlock(Blocks.BROWN_CONCRETE.defaultBlockState(), 96, 59, 31),
            new PaletteBlock(Blocks.GREEN_CONCRETE.defaultBlockState(), 73, 91, 36),
            new PaletteBlock(Blocks.RED_CONCRETE.defaultBlockState(), 142, 32, 32),
            new PaletteBlock(Blocks.BLACK_CONCRETE.defaultBlockState(), 8, 10, 15),

            // --- TERRACOTTA (Muted, warm tones - THE SECRET TO HUMAN SKIN/PORTRAITS) ---
            new PaletteBlock(Blocks.TERRACOTTA.defaultBlockState(), 152, 94, 67),          // Uncolored
            new PaletteBlock(Blocks.WHITE_TERRACOTTA.defaultBlockState(), 209, 178, 161),  // Actually a flesh pink
            new PaletteBlock(Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 161, 83, 37),   // Deep tan
            new PaletteBlock(Blocks.MAGENTA_TERRACOTTA.defaultBlockState(), 149, 88, 108),
            new PaletteBlock(Blocks.LIGHT_BLUE_TERRACOTTA.defaultBlockState(), 113, 108, 137),
            new PaletteBlock(Blocks.YELLOW_TERRACOTTA.defaultBlockState(), 186, 133, 35),  // Warm gold
            new PaletteBlock(Blocks.LIME_TERRACOTTA.defaultBlockState(), 103, 117, 52),
            new PaletteBlock(Blocks.PINK_TERRACOTTA.defaultBlockState(), 161, 78, 78),     // Rose tone
            new PaletteBlock(Blocks.GRAY_TERRACOTTA.defaultBlockState(), 57, 42, 35),      // Excellent for hair/shadows
            new PaletteBlock(Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState(), 135, 106, 97),
            new PaletteBlock(Blocks.CYAN_TERRACOTTA.defaultBlockState(), 86, 91, 91),
            new PaletteBlock(Blocks.PURPLE_TERRACOTTA.defaultBlockState(), 118, 70, 86),
            new PaletteBlock(Blocks.BLUE_TERRACOTTA.defaultBlockState(), 74, 59, 91),
            new PaletteBlock(Blocks.BROWN_TERRACOTTA.defaultBlockState(), 77, 51, 35),     // Deep dark skin tone
            new PaletteBlock(Blocks.GREEN_TERRACOTTA.defaultBlockState(), 76, 83, 42),
            new PaletteBlock(Blocks.RED_TERRACOTTA.defaultBlockState(), 143, 61, 46),
            new PaletteBlock(Blocks.BLACK_TERRACOTTA.defaultBlockState(), 37, 22, 16),

            // --- NATURAL BLOCKS (Fills in the gaps for grays, tans, and browns) ---
            new PaletteBlock(Blocks.DIRT.defaultBlockState(), 134, 96, 67),
            new PaletteBlock(Blocks.COARSE_DIRT.defaultBlockState(), 119, 85, 59),
            new PaletteBlock(Blocks.SAND.defaultBlockState(), 219, 211, 160),
            new PaletteBlock(Blocks.CLAY.defaultBlockState(), 160, 166, 179),
            new PaletteBlock(Blocks.STONE.defaultBlockState(), 125, 125, 125),
            new PaletteBlock(Blocks.COBBLESTONE.defaultBlockState(), 100, 100, 100),
            new PaletteBlock(Blocks.OBSIDIAN.defaultBlockState(), 20, 18, 29),
            new PaletteBlock(Blocks.SNOW_BLOCK.defaultBlockState(), 255, 255, 255), // The purest white possible

            // --- WOOD PLANKS (Excellent mid-tone browns) ---
            new PaletteBlock(Blocks.OAK_PLANKS.defaultBlockState(), 162, 130, 78),
            new PaletteBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), 104, 78, 41),
            new PaletteBlock(Blocks.BIRCH_PLANKS.defaultBlockState(), 215, 203, 141),
            new PaletteBlock(Blocks.JUNGLE_PLANKS.defaultBlockState(), 153, 113, 76),
            new PaletteBlock(Blocks.ACACIA_PLANKS.defaultBlockState(), 168, 90, 50),
            new PaletteBlock(Blocks.DARK_OAK_PLANKS.defaultBlockState(), 66, 43, 20)
    };

}
