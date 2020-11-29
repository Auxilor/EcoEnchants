package com.willfp.ecoenchants.integrations.anvilgui;

import com.willfp.ecoenchants.integrations.Integration;

/**
 * Interface for AnvilGUI integrations
 */
public interface AnvilGUIIntegration extends Integration {
    /**
     * Get if the NMS inventory is an instance of an AnvilGUI
     *
     * @param object The NMS inventory to check
     * @return If the object is an AnvilGUI
     * @see com.willfp.ecoenchants.nms.OpenInventory
     */
    boolean isInstance(Object object);
}
