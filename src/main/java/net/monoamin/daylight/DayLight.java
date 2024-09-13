package net.monoamin.daylight;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DayLight.MODID)
public class DayLight {
    public static final String MODID = "daylight";
    public static final boolean DEBUG = true;

    public DayLight() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DayLightEvents.class);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoad);
    }

    private void setup(final FMLCommonSetupEvent event) {
        net.minecraftforge.common.ForgeConfigSpec commonSpec = DayLightConfig.COMMON_CONFIG;
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
    }

    private void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() == DayLightConfig.COMMON_CONFIG) {
            // React to changes or reload options if necessary
        }
    }
}