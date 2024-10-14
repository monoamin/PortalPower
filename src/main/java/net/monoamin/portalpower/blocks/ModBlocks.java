package net.monoamin.portalpower.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "portalpower");

    public static final RegistryObject<PortalFrameBlock> PORTAL_FRAME = BLOCKS.register("portal_frame", PortalFrameBlock::new);
    public static final RegistryObject<PortalControllerBlock> PORTAL_CONTROLLER = BLOCKS.register("portal_controller", PortalControllerBlock::new);
}