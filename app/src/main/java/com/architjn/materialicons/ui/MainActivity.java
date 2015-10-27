package com.architjn.materialicons.ui;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.architjn.materialicons.BuildConfig;
import com.architjn.materialicons.PackageName;
import com.architjn.materialicons.R;
import com.architjn.materialicons.adapters.ChangelogAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=";
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForLicense();
        init();
        showChangelogIfNeeded();
    }

    private void showChangelogIfNeeded() {
        SharedPreferences shp = getSharedPreferences(PackageName.class.getName().toString(), MODE_PRIVATE);
        if (shp.getInt("ver", 0) < BuildConfig.VERSION_CODE) {
            showChangelog();
            shp.edit().putInt("ver", BuildConfig.VERSION_CODE).apply();
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.main_navigationview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout_main))
                .setTitle(getResources().getString(R.string.theme_title));
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new
                    ActivityManager.TaskDescription(getResources().getString(R.string.theme_title),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                    getResources().getColor(R.color.primaryColor));
            setTaskDescription(taskDescription);
        }
        (findViewById(R.id.fab_main)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ApplyIconActivity.class));
            }
        });
        setNav();
        setCards();
    }

    private void setCards() {
        Button button1, button2, button3;
        button1 = (Button) findViewById(R.id.home_card_one_button);
        button2 = (Button) findViewById(R.id.home_card_two_button);
        button3 = (Button) findViewById(R.id.home_card_three_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.card1_link));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        if (!getResources().getBoolean(R.bool.card1_visible))
            (findViewById(R.id.main_card2)).setVisibility(View.GONE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.card2_link));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        if (!getResources().getBoolean(R.bool.card2_visible))
            (findViewById(R.id.main_card2)).setVisibility(View.GONE);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getResources().getString(R.string.card3_link));
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            }
        });
        if (!getResources().getBoolean(R.bool.card3_visible))
            (findViewById(R.id.main_card3)).setVisibility(View.GONE);
    }

    private void setNav() {
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open, R.string.close) {
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
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.navigation_apply) {
                            startActivity(new Intent(MainActivity.this, ApplyIconActivity.class));
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_icons) {
                            startActivity(new Intent(MainActivity.this, IconsActivity.class));
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_wall) {
                            startActivity(new Intent(MainActivity.this, WallpaperActivity.class));
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_req_icons) {
                            startActivity(new Intent(MainActivity.this, RequestActivity.class));
                            closeDrawerAfterSmallDelay();
                        } else if (menuItem.getItemId() == R.id.navigation_about) {
                            startActivity(new Intent(MainActivity.this, AboutAppActivity.class));
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
                navigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
            }
        }, 800);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sendemail) {
            StringBuilder emailBuilder = new StringBuilder();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getResources().getString(R.string.email_id)));
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));

            emailBuilder.append("\n \n \nOS Version: ").append(System.getProperty("os.version")).append("(").append(Build.VERSION.INCREMENTAL).append(")");
            emailBuilder.append("\nOS API Level: ").append(Build.VERSION.SDK_INT);
            emailBuilder.append("\nDevice: ").append(Build.DEVICE);
            emailBuilder.append("\nManufacturer: ").append(Build.MANUFACTURER);
            emailBuilder.append("\nModel (and Product): ").append(Build.MODEL).append(" (").append(Build.PRODUCT).append(")");
            PackageInfo appInfo = null;
            try {
                appInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            assert appInfo != null;
            emailBuilder.append("\nApp Version Name: ").append(appInfo.versionName);
            emailBuilder.append("\nApp Version Code: ").append(appInfo.versionCode);

            intent.putExtra(Intent.EXTRA_TEXT, emailBuilder.toString());
            startActivity(Intent.createChooser(intent, (getResources().getString(R.string.send_title))));
            return true;
        } else if (id == R.id.action_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody =
                    getResources().getString(R.string.share_one) +
                            getResources().getString(R.string.developer_name) +
                            getResources().getString(R.string.share_two) +
                            MARKET_URL + getPackageName();
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, (getResources().getString(R.string.share_title))));
        } else if (id == R.id.action_changelog) {
            showChangelog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkForLicense() {
        if (getResources().getBoolean(R.bool.license_check)) {
            String installer = getPackageManager().getInstallerPackageName(getPackageName());
            try {
                if (installer.matches("com.android.vending") ||
                        installer.matches("com.google.android.feedback") ||
                        installer.matches("com.amazon.venezia")) {
                } else {
                    showNotLicensedDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showNotLicensedDialog();
            }
        }
    }

    private void showNotLicensedDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.license_failed_title)
                .content(R.string.license_failed)
                .positiveText(R.string.download)
                .negativeText(R.string.exit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + getPackageName()));
                        startActivity(browserIntent);
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        finish();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                }).show();
    }

    private void showChangelog() {
        new MaterialDialog.Builder(this)
                .title(R.string.changelog_dialog_title)
                .adapter(new ChangelogAdapter(this, R.array.fullchangelog), null)
                .positiveText(R.string.nice)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                    }
                }).show();
    }
}