package net.monoamin.portalpower;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.monoamin.portalpower.blockentities.PortalControllerBlockEntity;

import java.util.function.Supplier;

public class CustomTogglePacket {
    private final BlockPos pos;

    public CustomTogglePacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(CustomTogglePacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static CustomTogglePacket decode(FriendlyByteBuf buf) {
        return new CustomTogglePacket(buf.readBlockPos());
    }

    public static void handle(CustomTogglePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.level().getBlockEntity(msg.pos) instanceof PortalControllerBlockEntity blockEntity) {
                blockEntity.toggle();  // Toggles the BlockEntity's state on/off
                blockEntity.setChanged(); // Mark block entity as changed
            }
        });
        ctx.get().setPacketHandled(true);
    }
}