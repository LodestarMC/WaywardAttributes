package team.lodestar.wayward_attributes.core.data.listener;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;
import team.lodestar.lodestone.modules.toolkit.codec.LodestoneStreamCodecs;

import java.util.*;

public record AttributeDisplayData(Holder<Attribute> attribute, ResourceLocation texture,
                                   List<TagKey<Item>> tags, List<TagKey<Item>> blacklist, String componentRegex) {

    public static final Codec<AttributeDisplayData> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Attribute.CODEC.fieldOf("attribute").forGetter(AttributeDisplayData::attribute),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(AttributeDisplayData::texture),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("tags", Collections.emptyList()).forGetter(AttributeDisplayData::tags),
            TagKey.codec(Registries.ITEM).listOf().optionalFieldOf("blacklist", Collections.emptyList()).forGetter(AttributeDisplayData::blacklist),
            Codec.STRING.optionalFieldOf("componentRegex", "").forGetter(AttributeDisplayData::componentRegex)
    ).apply(instance, AttributeDisplayData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AttributeDisplayData> STREAM_CODEC =
            StreamCodec.composite(
                    Attribute.STREAM_CODEC, AttributeDisplayData::attribute,
                    ResourceLocation.STREAM_CODEC, AttributeDisplayData::texture,
                    LodestoneStreamCodecs.tagStreamCodec(Registries.ITEM).apply(ByteBufCodecs.list()), AttributeDisplayData::tags,
                    LodestoneStreamCodecs.tagStreamCodec(Registries.ITEM).apply(ByteBufCodecs.list()), AttributeDisplayData::blacklist,
                    ByteBufCodecs.STRING_UTF8, AttributeDisplayData::componentRegex,
                    AttributeDisplayData::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<AttributeDisplayData>> LIST_STREAM_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());

    @SuppressWarnings("DataFlowIssue")
    public static AttributeDisplayData findMatching(ItemStack stack, String component) {
        var trimmed = component.substring(component.indexOf("name.")+5);

        var consideredData = new ArrayList<AttributeDisplayData>();

        for (AttributeDisplayData data : AttributeDisplayDataReloadListener.DISPLAY_DATA) {
            var attribute = data.attribute;
            var id = attribute.getKey().location();
            var modid = id.getNamespace();
            if (!modid.equals("minecraft")) {
                if (!component.contains(modid)) {
                    continue;
                }
            }
            var regex = data.componentRegex;
            if (regex.isEmpty()) { //Default Behavior
                if (component.contains(modid) && component.contains(id.getPath())) {
                    consideredData.add(data);
                    continue;
                }

                var attributeId = ResourceLocation.fromNamespaceAndPath(modid, trimmed);
                var parsed = getAttribute(attributeId);
                if (parsed == null) {
                    var split = trimmed.split("\\.");
                    var last = split[split.length-1];
                    attributeId = ResourceLocation.fromNamespaceAndPath(modid, last);
                    parsed = getAttribute(attributeId);
                }
                if (attribute.equals(parsed)) {
                    consideredData.add(data);
                }
            }
            else {
                if (trimmed.matches(regex)){
                    consideredData.add(data);
                }
            }
        }
        if (!consideredData.isEmpty()) {
            if (consideredData.size() == 1) {
                return consideredData.getFirst();
            }
            AttributeDisplayData fallback = null;
            for (AttributeDisplayData data : consideredData) {
                var tags = data.tags;
                if (tags.isEmpty()) {
                    fallback = data;
                }
                else {
                    boolean matches = tags.stream().anyMatch(stack::is);
                    if (!data.blacklist.isEmpty()) {
                        if (data.blacklist.stream().anyMatch(stack::is)) {
                            matches = false;
                        }
                    }
                    if (matches) {
                        return data;
                    }
                }
            }
            return fallback;
        }

        return null;
    }

    public static Holder<Attribute> getAttribute(@Nullable ResourceLocation id) {
        if (id == null) {
            return null;
        }
        var registryAccess = Minecraft.getInstance().level.registryAccess();
        var registry = registryAccess.registryOrThrow(Registries.ATTRIBUTE);
        var holder = registry.getHolder(ResourceKey.create(Registries.ATTRIBUTE, id));
        return holder.orElse(null);
    }
}