package team.lodestar.wayward_attributes.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract boolean isDamageableItem();

    @Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void waywardAttributes$changeDurabilityTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List<Component> list) {
        if (this.isDamageableItem()) {
            list.add(Component.translatable("item.wayward_durability", this.getMaxDamage() - this.getDamageValue(), this.getMaxDamage()).withStyle(ChatFormatting.WHITE));
        }
    }

    @Inject(method = "getTooltipLines", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void waywardAttributes$removeVanillaDurabilityTooltip(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        var ogList = cir.getReturnValue();
        var newList = ogList.stream().filter(c -> !(c.getContents() instanceof TranslatableContents contents && contents.getKey().contains("item.durability"))).toList();
        cir.setReturnValue(newList);
    }
}
