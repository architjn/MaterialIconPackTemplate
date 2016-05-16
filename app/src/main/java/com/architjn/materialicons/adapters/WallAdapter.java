package com.architjn.materialicons.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.architjn.materialicons.R;
import com.architjn.materialicons.items.WallpaperItem;
import com.architjn.materialicons.tasks.ColorGridTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.SimpleItemViewHolder> {

    public static final int REQUEST_STORAGE = 102;
    private List<WallpaperItem> items;
    private Context context;
    private int reqPos = 0;

    public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView wall;
        public TextView name, author;
        public View mainView;
        public RelativeLayout realBackground;
        public ProgressBar pb;

        public SimpleItemViewHolder(final View view) {
            super(view);
            wall = (ImageView) view.findViewById(R.id.wall_grid_art);
            name = (TextView) view.findViewById(R.id.wall_grid_name);
            author = (TextView) view.findViewById(R.id.wall_grid_desc);
            pb = (ProgressBar) view.findViewById(R.id.progressBar_wall_grid);
            realBackground = (RelativeLayout) view.findViewById(R.id.wall_real_background);
            mainView = view;
        }
    }

    public WallAdapter(Context context, List<WallpaperItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public WallAdapter.SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.wall_grid, parent, false);
        return new SimpleItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SimpleItemViewHolder holder, final int position) {
        holder.realBackground.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        holder.name.setText(items.get(position).getName());
        holder.author.setText(items.get(position).getAuthor());
        Picasso.with(context).load(items.get(position).getThumb())
                .resize(getScreenWidth() / 2, dpToPx(200)).centerCrop().into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.pb.setVisibility(View.GONE);
                holder.wall.setImageBitmap(bitmap);
                holder.realBackground.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                new ColorGridTask(context, bitmap, holder).execute();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(((Activity) context).findViewById(R.id.coordinating_wall), "Wait!", Snackbar.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkForStoragePermission(position, holder);
                    }
                }).start();
            }
        });
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void checkForStoragePermission(int position, SimpleItemViewHolder holder) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            getWallpaper(position);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar.make(holder.mainView, R.string.permission_storage_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions((Activity) context,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_STORAGE);
                            }
                        })
                        .show();
            } else {
                reqPos = position;
                ActivityCompat.requestPermissions(((Activity) context),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE);
            }
        }
    }

    private void getWallpaper(int position) {
        try {
            Uri wallUri = getBitmapFromURL(items.get(position).getUrl());
            if (wallUri != null) {
                Intent setWall = new Intent(Intent.ACTION_ATTACH_DATA);
                setWall.setDataAndType(wallUri, "image/*");
                setWall.putExtra("png", "image/*");
                ((Activity) context).startActivityForResult(
                        Intent.createChooser(setWall, "Set Wallpaper using:"), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        try {
            String saveWallLocation = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + context.getResources().getString(R.string.walls_save_location);
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String format = s.format(new Date());
            File file = new File(saveWallLocation, format + ".png");
            file.getParentFile().mkdirs();
            file.delete();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return getLocalBitmapUri(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void storageRequestAccepted() {
        getWallpaper(reqPos);
    }

}

