package com.varshith.specialtools.client.tasks;


import com.varshith.specialtools.client.commands.PaletteBlock;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.Palette;

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


    private Minecraft mc;

    // Default constructor with default data.
    public PlacementTask() {
        x = -1;
        y = -1;
        image = null;
        source = null;
        startPos = null;
        mc = null;
        working = false;
    }

    // Constructor with data from the source, can be used when creating new objects.
    public PlacementTask(int x, int y, BufferedImage image, BlockPos startPos, FabricClientCommandSource source) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.working = true;
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

    public FabricClientCommandSource getSource() {
        return source;
    }

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

    // Main function which is called again and again in the entire course of ticks where the image processing happens.
    public boolean run(Minecraft client) {
        System.out.println("placed");
        this.mc = client;
        int width = x;
        int height = y;

        // this checks every active run of the function so that there is no game crashes cause of usage of attributes after game close or world exit etc.
        if (mc == null || mc.getSingleplayerServer() == null || mc.getSingleplayerServer().getLevel(mc.player.level().dimension()) == null || this.image==null) {
            this.reset();
            return false;
        }

        ServerLevel serverLevel = mc.getSingleplayerServer().getLevel(mc.player.level().dimension());

        // 3. Queue the world modification to happen on the Server's MAIN thread safely!
        mc.getSingleplayerServer().execute(() -> {

            int blocksPlaced = 0;

            while (currentY < height) {
                // Keeping the code thread safe cause sometimes two threads the client and the server may update and fuck up.
                if (!this.isWorking() || this.image == null) {
                    return;
                }

                int verticalOffset = (height - 1) - currentY;
                while (currentX < width) {
                    int pixelColor = image.getRGB(currentX, currentY);
                    int alpha = (pixelColor >> 24) & 0xff;
                    if (alpha == 0) {
                        currentX++;
                        continue;
                    }

                    int red = (pixelColor >> 16) & 0xff;
                    int green = (pixelColor >> 8) & 0xff;
                    int blue = pixelColor & 0xff;


                    BlockState targetBlock = PaletteBlock.getClosestColor(red, blue, green);


                    // Here, we offset X (left/right), Y (up/down), and leave Z at 0 (flat).
                    BlockPos targetPos = startPos.offset(currentX, verticalOffset, 0);

                    serverLevel.setBlock(targetPos, targetBlock, 2);
                    blocksPlaced++;
                    currentX++;
                }

                currentX = 0;
                currentY++;

                if (blocksPlaced >= 1000) {
                    break;
                }

            }
        });

        if (currentY >= height) {
            this.reset();
        }

        return true;
    }


    public void setData(PlacementTask taskResponse) {
        this.currentX = 0;
        this.currentY = 0;
        this.setWorking(true);
        this.setStartPos(taskResponse.getStartPos());
        this.setImage(taskResponse.getImage());
        this.setX(taskResponse.getX());
        this.setY(taskResponse.getY());
        this.setSource(taskResponse.getSource());
    }

    public void reset(){
        this.currentX = 0;
        this.currentY = 0;
        this.setWorking(false);
        this.setStartPos(null);
        this.setImage(null);
        this.setX(-1);
        this.setY(-1);
        this.setSource(null);
    }

}
