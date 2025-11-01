package team.lodestar.wayward_attributes.tweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.registry.ModAttachmentTypes;
import team.lodestar.wayward_attributes.registry.ModAttributeTypes;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

import java.util.List;

public class RangedAttributeTweaks {

    public static final ResourceLocation BASE_ARROW_DAMAGE = ResourceLocation.withDefaultNamespace("base_arrow_damage");
    public static final ResourceLocation BASE_ARROW_VELOCITY = ResourceLocation.withDefaultNamespace("base_arrow_velocity");
    public static final ResourceLocation BASE_DRAW_SPEED = ResourceLocation.withDefaultNamespace("base_draw_speed");

    public static boolean modifyDrawSpeed(LivingEntity target, ItemStack stack) {
        if (stack.is(ModTags.ItemTags.AFFECTED_BY_DRAW_SPEED)) {
            float drawSpeed = (float) target.getAttributeValue(ModAttributeTypes.DRAW_SPEED);
            var data = target.getData(ModAttachmentTypes.DRAW_SPEED_DATA);
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
        float velocity = original * (float) (shooter.getAttributeValue(ModAttributeTypes.ARROW_VELOCITY));
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
            float damage = (float) (shooter.getAttributeValue(ModAttributeTypes.ARROW_DAMAGE));
            arrow.setBaseDamage(damage);
        }
    }

    public static Item.Properties addCrossbowProperties(Item.Properties properties) {
        return addRangedItemProperties(properties, 2f, 4f, 0.75f);
    }

    public static Item.Properties addBowProperties(Item.Properties properties) {
        return addRangedItemProperties(properties, 2f, 3f, 1.0f);
    }

    public static Item.Properties addRangedItemProperties(Item.Properties properties, float arrowDamage, float arrowVelocity, float drawSpeed) {
        return LodestoneItemProperties.mergeAttributes(properties,
                ItemAttributeModifiers.builder()
                        .add(ModAttributeTypes.ARROW_DAMAGE, new AttributeModifier(BASE_ARROW_DAMAGE, arrowDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                        .add(ModAttributeTypes.ARROW_VELOCITY, new AttributeModifier(BASE_ARROW_VELOCITY, arrowVelocity, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                        .add(ModAttributeTypes.DRAW_SPEED, new AttributeModifier(BASE_DRAW_SPEED, drawSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                        .build());
    }
}
