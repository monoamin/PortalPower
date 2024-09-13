package net.monoamin.daylight;

import net.minecraftforge.common.ForgeConfigSpec;

public class DayLightConfig {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue overallScaling;
    public static ForgeConfigSpec.DoubleValue ratioEarlySpring;
    public static ForgeConfigSpec.DoubleValue ratioMidSpring;
    public static ForgeConfigSpec.DoubleValue ratioLateSpring;
    public static ForgeConfigSpec.DoubleValue ratioEarlySummer;
    public static ForgeConfigSpec.DoubleValue ratioMidSummer;
    public static ForgeConfigSpec.DoubleValue ratioLateSummer;
    public static ForgeConfigSpec.DoubleValue ratioEarlyFall;
    public static ForgeConfigSpec.DoubleValue ratioMidFall;
    public static ForgeConfigSpec.DoubleValue ratioLateFall;
    public static ForgeConfigSpec.DoubleValue ratioEarlyWinter;
    public static ForgeConfigSpec.DoubleValue ratioMidWinter;
    public static ForgeConfigSpec.DoubleValue ratioLateWinter;

    static {
        COMMON_BUILDER.push("General");

        overallScaling = COMMON_BUILDER.comment("overallScaling")
                .defineInRange("scaling", 1.0, 0.50, 1.50);
        ratioEarlySpring = COMMON_BUILDER.comment("ratioEarlySpring")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioMidSpring = COMMON_BUILDER.comment("ratioMidSpring")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioLateSpring = COMMON_BUILDER.comment("ratioLateSpring")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioEarlySummer = COMMON_BUILDER.comment("ratioEarlySummer")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioMidSummer = COMMON_BUILDER.comment("ratioMidSummer")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioLateSummer = COMMON_BUILDER.comment("ratioLateSummer")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioEarlyFall = COMMON_BUILDER.comment("ratioEarlyFall")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioMidFall = COMMON_BUILDER.comment("ratioMidFall")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioLateFall = COMMON_BUILDER.comment("ratioLateFall")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioEarlyWinter = COMMON_BUILDER.comment("ratioEarlyWinter")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioMidWinter = COMMON_BUILDER.comment("ratioMidWinter")
                .defineInRange("ratio", 1.0, 0.50, 1.50);
        ratioLateWinter = COMMON_BUILDER.comment("ratioLateWinter")
                .defineInRange("ratio", 1.0, 0.50, 1.50);

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
