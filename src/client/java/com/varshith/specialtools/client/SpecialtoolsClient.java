package com.varshith.specialtools.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.varshith.specialtools.client.commands.ChunkBreaker;
import com.varshith.specialtools.client.commands.UploadCommand;
import com.varshith.specialtools.client.tasks.PlacementTask;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.w3c.dom.Text;

import java.util.Collection;

public class SpecialtoolsClient implements ClientModInitializer {

    @Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // Creation of a upload command class i am trying to modulate code and the uploadCommand class does the necessary tasks for command creation.
        UploadCommand uploadCommand = new UploadCommand();
        // Creation of a class here inherently supports the persistence cause this function will only ever be called once and hence will never be recreated or destroyed as long as the client runs which perfectly fits the scope of this mod we are doing.
        PlacementTask placementTask = new PlacementTask();


        // Passing the placementTask class here to update its values whenever the uploadCommand is called.
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            uploadCommand.register(dispatcher, placementTask);
        });


        // At the start of every tick the system checks for an incomplete placementTask object and does the processing if required.
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if(placementTask.isWorking()){
                boolean res=placementTask.run(client);
                if(!res){
                    assert client.getSingleplayerServer() != null;
                    client.getSingleplayerServer().sendSystemMessage(Component.literal("Placement failed"));
                }
            }
        });



    }
}