package team.lodestar.wayward_attributes;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.item.*;

public class ModTags {

    public static class ItemTags {

        public static final TagKey<Item> AFFECTED_BY_DRAW_SPEED = tag("affected_by_draw_speed");

        public static TagKey<Item> tag(String path) {
            return ModTags.tag(Registries.ITEM, path);
        }
    }

    private static <T> TagKey<T> tag(ResourceKey<Registry<T>> registry, String path) {
        if (path.contains(":")) {
            return TagKey.create(registry, ResourceLocation.parse(path));
        }
        return TagKey.create(registry, WaywardAttributes.path(path));
    }
}