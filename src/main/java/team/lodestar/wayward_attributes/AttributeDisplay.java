package team.lodestar.wayward_attributes;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import team.lodestar.lodestone.network.ExtraStreamCodecs;

import java.util.*;

public record AttributeDisplay(Holder<Attribute> attribute, ResourceLocation texture, List<TagKey<Item>> tags, List<TagKey<Item>> blacklist) {

    public static final Codec<AttributeDisplay> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(AttributeDisplay::attribute),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(AttributeDisplay::texture),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("tags", Collections.emptyList()).forGetter(AttributeDisplay::tags),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("blacklist", Collections.emptyList()).forGetter(AttributeDisplay::blacklist)
    ).apply(instance, AttributeDisplay::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AttributeDisplay> STREAM_CODEC =
            StreamCodec.composite(
                    Attribute.STREAM_CODEC, AttributeDisplay::attribute,
                    ResourceLocation.STREAM_CODEC, AttributeDisplay::texture,
                    ExtraStreamCodecs.tagStreamCodec(Registries.ITEM).apply(ByteBufCodecs.list()), AttributeDisplay::tags,
                    ExtraStreamCodecs.tagStreamCodec(Registries.ITEM).apply(ByteBufCodecs.list()), AttributeDisplay::blacklist,
                    AttributeDisplay::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<AttributeDisplay>> LIST_STREAM_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());

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
            if (!data.blacklist.isEmpty()) {
                if (data.blacklist.stream().anyMatch(stack::is)) {
                    matches = false;
                }
            }
            if (matches) {
                return data;
            }
        }
        return fallback;
    }
}