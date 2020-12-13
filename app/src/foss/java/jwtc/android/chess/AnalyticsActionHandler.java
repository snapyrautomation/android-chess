package jwtc.android.chess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.snapyr.analytics.SnapyrAction;
import com.snapyr.analytics.SnapyrActionHandler;
import com.snapyr.analytics.ValueMap;

public class AnalyticsActionHandler implements SnapyrActionHandler, Application.ActivityLifecycleCallbacks {

    private AlertDialog.Builder zone1;
    private WebView zone2;
    private Activity context;

    public AnalyticsActionHandler(Activity context, AlertDialog.Builder zone1, WebView zone2) {
        this.context = context;
        this.zone1 = zone1;
        this.zone2 = zone2;
    }
    public AnalyticsActionHandler(Activity context, AlertDialog.Builder zone1) {
        this.context = context;
        this.zone1 = zone1;
        this.zone2 = null;
    }
    public AnalyticsActionHandler(Activity context) {
        this.context = context;
        this.zone1 = null;
        this.zone2 = null;
    }

    @Override
    public void handleAction(SnapyrAction snapyrAction) {
        ValueMap props = snapyrAction.getProperties();
        if (props.containsKey("zone")) {
            String zone = (String) props.get("zone");
            ValueMap params = (ValueMap) props.get("parameters");
            if (zone == "Zone 1") {
                WebView zoneView = new WebView(this.context);
                zoneView.loadUrl((String) params.get("content-url"));
                this.zone1.setView(zoneView);
                this.zone1.show();
            }
            if (zone == "Zone 2" && this.zone2 != null) {
                this.zone2.loadUrl((String) params.get("content-url"));
                this.zone2.setBackgroundColor(Color.TRANSPARENT);
                this.zone2.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d("ActionHandler", "onActivityStarted");
        this.context = activity;
        this.zone1 = new AlertDialog.Builder(this.context);
        this.zone2 = this.context.findViewById(R.id.SnapyrZone2);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
