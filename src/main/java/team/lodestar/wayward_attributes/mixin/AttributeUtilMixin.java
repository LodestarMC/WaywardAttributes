package team.lodestar.wayward_attributes.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.util.AttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.wayward_attributes.tweaks.DisplayedAttributeTweaks;

@Mixin(AttributeUtil.class)
public class AttributeUtilMixin {

    @WrapOperation(method = "applyModifierTooltips",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/AttributeUtil;getSortedModifiers(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlotGroup;)Lcom/google/common/collect/Multimap;"))
    private static Multimap<Holder<Attribute>, AttributeModifier> enchmod$addAttributeTooltips(ItemStack stack, EquipmentSlotGroup slot, Operation<Multimap<Holder<Attribute>, AttributeModifier>> original) {
        return DisplayedAttributeTweaks.getModifiersForTooltip(original.call(stack, slot), stack, slot);
    }

    @WrapOperation(method = "applyTextFor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/Attribute;getBaseId()Lnet/minecraft/resources/ResourceLocation;"))
    private static ResourceLocation enchmod$markAttributeAsBase(Attribute instance, Operation<ResourceLocation> original) {
        var id = DisplayedAttributeTweaks.getBaseId(instance);
        if (id != null) {
            return id;
        }
        return original.call(instance);
    }

    @Inject(method = "listHeader",
            at = @At(value = "HEAD"), cancellable = true)
    private static void enchmod$modifyListHeader(CallbackInfoReturnable<MutableComponent> cir) {
        cir.setReturnValue(DisplayedAttributeTweaks.replaceListHeader(cir.getReturnValue()));
    }

    @ModifyArg(method = "applyTextFor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")))
    private static ChatFormatting enchmod$modifyMergedAttributeColor(ChatFormatting format) {
        return DisplayedAttributeTweaks.updateMergedAttributeColor(format);
    }

    @ModifyArg(method = "applyTextFor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 1),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;")))
    private static ChatFormatting enchmod$modifyMergedAttributeBaseColor(ChatFormatting format) {
        return DisplayedAttributeTweaks.updateMergedAttributeComponentColor(format);
    }

    @WrapOperation(method = "applyTextFor",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/Attribute;toComponent(Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;Lnet/minecraft/world/item/TooltipFlag;)Lnet/minecraft/network/chat/MutableComponent;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;"),
            to = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 4)))
    private static MutableComponent enchmod$modifyMergedAttributeBaseColor(Attribute attribute, AttributeModifier modifier, TooltipFlag tooltipFlag, Operation<MutableComponent> original) {
        var result = original.call(attribute, modifier, tooltipFlag);
        return result.withStyle(DisplayedAttributeTweaks.updateMergedAttributeComponentColor(null));
    }
}
