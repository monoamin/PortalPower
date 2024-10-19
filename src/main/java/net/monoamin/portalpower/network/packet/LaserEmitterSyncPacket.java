package net.monoamin.portalpower.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.monoamin.portalpower.blockentities.LaserEmitterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class LaserEmitterSyncPacket {
    private final BlockPos pos;
    private final int energyLevel;
    private final boolean isActive;

    public LaserEmitterSyncPacket(BlockPos pos, int energyLevel, boolean isActive) {
        this.pos = pos;
        this.energyLevel = energyLevel;
        this.isActive = isActive;
    }

    // Encode the packet data to be sent to the client
    public static void encode(LaserEmitterSyncPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeInt(packet.energyLevel);
        buf.writeBoolean(packet.isActive);
    }

    // Decode the packet data received from the server
    public static LaserEmitterSyncPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int energyLevel = buf.readInt();
        boolean isActive = buf.readBoolean();
        return new LaserEmitterSyncPacket(pos, energyLevel, isActive);
    }

    // Handle the packet on the client side
    public static void handle(LaserEmitterSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(packet.pos);
            if (blockEntity instanceof LaserEmitterBlockEntity laserEmitterBlockEntity) {
                laserEmitterBlockEntity.setCurrentEnergyLevel(packet.energyLevel);
                laserEmitterBlockEntity.setActive(packet.isActive);
            }
        });
        context.setPacketHandled(true);
    }
}
