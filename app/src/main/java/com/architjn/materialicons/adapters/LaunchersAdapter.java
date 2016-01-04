package com.architjn.materialicons.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.architjn.materialicons.PackageName;
import com.architjn.materialicons.R;
import com.architjn.materialicons.items.LauncherListItem;

import java.lang.reflect.Constructor;
import java.util.List;

public class LaunchersAdapter extends RecyclerView.Adapter<LaunchersAdapter.SimpleItemViewHolder> {

    private static final int HEADER_INSTALLED = 0;
    private static final int HEADER_NOTINSTALLED = 1;
    private static final int ITEM = 2;
    private final List<LauncherListItem> items;
    private int installed, notInstalled;
    private String intentString;
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private Context context;

    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        public TextView launcherName, launcherState, header;
        public ImageView launcherIcon;
        public View mainView;

        public SimpleItemViewHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.header_name);
            launcherName = (TextView) view.findViewById(R.id.launcher_item_name);
            launcherState = (TextView) view.findViewById(R.id.launcher_item_state);
            launcherIcon = (ImageView) view.findViewById(R.id.launcher_item_icon);
            mainView = view;
        }
    }

    public LaunchersAdapter(Context context, List<LauncherListItem> items, int installed) {
        this.context = context;
        this.items = items;
        this.installed = 0;
        this.notInstalled = installed + 1;
    }

    @Override
    public LaunchersAdapter.SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_INSTALLED || viewType == HEADER_NOTINSTALLED) {
            return new SimpleItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.launcher_header, parent, false));
        }
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.launcher_list_item, parent, false);
        return new SimpleItemViewHolder(itemView);
    }

    private int getPosition(int pos) {
        if (pos < notInstalled) {
            return pos - 1;
        } else if (pos > notInstalled) {
            return pos - 2;
        }
        return pos;
    }

    @Override
    public void onBindViewHolder(final SimpleItemViewHolder holder, final int position) {
        if (position == installed) {
            holder.header.setText("Installed");
            return;
        } else if (position == notInstalled) {
            holder.header.setText("Not Installed");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (items.get(getPosition(position)).isInstalled()) {
                holder.mainView.setElevation(dpToPx(2));
                holder.mainView.setBackgroundColor(0xffffffff);
            } else {
                holder.mainView.setElevation(0);
                holder.mainView.setBackgroundColor(ContextCompat.getColor(context, R.color.appBg));
            }
        }
        holder.launcherName.setText(items.get(getPosition(position)).getName());
        if (items.get(getPosition(position)).isInstalled()) {
            holder.launcherState.setText("Installed");
            holder.launcherState.setTextColor(Color.parseColor("#009900"));
        } else {
            holder.launcherState.setText("Not Installed");
            holder.launcherState.setTextColor(Color.parseColor("#990000"));
        }
        int iconResource = context.getResources().getIdentifier(
                "ic_" + items.get(getPosition(position)).name.toLowerCase().replace(" ", "_"),
                "drawable", context.getPackageName()
        );
        holder.launcherIcon.setImageResource(iconResource);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(getPosition(position)).name.equals("Google Now Launcher")) {
                    gnlDialog();
                } else if (items.get(getPosition(position)).isInstalled()) {
                    openLauncher(items.get(getPosition(position)).getName());
                } else {
                    openInPlayStore(items.get(getPosition(position)));
                }
            }
        });
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void openInPlayStore(final LauncherListItem launcher) {
        intentString = MARKET_URL + launcher.packageName;
        final String LauncherName = launcher.name;
        final String cmName = "CM Theme Engine";
        String dialogContent;

        if (LauncherName.equals(cmName)) {
            dialogContent = launcher.name + context.getResources().getString(R.string.cm_dialog_content);
            intentString = "http://download.cyanogenmod.org/";
        } else {
            dialogContent = launcher.name + context.getResources().getString(R.string.lni_content);
            intentString = MARKET_URL + launcher.packageName;
        }

        new MaterialDialog.Builder(context)
                .title(launcher.name + context.getResources().getString(R.string.lni_title))
                .content(dialogContent)
                .positiveText(R.string.lni_yes)
                .negativeText(R.string.lni_no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(intentString));
                        context.startActivity(intent);
                    }
                }).show();
    }


    private void openLauncher(String name) {

        final String className = PackageName.class.getPackage().getName().toString() + ".launchers."
                + Character.toUpperCase(name.charAt(0))
                + name.substring(1).toLowerCase().replace(" ", "").replace("launcher", "")
                + "Launcher";

        Class<?> cl = null;
        try {
            cl = Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e("LAUNCHER CLASS MISSING", "Launcher class for: '" + name + "' missing!");
        }
        if (cl != null) {
            Constructor<?> constructor = null;
            try {
                constructor = cl.getConstructor(Context.class);
            } catch (NoSuchMethodException e) {
                Log.e("LAUNCHER CLASS CONS",
                        "Launcher class for: '" + name + "' is missing a constructor!");
            }
            try {
                if (constructor != null)
                    constructor.newInstance(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void gnlDialog() {
        final String appLink = MARKET_URL + context.getResources().getString(R.string.extraapp);
        new MaterialDialog.Builder(context)
                .title(R.string.gnl_title)
                .content(R.string.gnl_content)
                .positiveText(R.string.lni_yes)
                .negativeText(R.string.lni_no)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  super.onPositive(dialog);
                                  Intent intent = new Intent(Intent.ACTION_VIEW);
                                  intent.setData(Uri.parse(appLink));
                                  context.startActivity(intent);
                              }
                          }
                ).show();
    }

    @Override
    public int getItemCount() {
        return this.items.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == installed) {
            return HEADER_INSTALLED;
        } else if (position == notInstalled) {
            return HEADER_NOTINSTALLED;
        }
        return ITEM;
    }
}

