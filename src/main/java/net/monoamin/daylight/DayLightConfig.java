package net.monoamin.daylight;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class DayLightConfig {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue latitude;
    public static ForgeConfigSpec.DoubleValue axialTilt;
    public static ForgeConfigSpec.DoubleValue duration_factor;


    static {
        COMMON_BUILDER.push("General");

        latitude = COMMON_BUILDER.comment("latitude")
                .defineInRange("latitude", 45d, -80d, 80d);
        axialTilt = COMMON_BUILDER.comment("axialTilt")
                .defineInRange("axial_tilt", 23d, 0d, 45d);
        duration_factor = COMMON_BUILDER.comment("cycle_length")
                .defineInRange("duration_factor", 0.25d, 0.2d, 5d);

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }
}