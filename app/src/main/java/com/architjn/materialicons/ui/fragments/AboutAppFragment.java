package com.architjn.materialicons.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architjn.materialicons.R;
import com.architjn.materialicons.items.LauncherListItem;
import com.architjn.materialicons.ui.HomeActivity;

import java.util.ArrayList;

import de.psdev.licensesdialog.LicensesDialog;

/**
 * Created by architjn on 03/01/16.
 */
public class AboutAppFragment extends Fragment {

    private View mainView;
    private ArrayList<LauncherListItem> launchers = new ArrayList<>();
    private RecyclerView rv;
    private Context context;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.activity_about, container, false);
        context = mainView.getContext();
        toolbar = (Toolbar) mainView.findViewById(R.id.toolbar_about);
        setActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= 21){
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.primaryColorDark));
            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(context,R.color.navigationBarBgColor));
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
        (mainView.findViewById(R.id.about_card_app)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.about_card_app_link2));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        (mainView.findViewById(R.id.about_card_app_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.about_card_app_link));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        (mainView.findViewById(R.id.about_card_dev_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.developer_link));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        (mainView.findViewById(R.id.about_card_dev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.developer_googleplus));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        (mainView.findViewById(R.id.about_card_licenses)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLicense();
            }
        });
        (mainView.findViewById(R.id.about_card_based_on)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/jahirfiquitiva/PaperBoard");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        HomeActivity activity = ((HomeActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }

    private ActionBar getActionBar() {
        return ((HomeActivity) getActivity()).getSupportActionBar();
    }

    private void setLicense() {
        new LicensesDialog.Builder(context)
                .setTitle(R.string.library)
                .setNotices(R.raw.notices)
                .build()
                .show();
    }

    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }

}
