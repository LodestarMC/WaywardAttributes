package team.lodestar.wayward_attributes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.lodestar.wayward_attributes.config.WaywardAttributesCommonConfig;


@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @WrapOperation(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z"))
    private boolean waywardAttributes$modifySweepingDamageRatioMax(AbstractArrow instance, Operation<Boolean> original) {
        if (WaywardAttributesCommonConfig.CONFIG.bakedArrowCritDamage.getValue()) {
            return false;
        }
        return original.call(instance);
    }
}