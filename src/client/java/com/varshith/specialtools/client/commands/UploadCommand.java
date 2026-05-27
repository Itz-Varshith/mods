package com.varshith.specialtools.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.varshith.specialtools.client.tasks.PlacementTask;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class UploadCommand {
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, PlacementTask placementTask) {
        // Must use ClientCommandManager for UI/File stuff!
        dispatcher.register(ClientCommandManager.literal("upload")
                .executes(context -> execute(context, placementTask))
        );
    }

    // This function executes the command call, and it opens the file picker on a separate thread unrelated to the game thread to not hang the game and opens the folder with only two types of files accepted .jpg and .png. Then after the successful selection of an image the function creates a new PlacementTask returned by the process Image class and updates the values in the Task class passed to the execute function.
    public  int execute(CommandContext<FabricClientCommandSource> context, PlacementTask task) {

        FabricClientCommandSource source = context.getSource();
        source.sendFeedback(Component.literal("Opening file picker..."));

        // Run the file picker on a NEW thread so the game doesn't freeze!
        CompletableFuture.runAsync(() -> {
            try (MemoryStack stack = MemoryStack.stackPush()) {

                // Set up the allowed file extensions
                PointerBuffer filters = stack.mallocPointer(2);
                filters.put(stack.UTF8("*.jpg"));
                filters.put(stack.UTF8("*.png"));
                filters.flip();

                // Open the native file dialog (This is safe to use with Minecraft)
                String selectedPath = TinyFileDialogs.tinyfd_openFileDialog(
                        "Select an Image to Import",
                        "",
                        filters,
                        "Image Files (*.jpg, *.png)",
                        false
                );

                // If they clicked "Open" and didn't cancel
                if (selectedPath != null) {
                    File file = new File(selectedPath);

                    // Creation of task happens here and then the data is set to the main task object that has been passed to use.
                    PlacementTask taskResponse=processImage(file, source);
                    if(taskResponse==null){
                        source.sendError(Component.literal("Image not found/ invalid image data. Please try again."));
                    }else{
                        task.setData(taskResponse);
                    }
                } else {
                    source.sendFeedback(Component.literal("Upload canceled."));
                }
            }
        });

        return 1;
    }


    // This function processes the uploaded image extracts the required data from the image, creates the required Task object and returns it.
    public PlacementTask processImage(File selectedFile, FabricClientCommandSource source) {
        try {

            BufferedImage image = ImageIO.read(selectedFile);
            if (image == null) return null;

            int width = image.getWidth();
            int height = image.getHeight();

            if (width > 500 || height > 500) {
                source.sendError(Component.literal("Bro, please. Keep it under 500x500 so you don't melt your CPU."));
                return null;
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return null;

            // 1. Check if we are in Singleplayer so we can grab the Integrated Server
            if (mc.getSingleplayerServer() == null) {
                source.sendError(Component.literal("This command only works in single-player right now!"));
                return null;
            }

            // 2. Grab the actual SERVER level, not the Client level

            BlockPos startPos = mc.player.blockPosition();
            return new PlacementTask(width, height, image, startPos, source);
        } catch (Exception e) {
            source.sendError(Component.literal("Error processing image file!"));
            e.printStackTrace();
        }

        return null;
    }




}