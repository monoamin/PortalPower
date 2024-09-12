package net.monoamin.daylight;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
@Mod(DayLight.MODID)
public class DayLight {
    public static final String MODID = "daylight";
    public static final boolean DEBUG = true;

    public DayLight() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DayLightEvents.class);
    }
}