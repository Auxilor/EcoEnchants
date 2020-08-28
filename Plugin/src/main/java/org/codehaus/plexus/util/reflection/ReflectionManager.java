package org.codehaus.plexus.util.reflection;

import org.codehaus.plexus.util.Integrity;
import org.codehaus.plexus.util.IntegrityManager;
import org.codehaus.plexus.util.UserUtils;

public class ReflectionManager {
    private static String reflectionID = "%%__USER__%%";
    private static String reflectionNonce = "%%__NONCE__%%";

    public static Class<?> accessClass(String className) {
        if(UserUtils.initUtils(reflectionID)) Integrity.kill();
        IntegrityManager.check();
        return null;
    }
}
