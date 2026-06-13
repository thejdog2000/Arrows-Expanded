package com.jacobs.mae;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArrowsExpandedMod implements ModInitializer {
    public static final String MOD_ID = "minecraft-arrows-expanded";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.serverboundPlay().register(ArrowCyclePayload.TYPE, ArrowCyclePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ArrowCyclePayload.TYPE, (payload, context) -> {
            if (context.player().getMainHandItem().is(Items.BOW) || context.player().getOffhandItem().is(Items.BOW)) {
                context.server().execute(() -> ArrowSelectionManager.inspectOrCycle(context.player()));
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
        LOGGER.info("Minecraft Arrows Expanded loaded for Minecraft 26.1.2");
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("arrowsexpanded")
                .then(Commands.literal("status").executes(context -> status(context.getSource())))
                .then(Commands.literal("reload").executes(context -> reload(context.getSource()))));

        dispatcher.register(Commands.literal("mae")
                .then(Commands.literal("status").executes(context -> status(context.getSource())))
                .then(Commands.literal("reload").executes(context -> reload(context.getSource()))));
    }

    private static int status(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Minecraft Arrows Expanded is loaded. Modes: regular, web 3x1, lightning, explosive, napalm-explosive, knock back, teleport."), false);
        return 1;
    }

    private static int reload(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Minecraft Arrows Expanded has no config yet; runtime systems are active."), false);
        return 1;
    }
}
