package com.architjn.materialicons.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.architjn.materialicons.R;
import com.architjn.materialicons.others.ScrollAwareFABBehavior;
import com.architjn.materialicons.tasks.LoadAppsList;
import com.architjn.materialicons.ui.HomeActivity;
import com.pk.requestmanager.AppInfo;
import com.pk.requestmanager.PkRequestManager;

import java.util.List;

/**
 * Created by architjn on 04/01/16.
 */
public class RequestFragment extends Fragment implements LoadAppsList.Callback {

    private RecyclerView rv;
    private PkRequestManager requestManager;
    private List<AppInfo> apps;
    private View mainView;
    private Context context;
    private RequestAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.activity_request, container, false);
        context = mainView.getContext();
        rv = (RecyclerView) mainView.findViewById(R.id.req_rv);
        setActionBar((Toolbar) mainView.findViewById(R.id.toolbar_req));
        if (Build.VERSION.SDK_INT >= 21)
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.primaryColorDark));

        new LoadAppsList(context, this, requestManager).execute();

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        Snackbar.make(mainView.findViewById(R.id.coordinating_req), "Loading apps may take some time..", Snackbar.LENGTH_LONG).show();
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mainView.findViewById(R.id.fab_rev).getLayoutParams();
        p.setBehavior(new ScrollAwareFABBehavior(context));
        mainView.findViewById(R.id.fab_rev).setLayoutParams(p);
        (mainView.findViewById(R.id.fab_rev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Small workaround
                    requestManager.setActivity(getActivity());
                    // Build and send the request in the background.
                    requestManager.sendRequestAsync();
                    if (requestManager.getNumSelected() == 0) {
                        Snackbar.make(mainView.findViewById(R.id.coordinating_req), "No apps selected!", Snackbar.LENGTH_LONG)
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
                        Snackbar.make(mainView.findViewById(R.id.coordinating_req), "Generating Mail..", Snackbar.LENGTH_LONG).show();
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Snackbar.make(mainView.findViewById(R.id.coordinating_req), "Apps are loading wait for them..", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        HomeActivity activity = ((HomeActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }

    @Override
    public void onListLoaded(List<AppInfo> apps, PkRequestManager requestManager) {
        this.requestManager = requestManager;
        this.apps = apps;
        (mainView.findViewById(R.id.progressBar_req)).setVisibility(View.GONE);
        adapter = new RequestAdapter(context, apps);
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

}
