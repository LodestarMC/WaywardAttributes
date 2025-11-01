package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.AABB;
import team.lodestar.wayward_attributes.registry.ModAttributeTypes;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

public class SweepAttackTweaks {

    public static final ResourceLocation BASE_SWEEP_DAMAGE = ResourceLocation.withDefaultNamespace("base_sweeping_damage_ratio");
    public static final ResourceLocation BASE_SWEEP_RADIUS = ResourceLocation.withDefaultNamespace("base_sweeping_damage_radius");

    public static AABB modifySweepingArea(Player player, AABB aabb) {
        float radius = (float) player.getAttributeValue(ModAttributeTypes.SWEEPING_DAMAGE_RADIUS);
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

    public static Item.Properties addSwordSweeping(Item.Properties properties) {
        return addSwordProperties(properties, 0.25f, 0.75f);
    }

    public static Item.Properties addSwordProperties(Item.Properties properties, float damage, float radius) {
        return LodestoneItemProperties.mergeAttributes(properties,
                ItemAttributeModifiers.builder()
                        .add(Attributes.SWEEPING_DAMAGE_RATIO, new AttributeModifier(BASE_SWEEP_DAMAGE, damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(ModAttributeTypes.SWEEPING_DAMAGE_RADIUS, new AttributeModifier(BASE_SWEEP_RADIUS, radius, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build());
    }
}