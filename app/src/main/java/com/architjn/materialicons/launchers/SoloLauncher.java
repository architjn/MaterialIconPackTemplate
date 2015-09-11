package com.architjn.materialicons.launchers;

import android.content.Context;
import android.content.Intent;

import com.architjn.materialicons.R;


public class SoloLauncher {
    public SoloLauncher(Context context) {
        Intent soloApply = context.getPackageManager().getLaunchIntentForPackage("home.solo.launcher.free");
        Intent solo = new Intent("home.solo.launcher.free.APPLY_THEME");
        solo.putExtra("EXTRA_PACKAGENAME", context.getPackageName());
        solo.putExtra("EXTRA_THEMENAME", context.getString(R.string.theme_title));
        context.sendBroadcast(solo);
        context.startActivity(soloApply);
    }
}
