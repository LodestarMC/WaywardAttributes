package team.lodestar.wayward_attributes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import team.lodestar.wayward_attributes.tweaks.RangedAttributeTweaks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "updateUsingItem",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;shouldTriggerItemUseEffects()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;completeUsingItem()V")),
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/LivingEntity;useItemRemaining:I", opcode = Opcodes.GETFIELD, shift = At.Shift.BEFORE),
            cancellable = true)
    private void enchmod$applyDrawSpeed(CallbackInfo ci, @Local(argsOnly = true) ItemStack stack) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (RangedAttributeTweaks.modifyDrawSpeed(entity, stack)) {
            ci.cancel();
        }
    }
}
