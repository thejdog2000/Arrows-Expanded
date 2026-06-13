package com.jacobs.mae;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrowsExpandedMod implements ModInitializer {
    public static final String MOD_ID = "minecraft-arrows-expanded";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(ArrowCyclePayload.ID, ArrowCyclePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ArrowCyclePayload.ID, (payload, context) -> {
            if (context.player().getMainHandStack().isOf(Items.BOW) || context.player().getOffHandStack().isOf(Items.BOW)) {
                context.server().execute(() -> ArrowSelectionManager.inspectOrCycle(context.player()));
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
        LOGGER.info("Minecraft Arrows Expanded loaded for Minecraft 1.21.11");
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("arrowsexpanded")
                .then(CommandManager.literal("status").executes(context -> status(context.getSource())))
                .then(CommandManager.literal("reload").executes(context -> reload(context.getSource()))));

        dispatcher.register(CommandManager.literal("mae")
                .then(CommandManager.literal("status").executes(context -> status(context.getSource())))
                .then(CommandManager.literal("reload").executes(context -> reload(context.getSource()))));
    }

    private static int status(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Minecraft Arrows Expanded is loaded. Modes: regular, web 3x1, lightning, explosive, napalm-explosive, knock back, teleport."), false);
        return 1;
    }

    private static int reload(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Minecraft Arrows Expanded has no config yet; runtime systems are active."), false);
        return 1;
    }
}
