package net.monoamin.portalpower.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.monoamin.portalpower.network.packet.LaserEmitterSyncPacket;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraft.resources.ResourceLocation;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("portalpower", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        // Register LaserEmitterSyncPacket
        INSTANCE.messageBuilder(LaserEmitterSyncPacket.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(LaserEmitterSyncPacket::decode)
                .encoder(LaserEmitterSyncPacket::encode)
                .consumerMainThread(LaserEmitterSyncPacket::handle)
                .add();
    }

    // Utility method to send a packet to all players
    public static void sendToAllPlayers(Object message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}