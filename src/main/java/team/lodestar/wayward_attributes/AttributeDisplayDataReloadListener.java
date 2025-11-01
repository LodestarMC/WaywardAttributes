package team.lodestar.wayward_attributes;

import com.google.gson.*;
import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.neoforged.neoforge.event.*;

import java.util.*;

public class AttributeDisplayDataReloadListener extends SimpleJsonResourceReloadListener {

    public static final ArrayList<AttributeDisplay> DISPLAY_DATA = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().create();

    public AttributeDisplayDataReloadListener() {
        super(GSON, "wayward_data/attribute_display");
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new AttributeDisplayDataReloadListener());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        DISPLAY_DATA.clear();
        for (int i = 0; i < objectIn.size(); i++) {
            var location = (ResourceLocation) objectIn.keySet().toArray()[i];
            var array = objectIn.get(location).getAsJsonArray();
            var context = RegistryOps.create(JsonOps.INSTANCE, getRegistryLookup());
            for (JsonElement jsonElement : array) {
                try {
                    var display = AttributeDisplay.DIRECT_CODEC.parse(context, jsonElement).getOrThrow(JsonParseException::new);
                    DISPLAY_DATA.add(display);
                } catch (JsonParseException exception) {
                    WaywardAttributes.LOGGER.info("Could not parse Attribute Display Definition: ", exception);
                }
            }
        }
    }
}