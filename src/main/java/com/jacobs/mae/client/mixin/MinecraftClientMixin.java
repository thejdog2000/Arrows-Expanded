package com.jacobs.mae.client.mixin;

import com.jacobs.mae.ArrowCyclePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(method = "startAttack", at = @At("HEAD"))
    private void minecraftArrowsExpanded$cycleBowArrow(CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = (Minecraft) (Object) this;
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        boolean holdingBow = minecraft.player.getMainHandItem().is(Items.BOW) || minecraft.player.getOffhandItem().is(Items.BOW);
        if (holdingBow && ClientPlayNetworking.canSend(ArrowCyclePayload.TYPE)) {
            ClientPlayNetworking.send(ArrowCyclePayload.INSTANCE);
        }
    }
}
