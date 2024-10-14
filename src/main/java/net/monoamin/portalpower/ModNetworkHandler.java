package net.monoamin.portalpower;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("portalpower", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static int nextId() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.registerMessage(nextId(), CustomTogglePacket.class, CustomTogglePacket::encode, CustomTogglePacket::decode, CustomTogglePacket::handle);
        // Add more packet registrations here if needed
    }
}
