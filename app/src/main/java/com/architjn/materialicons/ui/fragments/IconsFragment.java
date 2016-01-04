package com.architjn.materialicons.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architjn.materialicons.R;
import com.architjn.materialicons.adapters.ViewPagerAdapter;
import com.architjn.materialicons.ui.HomeActivity;

/**
 * Created by architjn on 04/01/16.
 */
public class IconsFragment extends Fragment {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private View mainView;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.activity_icons, container, false);
        context = mainView.getContext();
        if (Build.VERSION.SDK_INT >= 21)
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.primaryColorDark));
        init();
        setActionBar(toolbar);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.icons_tablayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        HomeActivity activity = ((HomeActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }

    private void init() {
        viewPager = (ViewPager) mainView.findViewById(R.id.icons_viewPager);
        toolbar = (Toolbar) mainView.findViewById(R.id.toolbar_icons);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(getFragment(R.array.latest), getResources().getString(R.string.latest));
        adapter.addFrag(getFragment(R.array.system), getResources().getString(R.string.system));
        adapter.addFrag(getFragment(R.array.google), getResources().getString(R.string.google));
        adapter.addFrag(getFragment(R.array.games), getResources().getString(R.string.games));
        adapter.addFrag(getFragment(R.array.icon_pack), getResources().getString(R.string.icon_pack));
        adapter.addFrag(getFragment(R.array.drawer), getResources().getString(R.string.drawer));
        viewPager.setAdapter(adapter);
    }

    private IconFragment getFragment(int iconArray) {
        IconFragment fragment = new IconFragment();
        Bundle args = new Bundle();
        args.putInt("iconsArrayId", iconArray);
        fragment.setArguments(args);
        return fragment;
    }
}
