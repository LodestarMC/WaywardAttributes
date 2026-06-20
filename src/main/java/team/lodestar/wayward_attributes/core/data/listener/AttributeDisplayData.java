package team.lodestar.wayward_attributes.core.data.listener;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import team.lodestar.lodestone.modules.toolkit.codec.LodestoneStreamCodecs;

import java.util.*;

public record AttributeDisplayData(Holder<Attribute> attribute, ResourceLocation texture, List<TagKey<Item>> tags, List<TagKey<Item>> blacklist) {

    public static final Codec<AttributeDisplayData> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(AttributeDisplayData::attribute),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(AttributeDisplayData::texture),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("tags", Collections.emptyList()).forGetter(AttributeDisplayData::tags),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("blacklist", Collections.emptyList()).forGetter(AttributeDisplayData::blacklist)
    ).apply(instance, AttributeDisplayData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AttributeDisplayData> STREAM_CODEC =
            StreamCodec.composite(
                    Attribute.STREAM_CODEC, AttributeDisplayData::attribute,
                    ResourceLocation.STREAM_CODEC, AttributeDisplayData::texture,
                    LodestoneStreamCodecs.tagStreamCodec(Registries.ITEM).apply(ByteBufCodecs.list()), AttributeDisplayData::tags,
                    LodestoneStreamCodecs.tagStreamCodec(Registries.ITEM).apply(ByteBufCodecs.list()), AttributeDisplayData::blacklist,
                    AttributeDisplayData::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<AttributeDisplayData>> LIST_STREAM_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());

    public static AttributeDisplayData findMatching(ItemStack stack, Holder<Attribute> attribute) {
        AttributeDisplayData fallback = null;
        for (AttributeDisplayData data : AttributeDisplayDataReloadListener.DISPLAY_DATA) {
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