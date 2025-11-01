package team.lodestar.wayward_attributes;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.slf4j.Logger;
import team.lodestar.wayward_attributes.registry.*;

@Mod(WaywardAttributes.MODID)
public class WaywardAttributes
{
    public static final String MODID = "wayward_attributes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WaywardAttributes(IEventBus modEventBus) {
        NeoForgeMod.enableMergedAttributeTooltips();

        WaywardAttributeTypes.ATTRIBUTES.register(modEventBus);
        WaywardAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
