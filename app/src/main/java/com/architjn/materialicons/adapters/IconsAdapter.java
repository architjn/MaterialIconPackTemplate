package com.architjn.materialicons.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.architjn.materialicons.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.SimpleItemViewHolder> {

    private List<Integer> items;
    private int arrayId;
    private String[] iconsnames;
    private Context context;

    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public View mainView;

        public SimpleItemViewHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon_grid_iv);
            mainView = view;
        }
    }

    public IconsAdapter(Context context, int arrayId) {
        this.context = context;
        this.arrayId = arrayId;
        loadIcon();
    }

    @Override
    public IconsAdapter.SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.icon_grid, parent, false);
        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleItemViewHolder holder, final int position) {
        holder.icon.setImageResource(items.get(position));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogIconView = View.inflate(context, R.layout.dialog_icon, null);
                ImageView dialogIcon = (ImageView) dialogIconView.findViewById(R.id.dialogicon);
                dialogIcon.setImageResource(items.get(position));
                String name = iconsnames[position].toLowerCase(Locale.getDefault());
                new MaterialDialog.Builder(context)
                        .customView(dialogIconView, false)
                        .title(convertText(name))
                        .positiveText(R.string.close)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    private void loadIcon() {
        items = new ArrayList<>();
        final String packageName = context.getPackageName();
        addIcon(context.getResources(), packageName, arrayId);
    }

    private void addIcon(Resources resources, String packageName, int list) {
        iconsnames = resources.getStringArray(list);
        for (String extra : iconsnames) {
            int res = resources.getIdentifier(extra, "drawable", packageName);
            if (res != 0) {
                final int thumbRes = resources.getIdentifier(extra, "drawable", packageName);
                if (thumbRes != 0)
                    items.add(thumbRes);
            }
        }
    }

    private String convertText(String name) {
        String partialConvertedText = name.replaceAll("_", " ");
        String[] text = partialConvertedText.split("\\s+");
        StringBuilder sb = new StringBuilder();
        if (text[0].length() > 0) {
            sb.append(Character.toUpperCase(text[0].charAt(0))).append(text[0].subSequence(1, text[0].length()).toString().toLowerCase());
            for (int i = 1; i < text.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(text[i].charAt(0))).append(text[i].subSequence(1, text[i].length()).toString().toLowerCase());
            }
        }
        return sb.toString();
    }
}

