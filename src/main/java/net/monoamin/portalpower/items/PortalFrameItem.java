package net.monoamin.portalpower.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.monoamin.portalpower.blocks.PortalControllerBlock;
import net.monoamin.portalpower.blocks.PortalFrameBlock;

public class PortalFrameItem extends BlockItem {
    public PortalFrameItem(PortalFrameBlock block) {
        super(block, new Item.Properties());
    }
}
