package net.monoamin.portalpower.items;

import net.monoamin.portalpower.blocks.PortalControllerBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

public class PortalControllerItem extends BlockItem {
    public PortalControllerItem(PortalControllerBlock block) {
        super(block, new Item.Properties());
    }
}