package jwtc.android.chess;

import android.app.Activity;
import android.app.AlertDialog;
import android.webkit.WebView;

import com.snapyr.analytics.SnapyrAction;
import com.snapyr.analytics.SnapyrActionHandler;
import com.snapyr.analytics.ValueMap;

public class AnalyticsActionHandler implements SnapyrActionHandler {

    private AlertDialog.Builder zone1;
    private WebView zone2;
    private Activity context;

    public AnalyticsActionHandler(Activity context, AlertDialog.Builder zone1, WebView zone2) {
        this.context = context;
        this.zone1 = zone1;
        this.zone2 = zone2;
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
            if (zone == "Zone 2") {
                this.zone2.loadUrl((String) params.get("content-url"));
            }
        }
    }
}
