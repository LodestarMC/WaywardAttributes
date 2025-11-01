package team.lodestar.wayward_attributes.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import team.lodestar.wayward_attributes.*;

import java.util.function.Supplier;

public class WaywardAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WaywardAttributes.MODID);

    public static final Supplier<AttachmentType<DrawSpeedData>> DRAW_SPEED_DATA = ATTACHMENT_TYPES.register(
            "draw_speed_data", () -> AttachmentType.builder(() -> new DrawSpeedData())
                    .serialize(DrawSpeedData.CODEC).build());
}
