package com.architjn.materialicons.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.architjn.materialicons.R;
import com.architjn.materialicons.items.LauncherListItem;

import java.lang.reflect.Constructor;
import java.util.List;

public class LaunchersAdapter extends RecyclerView.Adapter<LaunchersAdapter.SimpleItemViewHolder> {

    private final List<LauncherListItem> items;
    private String intentString;
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private Context context;

    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        public TextView launcherName, launcherState;
        public ImageView launcherIcon;
        public View mainView;

        public SimpleItemViewHolder(View view) {
            super(view);

            launcherName = (TextView) view.findViewById(R.id.launcher_item_name);
            launcherState = (TextView) view.findViewById(R.id.launcher_item_state);
            launcherIcon = (ImageView) view.findViewById(R.id.launcher_item_icon);
            mainView = view;
        }
    }

    public LaunchersAdapter(Context context, List<LauncherListItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public LaunchersAdapter.SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.launcher_list_item, parent, false);


        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleItemViewHolder holder, final int position) {

        holder.launcherName.setText(items.get(position).getName());
        if (items.get(position).isInstalled()) {
            holder.launcherState.setText("Installed");
            holder.launcherState.setTextColor(Color.parseColor("#009900"));
        } else {
            holder.launcherState.setText("Not Installed");
            holder.launcherState.setTextColor(Color.parseColor("#990000"));
        }
        int iconResource = context.getResources().getIdentifier(
                "ic_" + items.get(position).name.toLowerCase().replace(" ", "_"),
                "drawable", context.getPackageName()
        );
        holder.launcherIcon.setImageResource(iconResource);
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items.get(position).name.equals("Google Now Launcher")) {
                    gnlDialog();
                } else if (items.get(position).isInstalled()) {
                    openLauncher(items.get(position).getName());
                } else {
                    openInPlayStore(items.get(position));
                }
            }
        });
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

        final String className = "com.architjn.materialicons" + ".launchers."
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
        return this.items.size();
    }
}

