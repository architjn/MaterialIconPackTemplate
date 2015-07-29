package com.architjn.materialicons.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.architjn.materialicons.R;
import com.pk.requestmanager.AppInfo;
import com.pk.requestmanager.PkRequestManager;
import com.pk.requestmanager.RequestSettings;

import java.util.List;

/**
 * Created by architjn on 29/07/15.
 */
public class LoadAppsList extends AsyncTask<Void, Void, Void> {

    public interface Callback {
        public void onListLoaded(List<AppInfo> apps, PkRequestManager requestManager);
    }

    private Callback callback;
    private Context context;
    private PkRequestManager requestManager;
    private List<AppInfo> apps;

    public LoadAppsList(Context context, Callback callback, PkRequestManager requestManager) {
        this.callback = callback;
        this.context = context;
        this.requestManager = requestManager;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Grab a reference to the manager and store it in a variable. This helps make code shorter.
        requestManager = PkRequestManager.getInstance(context);

        // Enable debugging. Disable this during production!
        requestManager.setDebugging(true);

        // Set your custom settings. Email address is required! Everything else is set to default.
        requestManager.setSettings(new RequestSettings.Builder()
                .addEmailAddress(context.getResources().getString(R.string.email_id))
                .emailSubject(context.getResources().getString(R.string.email_request_subject))
                .build());

        // Load the list of apps if none are loaded. This should normally be done asynchronously.
        requestManager.loadAppsIfEmpty();

        // Get the list of apps
        apps = requestManager.getApps();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callback.onListLoaded(apps, requestManager);
    }
}
