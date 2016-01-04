package com.architjn.materialicons.ui.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architjn.materialicons.R;
import com.architjn.materialicons.adapters.LaunchersAdapter;
import com.architjn.materialicons.items.LauncherListItem;
import com.architjn.materialicons.others.LauncherComparator;
import com.architjn.materialicons.ui.HomeActivity;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by architjn on 04/01/16.
 */
public class ApplyIconFragment extends Fragment {

    private ArrayList<LauncherListItem> launchers = new ArrayList<>();
    private View mainView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.activity_apply, container, false);
        context = mainView.getContext();
        setActionBar((Toolbar) mainView.findViewById(R.id.toolbar_apply));
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.primaryColorDark));
            getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(context,R.color.navigationBarBgColor));
        }
        init();
        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        HomeActivity activity = ((HomeActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }

    private void init() {
        String[] launcherArray = getResources().getStringArray(R.array.launchers_list);
        int installed = 0;
        for (String launcher : launcherArray) {
            String[] value = launcher.split("\\|");
            boolean isInstalled = isLauncherInstalled(value[1]);
            if (isInstalled)
                installed++;
            launchers.add(new LauncherListItem(value, isInstalled));
        }
        Collections.sort(launchers, new LauncherComparator());
        LaunchersAdapter adapter = new LaunchersAdapter(context, launchers, installed);
        RecyclerView rv = (RecyclerView) mainView.findViewById(R.id.apply_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

    private boolean isLauncherInstalled(String packageName) {
        final PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
