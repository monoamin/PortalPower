package net.monoamin.daylight;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DayLight.MODID)
public class CommandScheduler {
    private static final int INTERVAL = 500; // 500 ticks = 25 seconds (20 ticks = 1 second)
    private static int tickCounter = 0;
    private static MinecraftServer server;

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCounter++;
            if (tickCounter >= INTERVAL) {
                tickCounter = 0;
                String result = executeServerCommand("/season get");
                // Process the result

                if (DayLight.seasonStatus.initialized) {
                    if (DayLight.seasonStatus.season != 1.1f && result.contains("Early Spring")) {
                        DayLight.seasonStatus.season = 1.1f;
                        executeServerCommand("/hourglass config daySpeed=0.6");
                        executeServerCommand("/hourglass config nightSpeed=0.4");
                    } else if (DayLight.seasonStatus.season != 1.2f && result.contains("Mid Spring")) {
                        DayLight.seasonStatus.season = 1.2f;
                        logToServerChat(String.valueOf(DayLight.seasonStatus.season));
                        executeServerCommand("/hourglass config daySpeed=0.5");
                        executeServerCommand("/hourglass config nightSpeed=0.5");
                    } else if (DayLight.seasonStatus.season != 1.3f && result.contains("Late Spring")) {
                        DayLight.seasonStatus.season = 1.3f;
                        executeServerCommand("/hourglass config daySpeed=0.4");
                        executeServerCommand("/hourglass config nightSpeed=0.6");
                    }
                    else if (DayLight.seasonStatus.season != 2.1f && result.contains("Early Summer")) {
                        DayLight.seasonStatus.season = 2.1f;
                        executeServerCommand("/hourglass config daySpeed=0.35");
                        executeServerCommand("/hourglass config nightSpeed=0.65");
                    } else if (DayLight.seasonStatus.season != 2.2f && result.contains("Mid Summer")) {
                        DayLight.seasonStatus.season = 2.2f;
                        executeServerCommand("/hourglass config daySpeed=0.25");
                        executeServerCommand("/hourglass config nightSpeed=0.75");
                    } else if (DayLight.seasonStatus.season != 2.3f && result.contains("Late Summer")) {
                        DayLight.seasonStatus.season = 2.3f;
                        executeServerCommand("/hourglass config daySpeed=0.4");
                        executeServerCommand("/hourglass config nightSpeed=0.6");
                    }
                    else if (DayLight.seasonStatus.season != 3.1f && result.contains("Early Fall")) {
                        DayLight.seasonStatus.season = 3.1f;
                        executeServerCommand("/hourglass config daySpeed=0.5");
                        executeServerCommand("/hourglass config nightSpeed=0.5");
                    } else if (DayLight.seasonStatus.season != 3.2f && result.contains("Mid Fall")) {
                        DayLight.seasonStatus.season = 3.2f;
                        executeServerCommand("/hourglass config daySpeed=0.55");
                        executeServerCommand("/hourglass config nightSpeed=0.45");
                    } else if (DayLight.seasonStatus.season != 3.3f && result.contains("Late Fall")) {
                        DayLight.seasonStatus.season = 3.3f;
                        executeServerCommand("/hourglass config daySpeed=0.65");
                        executeServerCommand("/hourglass config nightSpeed=0.35");
                    }
                    else if (DayLight.seasonStatus.season != 4.1f && result.contains("Early Winter")) {
                        DayLight.seasonStatus.season = 4.1f;
                        executeServerCommand("/hourglass config daySpeed=0.7");
                        executeServerCommand("/hourglass config nightSpeed=0.3");
                    } else if (DayLight.seasonStatus.season != 4.2f && result.contains("Mid Winter")) {
                        DayLight.seasonStatus.season = 4.2f;
                        executeServerCommand("/hourglass config daySpeed=0.75");
                        executeServerCommand("/hourglass config nightSpeed=0.25");
                    } else if (DayLight.seasonStatus.season != 4.3f && result.contains("Late Winter")) {
                        DayLight.seasonStatus.season = 4.3f;
                        executeServerCommand("/hourglass config daySpeed=0.65");
                        executeServerCommand("/hourglass config nightSpeed=0.35");
                    }
                    else {
                        DayLight.seasonStatus.season = 0.0f;
                        executeServerCommand("/hourglass config daySpeed=0.5");
                        executeServerCommand("/hourglass config nightSpeed=0.5");
                        logToServerChat("unable to parse season");
                    }
                }
                logToServerChat(String.valueOf(DayLight.seasonStatus.season));
                executeServerCommand(String.valueOf(DayLight.seasonStatus.season));

            }
        }
    }

    private static String executeServerCommand(String command) {
        if (server != null) {
            CapturingCommandSource source = new CapturingCommandSource(server);
            server.getCommands().performPrefixedCommand(source, command);
            return String.join("\n", source.getOutput());
        }
        return null;
    }

    private static void logToServerChat(String command) {
        if (server != null) {
            CommandSourceStack source = server.createCommandSourceStack();
            server.getCommands().performPrefixedCommand(source, command);
        }
    }
}