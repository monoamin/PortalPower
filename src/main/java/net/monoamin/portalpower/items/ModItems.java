// src/main/java/com/yourmod/ModItems.java

package net.monoamin.portalpower.items;

import net.monoamin.portalpower.blocks.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "portalpower");

    public static final RegistryObject<Item> PORTAL_FRAME_ITEM = ITEMS.register("portal_frame",
            () -> new PortalFrameItem(ModBlocks.PORTAL_FRAME.get()));

    public static final RegistryObject<Item> LASER_EMITTER_ITEM = ITEMS.register("laser_emitter",
            () -> new LaserEmitterItem(ModBlocks.LASER_EMITTER.get()));

    public static final RegistryObject<Item> PORTAL_CONTROLLER_ITEM = ITEMS.register("resonator_core",
            () -> new PortalControllerItem(ModBlocks.PORTAL_CONTROLLER.get()));
}