package net.monoamin.portalpower.menus;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "portalpower");

    public static final RegistryObject<MenuType<PortalControllerMenu>> PORTAL_CONTROLLER_MENU = MENUS.register("resonator_core_menu",
            () -> IForgeMenuType.create((windowId, inv, data) -> new PortalControllerMenu(windowId, inv, data))
    );
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
