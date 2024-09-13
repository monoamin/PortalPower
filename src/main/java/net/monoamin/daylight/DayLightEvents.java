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
            double dayFactor = 1.0;
            double nightFactor = 1.0;

            switch (s){
                case 0:
                    dayFactor = DayLightConfig.ratioEarlySpring.get();
                    nightFactor = 2-DayLightConfig.ratioEarlySpring.get();
                case 1:
                    dayFactor = DayLightConfig.ratioMidSpring.get();
                    nightFactor = 2-DayLightConfig.ratioMidSpring.get();
                case 2:
                    dayFactor = DayLightConfig.ratioLateSpring.get();
                    nightFactor = 2-DayLightConfig.ratioLateSpring.get();
                case 3:
                    dayFactor = DayLightConfig.ratioEarlySummer.get();
                    nightFactor = 2-DayLightConfig.ratioEarlySummer.get();
                case 4:
                    dayFactor = DayLightConfig.ratioMidSummer.get();
                    nightFactor = 2-DayLightConfig.ratioMidSummer.get();
                case 5:
                    dayFactor = DayLightConfig.ratioLateSummer.get();
                    nightFactor = 2-DayLightConfig.ratioLateSummer.get();
                case 6:
                    dayFactor = DayLightConfig.ratioEarlyFall.get();
                    nightFactor = 2-DayLightConfig.ratioEarlyFall.get();
                case 7:
                    dayFactor = DayLightConfig.ratioMidFall.get();
                    nightFactor = 2-DayLightConfig.ratioMidFall.get();
                case 8:
                    dayFactor = DayLightConfig.ratioLateFall.get();
                    nightFactor = 2-DayLightConfig.ratioLateFall.get();
                case 9:
                    dayFactor = DayLightConfig.ratioEarlyWinter.get();
                    nightFactor = 2-DayLightConfig.ratioEarlyWinter.get();
                case 10:
                    dayFactor = DayLightConfig.ratioMidWinter.get();
                    nightFactor = 2-DayLightConfig.ratioMidWinter.get();
                case 11:
                    dayFactor = DayLightConfig.ratioLateWinter.get();
                    nightFactor = 2-DayLightConfig.ratioLateWinter.get();
            }
            double overallScaling = DayLightConfig.overallScaling.get();
            if (s == 0) setDayNightSpeed(dayFactor * overallScaling, nightFactor * overallScaling);
        }
    }
    private static void setDayNightSpeed(double dayFactor, double nightFactor) {
        HourglassConfig.SERVER_CONFIG.daySpeed.set(dayFactor);
        HourglassConfig.SERVER_CONFIG.nightSpeed.set(nightFactor);
    }
}