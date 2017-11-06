package kd.push.utils;

import android.os.Build.VERSION;

public class VersionUtils {
    private VersionUtils() {
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= 8;
    }

    public static boolean hasGingerbread() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean hasICE_CREAM_SANDWICH() {
        return VERSION.SDK_INT >= 14;
    }

    public static boolean hasHoneycombMR1() {
        return VERSION.SDK_INT >= 12;
    }

    public static boolean hasJellyBean() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean hasKitkat() {
        return VERSION.SDK_INT >= 19;
    }
}
