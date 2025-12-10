package team.lodestar.wayward_attributes.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.wayward_attributes.AttributeDisplay;
import team.lodestar.wayward_attributes.AttributeDisplayDataReloadListener;

import java.util.List;

public class SyncReloadListenerDataPayload extends OneSidedPayloadData {

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncReloadListenerDataPayload> STREAM_CODEC =
            StreamCodec.composite(
                    AttributeDisplay.LIST_STREAM_CODEC, p -> p.attributeDisplays,
                    SyncReloadListenerDataPayload::new
            );

    private final List<AttributeDisplay> attributeDisplays;

    public SyncReloadListenerDataPayload() {
        this(AttributeDisplayDataReloadListener.DISPLAY_DATA);
    }

    public SyncReloadListenerDataPayload(List<AttributeDisplay> attributeDisplays) {
        this.attributeDisplays = attributeDisplays;
    }

    public static SyncReloadListenerDataPayload deserialize(RegistryFriendlyByteBuf buf) {
        return STREAM_CODEC.decode(buf);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        STREAM_CODEC.encode(buf, this);
    }

    @Override
    public void handle(IPayloadContext context) {
        var clientDisplays = AttributeDisplayDataReloadListener.DISPLAY_DATA;
        clientDisplays.clear();
        clientDisplays.addAll(attributeDisplays);
    }
}