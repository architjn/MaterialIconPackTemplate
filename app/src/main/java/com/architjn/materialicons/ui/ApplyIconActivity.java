package com.architjn.materialicons.ui;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.architjn.materialicons.R;
import com.architjn.materialicons.adapters.LaunchersAdapter;
import com.architjn.materialicons.items.LauncherListItem;
import com.architjn.materialicons.others.LauncherComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by architjn on 27/07/15.
 */
public class ApplyIconActivity extends AppCompatActivity {

    private ArrayList<LauncherListItem> launchers = new ArrayList<>();
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_apply));
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        String[] launcherArray = getResources().getStringArray(R.array.launchers_list);
        for (String launcher : launcherArray) {
            launchers.add(new LauncherListItem(launcher.split("\\|"),
                    isLauncherInstalled(launcher.split("\\|")[1])));
        }
        Collections.sort(launchers, new LauncherComparator());
        LaunchersAdapter adapter = new LaunchersAdapter(this, launchers);
        rv = (RecyclerView) findViewById(R.id.apply_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

    private boolean isLauncherInstalled(String packageName) {
        final PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
