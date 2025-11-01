package team.lodestar.wayward_attributes.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import team.lodestar.wayward_attributes.tweaks.SweepAttackTweaks;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    @ModifyArg(method = "<init>(Lnet/minecraft/world/item/Tier;Lnet/minecraft/world/item/Item$Properties;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TieredItem;<init>(Lnet/minecraft/world/item/Tier;Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties enchmod$addSwordAttributes(Item.Properties properties) {
        return SweepAttackTweaks.addSwordSweeping(properties);
    }
    @ModifyArg(method = "<init>(Lnet/minecraft/world/item/Tier;Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/item/component/Tool;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TieredItem;<init>(Lnet/minecraft/world/item/Tier;Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties enchmod$addSwordAttributesOtherConstructor(Item.Properties properties) {
        return SweepAttackTweaks.addSwordSweeping(properties);
    }
}
