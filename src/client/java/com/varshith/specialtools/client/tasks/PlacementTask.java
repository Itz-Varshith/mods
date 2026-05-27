package com.varshith.specialtools.client.tasks;


import com.mojang.realmsclient.gui.task.DataFetcher;
import com.varshith.specialtools.client.commands.PaletteBlock;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.impl.attachment.AttachmentPersistentState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.image.BufferedImage;

public class PlacementTask {
    private int x;
    private int y;
    private BlockPos startPos;
    private BufferedImage image;
    private FabricClientCommandSource source;
    private boolean working;

    private int currentX;
    private int currentY;

    private int ticks=0;

    private Minecraft mc;

    // Default constructor with default data.
    public PlacementTask(){
        x=-1;
        y=-1;
        image=null;
        source=null;
        startPos=null;
        mc=null;
        working=false;
    }

    // Constructor with data from the source, can be used when creating new objects.
    public PlacementTask(int x, int y, BufferedImage image, BlockPos startPos, FabricClientCommandSource source) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.working=true;
        this.startPos = startPos;
        this.source = source;
        this.currentX = 0;
        this.currentY = 0;
    }
    // Getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public BufferedImage getImage() {
        return image;
    }
    public boolean isWorking() {
        return working;
    }
    public BlockPos getStartPos() {
        return startPos;
    }
    public FabricClientCommandSource getSource() { return source; }

    // Setters
    public void setY(int y) {
        this.y = y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void setStartPos(BlockPos startPos) {
        this.startPos = startPos;
    }
    public void setSource(FabricClientCommandSource source) {
        this.source = source;
    }
    public void setWorking(boolean working) {
        this.working = working;
    }

    // Main function which is called again and again in the entire course of
    public boolean run(Minecraft client) {

        ticks++;
        this.mc=client;
        int width=x;
        int height=y;
        if(mc.getSingleplayerServer()==null){
            return false;
        }
        net.minecraft.server.level.ServerLevel serverLevel = mc.getSingleplayerServer().getLevel(mc.player.level().dimension());
//        source.sendFeedback(Component.literal("Mapping " + 1000 + " blocks..."));

        // 3. Queue the world modification to happen on the Server's MAIN thread safely!
        mc.getSingleplayerServer().execute(() -> {

            int blocksPlaced = 0;
//                for (int y = 0; y < height; y++) {
//                    for (int x = 0; x < width; x++) {
//
//                        int pixelColor = image.getRGB(x, y);
//                        int alpha = (pixelColor >> 24) & 0xff;
//                        if (alpha == 0) continue;
//
//                        int red = (pixelColor >> 16) & 0xff;
//                        int green = (pixelColor >> 8) & 0xff;
//                        int blue = pixelColor & 0xff;
//
//                        int brightness = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
//
//                        BlockState targetBlock;
//
//
//                        if (brightness > 191) {
//                            targetBlock = Blocks.WHITE_WOOL.defaultBlockState();
//                        }
//                        else if (brightness > 127) {
//                            targetBlock = Blocks.LIGHT_GRAY_WOOL.defaultBlockState();
//                        }
//                        else if (brightness > 63) {
//                            targetBlock = Blocks.GRAY_WOOL.defaultBlockState();
//                        }
//                        else {
//                            targetBlock = Blocks.BLACK_WOOL.defaultBlockState();
//                        }
//
//                        BlockPos targetPos = startPos.offset(x, 0, y);
//
//                        // 4. Use Flag 2 to prevent lighting engine crashes on large images
//                        serverLevel.setBlock(targetPos, targetBlock, 2);
//                        blocksPlaced++;
//                    }
//                }
            // Two styles to place image one is bottom to top i.e changes y coords, and the above commented code makes it so that the y coordinate is constant.
            while( currentY < height) {


                int verticalOffset = (height - 1) - currentY;
//                System.out.println("Inside the first loop, here with, " + currentY);
               while(currentX < width) {
//                    System.out.println(currentX + " " + currentY);
                    int pixelColor = image.getRGB(currentX, currentY);
                    int alpha = (pixelColor >> 24) & 0xff;
                    if (alpha == 0) continue;

                    int red = (pixelColor >> 16) & 0xff;
                    int green = (pixelColor >> 8) & 0xff;
                    int blue = pixelColor & 0xff;

                    int brightness = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                    BlockState targetBlock=getClosestColor(red, blue, green);

                    // 2. The Golden Rule of 2D Walls: Keep one dimension at ZERO!
                    // Here, we offset X (left/right), Y (up/down), and leave Z at 0 (flat).
                    BlockPos targetPos = startPos.offset(currentX, verticalOffset, 0);

                    serverLevel.setBlock(targetPos, targetBlock, 2);
                    blocksPlaced++;
                    currentX++;
                }
                   currentX=0;



                currentY++;
                if(blocksPlaced>=1000){
                    break;
                }

            }
//            source.sendFeedback(Component.literal("Successfully placed " + blocksPlaced + " blocks!"));
        });
        if(currentY>=height){
            working=false;
        }
        return true;
    }

    public BlockState getClosestColor(int red, int blue, int green) {
        int minDistance=Integer.MAX_VALUE;
        BlockState closestBlock=null;
        for(PaletteBlock paletteBlock : MASTER_PALETTE) {
            int dist=paletteBlock.getDistance(red, blue, green);
            if(dist<minDistance){
                closestBlock=paletteBlock.getBlockState();
                minDistance=dist;
            }
        }

        return closestBlock;
    }
    // Put this as a static variable at the top of your UploadCommand class
    public static final PaletteBlock[] MASTER_PALETTE = new PaletteBlock[] {
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

    public void reset() {
        currentX=0;
        currentY=0;
        ticks=0;
    }

    public void setData(PlacementTask taskResponse) {
        this.currentX=0;
        this.currentY=0;
        this.ticks=0;
        this.setWorking(false);
        this.setStartPos(taskResponse.getStartPos());
        this.setImage(taskResponse.getImage());
        this.setX(taskResponse.getX());
        this.setY(taskResponse.getY());
        this.setSource(taskResponse.getSource());
    }
}
