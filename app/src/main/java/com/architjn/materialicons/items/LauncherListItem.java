package com.architjn.materialicons.items;

/**
 * Created by architjn on 27/07/15.
 */
public class LauncherListItem {
    public final String name;
    public final String packageName;
    private boolean isInstalled = false;

    public LauncherListItem(String[] values, boolean isInstalled) {
        name = values[0];
        packageName = values[1];
        this.isInstalled = isInstalled;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isInstalled() {
        return isInstalled;
    }
}
