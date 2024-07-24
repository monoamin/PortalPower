package net.monoamin.daylight;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CapturingCommandSource extends CommandSourceStack {
    private final List<String> output = new ArrayList<>();

    public CapturingCommandSource(MinecraftServer server) {
        super(server, Vec3.ZERO, Vec2.ZERO, server.overworld(), 4, "", Component.literal(""), server, null);
    }

    @Override
    public void sendSystemMessage(Component textComponent) {
        output.add(textComponent.getString());
        // Also print the message to the console
        System.out.println(textComponent.getString());
    }

    public List<String> getOutput() {
        return output;
    }
}