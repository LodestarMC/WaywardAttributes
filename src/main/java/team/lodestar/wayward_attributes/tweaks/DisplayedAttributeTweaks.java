package team.lodestar.wayward_attributes.tweaks;

import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.neoforged.neoforge.common.extensions.IAttributeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import team.lodestar.lodestone.registry.common.tag.*;
import team.lodestar.lodestone.systems.enchanting.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.registry.WaywardAttributeTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

public class DisplayedAttributeTweaks {

    public static final ResourceLocation BASE_MINING_EFFICIENCY = ResourceLocation.withDefaultNamespace("base_mining_efficiency");

    public static Multimap<Holder<Attribute>, AttributeModifier> getModifiersForTooltip(Multimap<Holder<Attribute>, AttributeModifier> original, ItemStack stack, EquipmentSlotGroup slot) {
        var group = EquipmentSlotGroup.MAINHAND;
        if (stack.is(ModTags.ItemTags.AFFECTED_BY_DRAW_SPEED)) {
            group = EquipmentSlotGroup.HAND;
        }

        if (slot.equals(group)) {
            return getAccurateItemModifiers(original, stack);
        }
        return original;
    }

    public static Multimap<Holder<Attribute>, AttributeModifier> getAccurateItemModifiers(Multimap<Holder<Attribute>, AttributeModifier> map, ItemStack stack) {
        if (stack.is(LodestoneItemTags.ENCHANTMENT_HOLDER)) {
            map.clear();
            return map;
        }
        if (stack.is(ItemTags.MINING_ENCHANTABLE)) {
            Tool tool = stack.get(DataComponents.TOOL);
            if (tool != null) {
                double speed = tool.rules().stream().mapToDouble(r -> r.speed().orElse(0f).doubleValue()).max().orElse(0);
                map.put(Attributes.MINING_EFFICIENCY, new AttributeModifier(BASE_MINING_EFFICIENCY, speed, AttributeModifier.Operation.ADD_VALUE));
            }
        }

        modifyBaseAttribute(map, stack, Attributes.ATTACK_DAMAGE, EnchantmentEffectComponents.DAMAGE);
        modifyAttributes(map, Attributes.SWEEPING_DAMAGE_RATIO, Attributes.ATTACK_DAMAGE);

        modifyBaseAttribute(map, stack, WaywardAttributeTypes.ARROW_DAMAGE, EnchantmentEffectComponents.DAMAGE);
        modifyAttributes(map, WaywardAttributeTypes.ARROW_DAMAGE, WaywardAttributeTypes.ARROW_VELOCITY);


        modifyBaseAttribute(map, stack, WaywardAttributeTypes.DRAW_SPEED, EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, f -> -f);

        return map;
    }

    public static void modifyBaseAttribute(Multimap<Holder<Attribute>, AttributeModifier> map, ItemStack stack, Holder<Attribute> modified, DataComponentType<?> component) {
        modifyBaseAttribute(map, stack, modified, component, f -> f);
    }

    @SuppressWarnings("unchecked")
    public static void modifyBaseAttribute(Multimap<Holder<Attribute>, AttributeModifier> map, ItemStack stack, Holder<Attribute> modified, DataComponentType<?> component, Float2FloatFunction valueModifier) {
        if (modified instanceof DeferredHolder<?, ?> deferredHolder) {
            modified = (Holder<Attribute>) deferredHolder.getDelegate();
        }
        if (map.containsKey(modified)) {
            AttributeModifier base = null;
            for (AttributeModifier attributeModifier : map.get(modified)) {
                ResourceLocation baseId = modified.value().getBaseId();
                if (attributeModifier.id().equals(baseId)) {
                    base = attributeModifier;
                    break;
                }
            }
            if (base != null) {
                float added = LodestoneEnchantmentValueEffectHelper.getComponentValue(stack, component, 0);
                added = valueModifier.get(added);
                if (added != 0) {
                    map.put(modified, growAttribute(base, added));
                }
            }
        }
    }

    public static void modifyAttributes(Multimap<Holder<Attribute>, AttributeModifier> map, Holder<Attribute> modified, Holder<Attribute> modifier) {
        if (map.containsKey(modified)) {
            var modifiers = LodestoneEnchantmentAttributeHelper.asAttributeModifiers(map);
            var needsRecalculation = new ArrayList<>(map.get(modified));
            map.removeAll(modified);
            for (AttributeModifier attributeModifier : needsRecalculation) {
                if (attributeModifier.operation().equals(AttributeModifier.Operation.ADD_VALUE)) {
                    map.put(modified, scaleAttribute(modifiers, attributeModifier, modifier));
                }
            }
            for (AttributeModifier attributeModifier : needsRecalculation) {
                if (!attributeModifier.operation().equals(AttributeModifier.Operation.ADD_VALUE)) {
                    map.put(modified, attributeModifier);
                }
            }
        }
    }

    public static AttributeModifier growAttribute(AttributeModifier modified, float addedValue) {
        return new AttributeModifier(modified.id().withSuffix("_added_value"), addedValue, AttributeModifier.Operation.ADD_VALUE);
    }

    public static AttributeModifier scaleAttribute(ItemAttributeModifiers modifiers, AttributeModifier modified, Holder<Attribute> modifier) {
        var player = Minecraft.getInstance().player;
        float playerBase = (float) player.getAttributeBaseValue(modifier);
        float itemBase = LodestoneEnchantmentAttributeHelper.getBaseValue(modifiers, playerBase, modifier);
        return new AttributeModifier(modified.id(), modified.amount() * itemBase, AttributeModifier.Operation.ADD_VALUE);
    }

    public static MutableComponent replaceListHeader(MutableComponent original) {
        return Component.literal(" \u2507 ").withStyle(ChatFormatting.DARK_GRAY);
    }

    public static ChatFormatting updateMergedAttributeColor(ChatFormatting original) {
        return ChatFormatting.DARK_GREEN;
    }

    public static MutableComponent updateMergedAttributeComponentColor(Component original) {
        if (original instanceof MutableComponent mutable) {
            return mutable.withStyle(ChatFormatting.DARK_GRAY);
        }
        return MutableComponent.create(original.getContents()).withStyle(ChatFormatting.DARK_GRAY);
    }

    public static ResourceLocation getBaseId(IAttributeExtension attribute) {
        if (attribute.equals(Attributes.MINING_EFFICIENCY.value())) {
            return BASE_MINING_EFFICIENCY;
        }
        if (attribute.equals(Attributes.SWEEPING_DAMAGE_RATIO.value())) {
            return SweepAttackTweaks.BASE_SWEEP_DAMAGE;
        }
        return null;
    }
}