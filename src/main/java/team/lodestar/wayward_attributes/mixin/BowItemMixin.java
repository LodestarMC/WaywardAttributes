package team.lodestar.wayward_attributes.mixin;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import team.lodestar.wayward_attributes.tweaks.RangedAttributeTweaks;

@Mixin(BowItem.class)
public class BowItemMixin {

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;<init>(Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties enchmod$addBowAttributes(Item.Properties properties) {
        return RangedAttributeTweaks.addBowProperties(properties);
    }
}
