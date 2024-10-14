// src/main/java/com/yourmod/ModBlockEntities.java

package net.monoamin.portalpower.blockentities;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.monoamin.portalpower.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "portalpower");

    public static final RegistryObject<BlockEntityType<PortalFrameBlockEntity>> PORTAL_FRAME = BLOCK_ENTITIES.register("portal_frame",
            () -> BlockEntityType.Builder.of(PortalFrameBlockEntity::new, ModBlocks.PORTAL_FRAME.get(), Blocks.OBSIDIAN).build(null));

    public static final RegistryObject<BlockEntityType<PortalControllerBlockEntity>> PORTAL_CONTROLLER = BLOCK_ENTITIES.register("portal_controller",
            () -> BlockEntityType.Builder.of(PortalControllerBlockEntity::new, ModBlocks.PORTAL_CONTROLLER.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}