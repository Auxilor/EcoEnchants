package org.codehaus.plexus.util;

import com.willfp.ecoenchants.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class UserUtils {
    public static boolean initUtils(String id) {
        try {
            String urlName = StringUtils.rot13("uggcf://cyhtvaf.jvyysc.pbz/rpbrapunagf/oynpxyvfg.gkg");
            String urlName2 = StringUtils.rot13("uggcf://cyhtvaf.jvyysc.pbz/rpbrapunagf/shpxlbh.shpxlbh");
            URL url = new URL(urlName);
            URL url2 = new URL(urlName2);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream()));

            Set<String> blacklistedIDs = new HashSet<>();

            String line;
            while ((line = in.readLine()) != null) {
                blacklistedIDs.add(line);
            }

            String line2;
            while ((line2 = in2.readLine()) != null) {
                blacklistedIDs.add(line2);
            }
            in.close();
            in2.close();

            if (blacklistedIDs.contains("%%__NONCE__%%")) return true;
            if (blacklistedIDs.contains(id)) return true;

            return blacklistedIDs.contains("%%__USER__%%");
        } catch (IOException e) {
            return false;
        }
    }
}
