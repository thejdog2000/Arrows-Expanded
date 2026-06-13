package com.jacobs.mae.mixin;

import com.jacobs.mae.ArrowEffectTracker;
import com.jacobs.mae.ArrowSelectionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(method = "shootProjectile", at = @At("TAIL"))
    private void minecraftArrowsExpanded$tagSelectedArrow(LivingEntity shooter, Projectile projectile, int index, float velocity, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
        if (shooter instanceof ServerPlayer player && projectile instanceof AbstractArrow arrow) {
            ArrowEffectTracker.tag(arrow, ArrowSelectionManager.getSelected(player));
        }
    }
}
