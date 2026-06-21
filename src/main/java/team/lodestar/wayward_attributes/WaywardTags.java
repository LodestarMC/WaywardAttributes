package team.lodestar.wayward_attributes;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.item.*;

public class WaywardTags {

    public static class ItemTags {

        public static final TagKey<Item> AFFECTED_BY_DRAW_SPEED = tag("affected_by_draw_speed");

        public static TagKey<Item> tag(String path) {
            return WaywardTags.tag(Registries.ITEM, path);
        }
    }

    public static class DamageTypeTags {

        public static final TagKey<DamageType> IS_MAGIC = common("is_magic");
        public static final TagKey<DamageType> AFFECTED_BY_MAGIC_RESISTANCE = tag("affected_by_magic_resistance");
        public static final TagKey<DamageType> AFFECTED_BY_MAGIC_PROFICIENCY = tag("affected_by_magic_proficiency");
        public static final TagKey<DamageType> CAN_TRIGGER_MAGIC_DAMAGE = common("can_trigger_magic_damage");
        public static final TagKey<DamageType> IGNORES_MAGIC_ATTACK_COOLDOWN_SCALAR = common("ignores_magic_attack_cooldown_scalar");

        public static TagKey<DamageType> common(String path) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", path));
        }

        public static TagKey<DamageType> tag(String path) {
            return WaywardTags.tag(Registries.DAMAGE_TYPE, path);
        }
    }

    private static <T> TagKey<T> tag(ResourceKey<Registry<T>> registry, String path) {
        if (path.contains(":")) {
            return TagKey.create(registry, ResourceLocation.parse(path));
        }
        return TagKey.create(registry, WaywardAttributes.path(path));
    }
}