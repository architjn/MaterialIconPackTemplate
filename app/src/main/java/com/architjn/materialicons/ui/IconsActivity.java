package com.architjn.materialicons.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.architjn.materialicons.R;
import com.architjn.materialicons.adapters.ViewPagerAdapter;
import com.architjn.materialicons.ui.fragments.IconFragment;

/**
 * Created by architjn on 27/07/15.
 */
public class IconsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icons);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_icons));
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.icons_tablayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.icons_viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar_icons);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return false;
    }

}
