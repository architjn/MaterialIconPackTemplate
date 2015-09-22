package com.architjn.materialicons.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.architjn.materialicons.R;
import com.architjn.materialicons.tasks.LoadAppsList;
import com.pk.requestmanager.AppInfo;
import com.pk.requestmanager.PkRequestManager;

import java.util.List;

/**
 * Created by architjn on 29/07/15.
 */
public class RequestActivity extends AppCompatActivity implements LoadAppsList.Callback {

    private RecyclerView rv;
    private PkRequestManager requestManager;
    private List<AppInfo> apps;
    private RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        rv = (RecyclerView) findViewById(R.id.req_rv);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_req));
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(getResources().getColor(R.color.primaryColorDark));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LoadAppsList(this, this, requestManager).execute();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        Snackbar.make(findViewById(R.id.coordinating_req), "Loading apps may take some time..", Snackbar.LENGTH_LONG).show();
        (findViewById(R.id.fab_rev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Small workaround
                    requestManager.setActivity(RequestActivity.this);
                    // Build and send the request in the background.
                    requestManager.sendRequestAsync();
                    if (requestManager.getNumSelected() == 0) {
                        Snackbar.make(findViewById(R.id.coordinating_req), "No apps selected!", Snackbar.LENGTH_LONG)
                                .setAction("Select All", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        List<AppInfo> mAppList = apps;
                                        for (AppInfo mApp : mAppList) {
                                            mApp.setSelected(true);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                }).show();
                    } else {
                        Snackbar.make(findViewById(R.id.coordinating_req), "Generating Mail..", Snackbar.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(R.id.coordinating_req), "Apps are loading wait for them..", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onListLoaded(List<AppInfo> apps, PkRequestManager requestManager) {
        this.requestManager = requestManager;
        this.apps = apps;
        (findViewById(R.id.progressBar_req)).setVisibility(View.GONE);
        adapter = new RequestAdapter(this, apps);
        rv.setAdapter(adapter);
    }

    public static class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.SimpleItemViewHolder> {

        private final List<AppInfo> items;
        private Context context;

        public final static class SimpleItemViewHolder extends RecyclerView.ViewHolder {
            public TextView appName;
            public ImageView appIcon;
            public CheckBox checkBox;
            public View mainView;

            public SimpleItemViewHolder(View view) {
                super(view);

                appName = (TextView) view.findViewById(R.id.req_item_name);
                appIcon = (ImageView) view.findViewById(R.id.req_item_icon);
                checkBox = (CheckBox) view.findViewById(R.id.req_item_checkbox);
                mainView = view;
            }
        }

        public RequestAdapter(Context context, List<AppInfo> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public RequestAdapter.SimpleItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.req_list_item, parent, false);


            return new SimpleItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SimpleItemViewHolder holder, final int position) {

            holder.appName.setText(items.get(position).getName());
            holder.appIcon.setImageDrawable(items.get(position).getImage());
            holder.checkBox.setChecked(items.get(position).isSelected());
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToList(position);
                }
            });
            holder.mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToList(position);
                }
            });
        }

        private void addToList(int position) {
            AppInfo mApp = items.get(position);
            mApp.setSelected(!mApp.isSelected());
            items.set(position, mApp);

            // Let the adapter know you selected something
            new CountDownTimer(400, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    notifyDataSetChanged();
                }
            }.start();
        }

        @Override
        public int getItemCount() {
            return this.items.size();
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
