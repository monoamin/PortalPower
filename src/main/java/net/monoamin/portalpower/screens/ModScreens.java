package net.monoamin.portalpower.screens;

import net.minecraft.client.gui.screens.MenuScreens;
import net.monoamin.portalpower.menus.ModMenus;

public class ModScreens {
    public static void registerScreens() {
        MenuScreens.register(ModMenus.PORTAL_CONTROLLER_MENU.get(), PortalControllerScreen::new);
    }
}
