package com.willfp.ecoenchants.integrations.anvilgui.plugins;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.integrations.anvilgui.AnvilGUIIntegration;

public class AnvilGUIImpl implements AnvilGUIIntegration {
    private static final String ANVIL_GUI_CLASS = "net.wesjd.anvilgui.version.Wrapper" + EcoEnchantsPlugin.NMS_VERSION.substring(1) + "$AnvilContainer";

    @Override
    public boolean isInstance(Object object) {
        return object.getClass().toString().equals(ANVIL_GUI_CLASS);
    }

    @Override
    public String getPluginName() {
        return "AnvilGUI";
    }
}
