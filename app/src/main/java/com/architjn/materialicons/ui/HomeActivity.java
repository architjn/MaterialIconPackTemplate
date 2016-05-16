package com.architjn.materialicons.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.architjn.materialicons.R;
import com.architjn.materialicons.ui.fragments.AboutAppFragment;
import com.architjn.materialicons.ui.fragments.ApplyIconFragment;
import com.architjn.materialicons.ui.fragments.HomeFragment;
import com.architjn.materialicons.ui.fragments.IconsFragment;
import com.architjn.materialicons.ui.fragments.RequestFragment;
import com.architjn.materialicons.ui.fragments.WallpapersFragment;

/**
 * Created by architjn on 03/01/16.
 */
public class HomeActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        switchFragment(new HomeFragment(), false);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.primaryColorDark));
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.navigationBarBgColor));
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.main_navigationview);
    }

    public void updateToggleButton(Toolbar toolbar) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void setNav(Toolbar toolbar) {
        updateToggleButton(toolbar);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.navigation_home) {
                            menuItem.setCheckable(true);
                            HomeFragment fragment = new HomeFragment();
                            switchFragment(fragment, true);
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_apply) {
                            menuItem.setCheckable(true);
                            switchFragment(new ApplyIconFragment(), true);
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_icons) {
                            menuItem.setCheckable(true);
                            switchFragment(new IconsFragment(), true);
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_wall) {
                            menuItem.setCheckable(true);
                            switchFragment(new WallpapersFragment(), true);
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_req_icons) {
                            menuItem.setCheckable(true);
                            switchFragment(new RequestFragment(), true);
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_about) {
                            switchFragment(new AboutAppFragment(), true);
                            closeDrawerAfterSmallDelay();
                        }
                        return true;
                    }
                });

    }

    private void closeDrawerAfterSmallDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 800);
    }

    public void switchFragment(Fragment fragment, boolean b) {
        if (!b)
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
        else
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}
