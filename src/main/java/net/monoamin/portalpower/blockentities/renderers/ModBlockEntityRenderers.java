package net.monoamin.portalpower.blockentities.renderers;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.monoamin.portalpower.blockentities.ModBlockEntities;

public class ModBlockEntityRenderers {
    public static void registerBlockEntityRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.LASER_EMITTER.get(), LaserEmitterRenderer::new);
    }
}
