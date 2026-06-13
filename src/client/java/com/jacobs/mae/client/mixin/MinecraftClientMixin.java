package com.jacobs.mae.client.mixin;

import com.jacobs.mae.ArrowCyclePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void minecraftArrowsExpanded$cycleBowArrow(CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient minecraft = (MinecraftClient) (Object) this;
        if (minecraft.player == null || minecraft.currentScreen != null) {
            return;
        }

        boolean holdingBow = minecraft.player.getMainHandStack().isOf(Items.BOW) || minecraft.player.getOffHandStack().isOf(Items.BOW);
        if (holdingBow && ClientPlayNetworking.canSend(ArrowCyclePayload.ID)) {
            ClientPlayNetworking.send(ArrowCyclePayload.INSTANCE);
            cir.setReturnValue(true);
        }
    }
}
