package team.lodestar.wayward_attributes.registry;

import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.resources.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.*;
import net.neoforged.neoforge.network.registration.*;
import org.jetbrains.annotations.*;
import team.lodestar.wayward_attributes.WaywardAttributes;
import team.lodestar.wayward_attributes.network.SyncReloadListenerDataPayload;

import java.util.*;

public class WaywardPayloadTypes {

    public static TemporaryLodestonePayloadRegistryHelper WAYWARD_ATTRIBUTES_CHANNEL = new TemporaryLodestonePayloadRegistryHelper(WaywardAttributes.MODID);

    public static void registerNetworkStuff(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        WAYWARD_ATTRIBUTES_CHANNEL.playToClient(registrar, "sync_reload_listener_data", SyncReloadListenerDataPayload.class, SyncReloadListenerDataPayload::deserialize);
    }

    public static abstract class TemporaryLodestoneNetworkPayloadData implements CustomPacketPayload {

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TemporaryLodestonePayloadRegistryHelper.PAYLOAD_TO_TYPE.get(getClass());
        }

        public abstract void serialize(RegistryFriendlyByteBuf byteBuf);
    }

    public static abstract class TemporaryOneSidedPayloadData extends TemporaryLodestoneNetworkPayloadData {

        public abstract void handle(final IPayloadContext context);
    }
    public record TemporaryLodestonePayloadRegistryHelper(String namespace) {

        public static final HashMap<Class<? extends TemporaryLodestoneNetworkPayloadData>, CustomPacketPayload.Type<? extends TemporaryLodestoneNetworkPayloadData>> PAYLOAD_TO_TYPE = new HashMap<>();

        public <T extends TemporaryOneSidedPayloadData> void playToClient(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
            var type = createPayloadType(clazz, name);
            var codec = createStreamCodec(decoder);
            registrar.playToClient(type, codec, TemporaryOneSidedPayloadData::handle);
        }

        public <T extends TemporaryLodestoneNetworkPayloadData> StreamCodec<RegistryFriendlyByteBuf, T> createStreamCodec(PayloadDataSupplier<T> supplier) {
            return StreamCodec.ofMember(serializePayload(), deserializePayload(supplier));
        }

        public <B extends RegistryFriendlyByteBuf, T extends TemporaryLodestoneNetworkPayloadData> StreamMemberEncoder<B, T> serializePayload() {
            return TemporaryLodestoneNetworkPayloadData::serialize;
        }

        public <B extends RegistryFriendlyByteBuf, T extends TemporaryLodestoneNetworkPayloadData> StreamDecoder<B, T> deserializePayload(PayloadDataSupplier<T> supplier) {
            return byteBuf -> {
                try {
                    return supplier.deserializePayload(byteBuf);
                } catch (Exception e) {
                    throw new RuntimeException("Couldn't decode payload type from channel " + namespace, e);
                }
            };
        }

        public <T extends TemporaryLodestoneNetworkPayloadData> CustomPacketPayload.Type<T> createPayloadType(Class<T> clazz, String id) {
            CustomPacketPayload.Type<T> type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(namespace, id));
            PAYLOAD_TO_TYPE.put(clazz, type);
            return type;
        }

    }

    public interface PayloadDataSupplier<T extends TemporaryLodestoneNetworkPayloadData> {
        T deserializePayload(RegistryFriendlyByteBuf byteBuf);
    }
}