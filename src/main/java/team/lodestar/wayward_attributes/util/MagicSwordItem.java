package team.lodestar.wayward_attributes.util;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import team.lodestar.lodestone.modules.toolkit.item.*;
import team.lodestar.wayward_attributes.core.registry.*;
import team.lodestar.wayward_attributes.tweaks.*;

import static team.lodestar.wayward_attributes.core.registry.WaywardAttributeTypes.BASE_MAGIC_DAMAGE;

public class MagicSwordItem extends WaywardSwordItem {

    public MagicSwordItem(Tier tier, float attackDamage, float attackSpeed, float sweepDamage, float sweepRadius, float magicDamage, LodestoneItemProperties properties) {
        super(tier, attackDamage, attackSpeed, sweepDamage, sweepRadius, properties.mergeAttributes(
                ItemAttributeModifiers.builder()
                        .add(WaywardAttributeTypes.MAGIC_DAMAGE, new AttributeModifier(BASE_MAGIC_DAMAGE, magicDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()));
    }
}
