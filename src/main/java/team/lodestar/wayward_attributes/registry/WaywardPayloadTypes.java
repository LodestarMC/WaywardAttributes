package team.lodestar.wayward_attributes.registry;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import team.lodestar.lodestone.registry.common.LodestoneNetworkPayloads;
import team.lodestar.wayward_attributes.WaywardAttributes;
import team.lodestar.wayward_attributes.network.SyncReloadListenerDataPayload;

public class WaywardPayloadTypes {

    public static LodestoneNetworkPayloads.LodestonePayloadRegistryHelper WAYWARD_ATTRIBUTES_CHANNEL = new LodestoneNetworkPayloads.LodestonePayloadRegistryHelper(WaywardAttributes.MODID);

    public static void registerNetworkStuff(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        WAYWARD_ATTRIBUTES_CHANNEL.playToClient(registrar, "sync_reload_listener_data", SyncReloadListenerDataPayload.class, SyncReloadListenerDataPayload::deserialize);
    }
}