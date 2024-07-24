package net.monoamin.daylight;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DayLight.MODID)
public class DayLight {
    public static final String MODID = "daylight";
    public static SeasonStatus seasonStatus = new SeasonStatus();

    public DayLight() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(CommandScheduler.class);
        seasonStatus.initialized = true;
        seasonStatus.season = 0;
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some setup code
    }
}