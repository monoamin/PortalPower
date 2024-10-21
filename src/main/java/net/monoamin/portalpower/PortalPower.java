package net.monoamin.portalpower;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.monoamin.portalpower.blockentities.ModBlockEntities;
import net.monoamin.portalpower.blockentities.renderers.ModBlockEntityRenderers;
import net.monoamin.portalpower.blocks.ModBlocks;
import net.monoamin.portalpower.items.ModItems;
import net.monoamin.portalpower.menus.ModMenus;
import net.monoamin.portalpower.network.ModMessages;
import net.monoamin.portalpower.screens.ModScreens;

@Mod(PortalPower.MODID)
public class PortalPower {
    public static final String MODID = "portalpower";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PortalPower.MODID);

    // Register Creative Tab
    public static final RegistryObject<CreativeModeTab> PORTAL_POWER_CREATIVETAB = CREATIVE_MODE_TABS.register("portalpower", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.RESONATOR_CORE.get()))  // Set an icon for the tab
                    .title(Component.translatable("itemGroup.portalpower"))   // Tab name translation
                    .displayItems((parameters, output) -> {
                        // Add the block to the tab
                        output.accept(ModItems.PORTAL_FRAME_ITEM.get());
                        output.accept(ModItems.PORTAL_CONTROLLER_ITEM.get());
                        output.accept(ModItems.LASER_EMITTER_ITEM.get());
                    })
                    .build()
    );

    public PortalPower() {
        PortalPowerConfig.register();
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = "portalpower", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientModEventSubscriber {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ModScreens::registerScreens);
            event.enqueueWork(ModBlockEntityRenderers::registerBlockEntityRenderers);
        }
    }

    @Mod.EventBusSubscriber(modid = "portalpower", bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModEventSubscriber {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(ModMessages::register);
        }
    }
}