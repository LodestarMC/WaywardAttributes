package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.core.component.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.event.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.config.WaywardAttributesCommonConfig;
import team.lodestar.wayward_attributes.core.registry.WaywardAttachmentTypes;
import team.lodestar.wayward_attributes.core.registry.WaywardAttributeTypes;

import java.util.List;

public class RangedAttributeTweaks {

    public static final ResourceLocation BASE_ARROW_DAMAGE = ResourceLocation.withDefaultNamespace("base_arrow_damage");
    public static final ResourceLocation BASE_ARROW_VELOCITY = ResourceLocation.withDefaultNamespace("base_arrow_velocity");
    public static final ResourceLocation BASE_DRAW_SPEED = ResourceLocation.withDefaultNamespace("base_draw_speed");

    public static boolean modifyDrawSpeed(LivingEntity target, ItemStack stack) {
        if (stack.is(WaywardTags.ItemTags.AFFECTED_BY_DRAW_SPEED)) {
            float drawSpeed = (float) target.getAttributeValue(WaywardAttributeTypes.DRAW_SPEED);
            var data = target.getData(WaywardAttachmentTypes.DRAW_SPEED_DATA);
            int raise = data.increment(drawSpeed);
            target.useItemRemaining -= raise;
            if (target.useItemRemaining <= 0 && !target.level().isClientSide && !stack.useOnRelease()) {
                target.completeUsingItem();
            }
            return true;
        }
        return false;
    }

    public static float modifyVelocity(float original, LivingEntity shooter, ItemStack stack, List<ItemStack> projectileItems) {
        float velocity = original * (float) (shooter.getAttributeValue(WaywardAttributeTypes.ARROW_VELOCITY));
        float base = 3f;
        if (stack.getItem() instanceof CrossbowItem) {
            if (projectileItems.stream().anyMatch(s -> s.is(Items.FIREWORK_ROCKET))) {
                base = 6.4f;
            }
            else {
                base = 3.15f;
            }
        }
        return velocity / base;
    }

    public static void modifyDamage(LivingEntity shooter, Entity projectile) {
        if (projectile instanceof AbstractArrow arrow) {
            float damage = (float) (shooter.getAttributeValue(WaywardAttributeTypes.ARROW_DAMAGE));
            arrow.setBaseDamage(damage);
        }
    }

    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        var list = event.getAllItems().toList();
        for (Item item : list) {
            if (item instanceof BowItem) {
                event.modify(item, b -> addBowProperties(item, b));
            }
            else if (item instanceof CrossbowItem) {
                event.modify(item, b -> addCrossbowProperties(item, b));
            }
        }
    }

    public static void addCrossbowProperties(Item item, DataComponentPatch.Builder builder) {
        var config = WaywardAttributesCommonConfig.CONFIG;

        float arrowDamage = config.baseArrowDamage.getValue().floatValue();
        float arrowVelocity = config.crossbowArrowVelocity.getValue().floatValue();
        float drawSpeed = config.crossbowDrawSpeed.getValue().floatValue();
        if (config.bakedArrowCritDamage.getValue()) {
            arrowDamage *= 1.5f;
        }
        addRangedItemProperties(item, builder, arrowDamage, arrowVelocity, drawSpeed);
    }

    public static void addBowProperties(Item item, DataComponentPatch.Builder builder) {
        var config = WaywardAttributesCommonConfig.CONFIG;

        float arrowDamage = config.baseArrowDamage.getValue().floatValue();
        float arrowVelocity = config.bowArrowVelocity.getValue().floatValue();
        float drawSpeed = config.bowDrawSpeed.getValue().floatValue();
        if (config.bakedArrowCritDamage.getValue()) {
            arrowDamage *= 1.5f;
        }
        addRangedItemProperties(item, builder, arrowDamage, arrowVelocity, drawSpeed);
    }

    public static void addRangedItemProperties(Item item, DataComponentPatch.Builder builder, float arrowDamage, float arrowVelocity, float drawSpeed) {
        var modifiers = item.components().get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers == null) {
            return;
        }
        modifiers = modifiers.withModifierAdded(WaywardAttributeTypes.ARROW_DAMAGE.getDelegate(), new AttributeModifier(BASE_ARROW_DAMAGE, arrowDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
        modifiers = modifiers.withModifierAdded(WaywardAttributeTypes.ARROW_VELOCITY.getDelegate(), new AttributeModifier(BASE_ARROW_VELOCITY, arrowVelocity, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
        modifiers = modifiers.withModifierAdded(WaywardAttributeTypes.DRAW_SPEED.getDelegate(), new AttributeModifier(BASE_DRAW_SPEED, drawSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND);
        builder.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }
}
