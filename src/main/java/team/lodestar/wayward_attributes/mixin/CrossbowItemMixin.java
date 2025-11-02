package team.lodestar.wayward_attributes.mixin;

import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import team.lodestar.wayward_attributes.tweaks.RangedAttributeTweaks;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyArg(method = "getChargeDuration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;modifyCrossbowChargingTime(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;F)F"), index = 2)
    private static float waywardAttributes$normalizeCrossbowChargingTime(float crossbowChargingTime) {
        return 1.0f;
    }
}
