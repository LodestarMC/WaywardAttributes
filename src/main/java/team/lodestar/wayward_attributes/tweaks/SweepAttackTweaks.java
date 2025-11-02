package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.core.component.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.event.*;
import team.lodestar.wayward_attributes.registry.WaywardAttributeTypes;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

import java.util.*;

public class SweepAttackTweaks {

    public static final ResourceLocation BASE_SWEEP_DAMAGE = ResourceLocation.withDefaultNamespace("base_sweeping_damage_ratio");
    public static final ResourceLocation BASE_SWEEP_RADIUS = ResourceLocation.withDefaultNamespace("base_sweeping_damage_radius");

    public static AABB modifySweepingArea(Player player, AABB aabb) {
        float radius = (float) player.getAttributeValue(WaywardAttributeTypes.SWEEPING_DAMAGE_RADIUS);
        float vertical = 0.25f * radius;
        var center = aabb.getCenter();
        var min = aabb.getMinPosition();
        var max = aabb.getMaxPosition();
        var minOffset = min.subtract(center);
        var maxOffset = max.subtract(center);
        var newMin = center.add(minOffset.multiply(radius, vertical, radius));
        var newMax = center.add(maxOffset.multiply(radius, vertical, radius));
        return new AABB(newMin, newMax);
    }

    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        var list = event.getAllItems().toList();
        for (Item item : list) {
            if (item.canPerformAction(item.getDefaultInstance(), ItemAbilities.SWORD_SWEEP)) {
                event.modify(item, b -> addSwordSweeping(item, b));
            }
        }
    }

    public static void addSwordSweeping(Item item, DataComponentPatch.Builder builder) {
        addSwordProperties(item, builder, 0.25f, 0.75f);
    }

    public static void addSwordProperties(Item item, DataComponentPatch.Builder builder, float damage, float radius) {
        var modifiers = item.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null) {
            return;
        }
        modifiers = modifiers.withModifierAdded(Attributes.SWEEPING_DAMAGE_RATIO, new AttributeModifier(BASE_SWEEP_DAMAGE, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        modifiers = modifiers.withModifierAdded(WaywardAttributeTypes.SWEEPING_DAMAGE_RADIUS, new AttributeModifier(BASE_SWEEP_RADIUS, radius, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }
}