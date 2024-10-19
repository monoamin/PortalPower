package net.monoamin.portalpower.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "portalpower");

    public static final RegistryObject<PortalFrameBlock> PORTAL_FRAME = BLOCKS.register("portal_frame", PortalFrameBlock::new);
    public static final RegistryObject<ResonatorCoreBlock> PORTAL_CONTROLLER = BLOCKS.register("resonator_core", ResonatorCoreBlock::new);
    public static final RegistryObject<LaserEmitterBlock> LASER_EMITTER = BLOCKS.register("laser_emitter", LaserEmitterBlock::new);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}