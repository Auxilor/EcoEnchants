package com.willfp.ecoenchants.integrations.anvilgui.plugins;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.integrations.anvilgui.AnvilGUIIntegration;

public class AnvilGUIImpl implements AnvilGUIIntegration {
    @Override
    public boolean isInstance(Object object) {
        try {
            return object.getClass().equals(Class.forName("net.wesjd.anvilgui.version.Wrapper" + EcoEnchantsPlugin.NMS_VERSION.substring(1) + "$AnvilContainer"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getPluginName() {
        return "AnvilGUI";
    }
}
