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
            int s = ((Season.SubSeason) subSeason).ordinal();
            if (DayLight.DEBUG)
                LogManager.getLogger().debug("####DAYLIGHT####:  Season changed to {}", s);
            double r = getDayNightRatio(DayLightConfig.latitude.get(), s);
            setDayNightSpeed(0+r,2-r);
        }
    }

    private static void setDayNightSpeed(double dayFactor, double nightFactor) {
        HourglassConfig.SERVER_CONFIG.daySpeed.set(dayFactor);
        HourglassConfig.SERVER_CONFIG.nightSpeed.set(nightFactor);
    }

    private static double toRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    // Function to calculate day/night length ratio based on latitude and month
    public static double getDayNightRatio(double latitude, int month) {
        // Ensure month is within valid range (0 to 11)
        if (month < 0) month = 0;
        if (month > 11) month = 11;

        // Convert latitude and Earth's tilt to radians
        double latRadians = toRadians(DayLightConfig.latitude.get());
        double tiltRadians = toRadians(DayLightConfig.axialTilt.get());

        // Approximate the solar declination angle based on the month
        // Month 0 (early spring) corresponds to the spring equinox, declination of 0.
        double hourAngle = getHourAngle(month, tiltRadians, latRadians);
        double daylightHours = 24.0 * (hourAngle / Math.PI);

        // Return the ratio of daylight to night hours
        double nightHours = 24.0 - daylightHours;
        return daylightHours / nightHours;
    }

    private static double getHourAngle(int month, double tiltRadians, double latRadians) {
        double dayOfYear = month * (365 / 12.0); // Roughly map month to day of the year
        double solarDeclination = tiltRadians * Math.sin(2 * Math.PI * (dayOfYear - 80) / 365);

        // Calculate the cosine of the hour angle for sunrise/sunset
        double cosHourAngle = -Math.tan(latRadians) * Math.tan(solarDeclination);

        // Limit cosHourAngle to [-1, 1] to avoid domain errors in acos
        cosHourAngle = Math.max(-1, Math.min(1, cosHourAngle));

        // Calculate the hour angle (in radians) and convert to hours of daylight
        double hourAngle = Math.acos(cosHourAngle);
        return hourAngle;
    }
}