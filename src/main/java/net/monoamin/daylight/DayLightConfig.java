package net.monoamin.daylight;

import net.minecraftforge.common.ForgeConfigSpec;

public class DayLightConfig {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue latitude;
    public static ForgeConfigSpec.DoubleValue axialTilt;


    static {
        COMMON_BUILDER.push("General");

        latitude = COMMON_BUILDER.comment("latitude")
                .defineInRange("latitude", 45d, -80d, 80d);
        axialTilt = COMMON_BUILDER.comment("axialTilt")
                .defineInRange("axial_tilt", 23d, 0d, 45d);

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
