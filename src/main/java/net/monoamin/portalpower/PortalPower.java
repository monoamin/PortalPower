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
import net.monoamin.portalpower.blocks.ModBlocks;
import net.monoamin.portalpower.items.ModItems;
import net.monoamin.portalpower.menus.ModMenus;
import net.monoamin.portalpower.screens.ModScreens;

@Mod(PortalPower.MODID)
public class PortalPower {
    public static final String MODID = "portalpower";
    public static final boolean DEBUG = true;

    /*
    // Create a DeferredRegister for blocks and items
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PortalPower.MODID);
/*
    // Register Blocks
    public static final RegistryObject<Block> LASER_BLOCK = BLOCKS.register("laser_block",
            () -> new LaserEmitterBlock(BlockBehaviour.Properties.of().strength(1.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final RegistryObject<Block> PORTAL_FRAME_BLOCK = BLOCKS.register("portal_frame",
            () -> new PortalFrameBlock(BlockBehaviour.Properties.of().strength(1.0f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
    public static final RegistryObject<Block> PORTAL_CONTROLLER_BLOCK = BLOCKS.register("portal_controller",
            () -> new PortalControllerBlock(BlockBehaviour.Properties.of().strength(1.0f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    // Register Items
    public static final RegistryObject<Item> LASER_BLOCK_ITEM = ITEMS.register("laser_block",
            () -> new BlockItem(LASER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PORTAL_FRAME_ITEM = ITEMS.register("portal_frame",
            () -> new BlockItem(PORTAL_FRAME_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PORTAL_CONTROLLER_ITEM = ITEMS.register("portal_controller",
            () -> new BlockItem(PORTAL_CONTROLLER_BLOCK.get(), new Item.Properties()));

*/
    // Register Creative Tab
    public static final RegistryObject<CreativeModeTab> PORTAL_POWER_CREATIVETAB = CREATIVE_MODE_TABS.register("portalpower", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.PORTAL_CONTROLLER.get()))  // Set an icon for the tab
                    .title(Component.translatable("itemGroup.portalpower"))   // Tab name translation
                    .displayItems((parameters, output) -> {
                        // Add the block to the tab
                        output.accept(ModItems.PORTAL_FRAME_ITEM.get());
                        output.accept(ModItems.PORTAL_CONTROLLER_ITEM.get());
                    })
                    .build()
    );


    public PortalPower() {
        PortalPowerConfig.register();
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    @Mod.EventBusSubscriber(modid = "portalpower", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientModEventSubscriber {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ModScreens::registerScreens);
        }
    }

    @Mod.EventBusSubscriber(modid = "portalpower", bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModEventSubscriber {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(ModNetworkHandler::register);
        }
    }
}