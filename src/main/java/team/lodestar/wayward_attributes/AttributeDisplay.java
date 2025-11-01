package team.lodestar.wayward_attributes;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;

import java.util.*;

public record AttributeDisplay(Holder<Attribute> attribute, ResourceLocation texture, List<TagKey<Item>> tags) {

    public static final Codec<AttributeDisplay> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(AttributeDisplay::attribute),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(AttributeDisplay::texture),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("tags", Collections.emptyList()).forGetter(AttributeDisplay::tags)
    ).apply(instance, AttributeDisplay::new));


    public static AttributeDisplay findMatching(ItemStack stack, Holder<Attribute> attribute) {
        AttributeDisplay fallback = null;
        for (AttributeDisplay data : AttributeDisplayDataReloadListener.DISPLAY_DATA) {
            if (!data.attribute().equals(attribute)) {
                continue;
            }
            if (data.tags.isEmpty()) {
                fallback = data;
                continue;
            }
            boolean matches = data.tags.stream().anyMatch(stack::is);
            if (matches) {
                return data;
            }
        }
        return fallback;
    }
}