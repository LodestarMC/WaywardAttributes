package team.lodestar.wayward_attributes.util;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import team.lodestar.lodestone.modules.toolkit.item.*;
import team.lodestar.lodestone.modules.toolkit.item.tools.*;
import team.lodestar.wayward_attributes.*;
import team.lodestar.wayward_attributes.core.registry.*;

import static team.lodestar.lodestone.registry.common.LodestoneAttributes.*;

public class MagicAxeItem extends LodestoneAxeItem {

    public MagicAxeItem(Tier tier, float attackDamage, float attackSpeed, float magicDamage, LodestoneItemProperties properties) {
        super(tier, attackDamage, attackSpeed, properties.mergeAttributes(
                ItemAttributeModifiers.builder()
                        .add(WaywardAttributeTypes.MAGIC_DAMAGE, new AttributeModifier(BASE_MAGIC_DAMAGE, magicDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()));
    }
}