package com.jacobs.mae.mixin;

import com.jacobs.mae.ArrowEffectTracker;
import com.jacobs.mae.ArrowEffects;
import com.jacobs.mae.ArrowMode;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {
    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void minecraftArrowsExpanded$applyEntityImpact(EntityHitResult hitResult, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ArrowMode mode = ArrowEffectTracker.consume(arrow);
        ArrowEffects.apply(arrow, mode, hitResult.getLocation(), hitResult.getEntity());
    }

    @Inject(method = "onHitBlock", at = @At("TAIL"))
    private void minecraftArrowsExpanded$applyBlockImpact(BlockHitResult hitResult, CallbackInfo ci) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        ArrowMode mode = ArrowEffectTracker.consume(arrow);
        ArrowEffects.apply(arrow, mode, hitResult.getLocation(), null);
    }
}
