package org.codehaus.plexus.util;

public class IntegrityManager {
    public static void check() {
        try {
            Integrity.runIntegrityCheck();
        } catch (Exception e) {
            Integrity.kill();
        }
    }
}
