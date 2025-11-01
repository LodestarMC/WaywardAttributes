package team.lodestar.wayward_attributes.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;


@Mixin(Attributes.class)
public class AttributesMixin {

    @ModifyArg(method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/RangedAttribute;<init>(Ljava/lang/String;DDD)V", ordinal = 0),
            slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=attribute.name.player.sweeping_damage_ratio")), index = 2)
    private static double waywardAttributes$modifySweepingDamageRatioMax(double max) {
        if (max == 1.0D) return 2048.0D;
        return max;
    }
}