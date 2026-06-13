package com.jacobs.mae.mixin;

import com.jacobs.mae.ArrowEffectTracker;
import com.jacobs.mae.ArrowSelectionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Inject(method = "shoot", at = @At("TAIL"))
    private void minecraftArrowsExpanded$tagSelectedArrow(LivingEntity shooter, ProjectileEntity projectile, int index, float velocity, float divergence, float yaw, LivingEntity target, CallbackInfo ci) {
        if (shooter instanceof ServerPlayerEntity player && projectile instanceof PersistentProjectileEntity arrow) {
            ArrowEffectTracker.tag(arrow, ArrowSelectionManager.getSelected(player));
        }
    }
}
