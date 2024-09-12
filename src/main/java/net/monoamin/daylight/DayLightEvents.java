package net.monoamin.daylight;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonChangedEvent;
import net.lavabucket.hourglass.config.HourglassConfig;

@Mod.EventBusSubscriber(modid = DayLight.MODID)
public class DayLightEvents {
    private static MinecraftServer server;
    private static final double b = 0.5;
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
    }
    @SubscribeEvent
    public static void onSeasonChanged(SeasonChangedEvent<?> e) {
        Object subSeason = e.getNewSeason();
        if (subSeason instanceof Season.SubSeason) {
            int s = ((Season.SubSeason)subSeason).ordinal();
            if (DayLight.DEBUG)
                LogManager.getLogger().debug("####DAYLIGHT####:  Season changed to " + s);
            if (s == 0) setDayNightSpeed(0.75 * b, 1.25 * b);
            if (s == 1) setDayNightSpeed(1.00 * b, 1.00 * b);
            if (s == 2) setDayNightSpeed(1.25 * b, 0.75 * b);
            if (s == 3) setDayNightSpeed(1.40 * b, 0.60 * b);
            if (s == 4) setDayNightSpeed(1.50 * b, 0.75 * b);
            if (s == 5) setDayNightSpeed(1.40 * b, 0.60 * b);
            if (s == 6) setDayNightSpeed(1.25 * b, 0.75 * b);
            if (s == 7) setDayNightSpeed(1.00 * b, 1.00 * b);
            if (s == 8) setDayNightSpeed(0.75 * b, 1.25 * b);
            if (s == 9) setDayNightSpeed(0.60 * b, 1.40 * b);
            if (s == 10) setDayNightSpeed(0.50 * b, 1.50 * b);
            if (s == 11) setDayNightSpeed(0.60 * b, 1.40 * b);
        }
    }
    private static void setDayNightSpeed(double dayFactor, double nightFactor) {
        HourglassConfig.SERVER_CONFIG.daySpeed.set(dayFactor);
        HourglassConfig.SERVER_CONFIG.nightSpeed.set(nightFactor);
    }
}