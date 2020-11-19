package com.willfp.ecoenchants.integrations.anvilgui.plugins;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.integrations.anvilgui.AnvilGUIIntegration;

public class AnvilGUIImpl implements AnvilGUIIntegration {
    private static Class<?> ANVIL_GUI_CLASS = null;

    @Override
    public boolean isInstance(Object object) {
        assert ANVIL_GUI_CLASS != null;
        return object.getClass().equals(ANVIL_GUI_CLASS);
    }

    @Override
    public String getPluginName() {
        return "AnvilGUI";
    }

    static {
        try {
            ANVIL_GUI_CLASS = Class.forName("net.wesjd.anvilgui.version.Wrapper" + EcoEnchantsPlugin.NMS_VERSION.substring(1) + "$AnvilContainer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
