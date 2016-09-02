package evgenykravtsov.appblocker.external.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import evgenykravtsov.appblocker.DependencyInjection;

public class AppBlockerController extends Application {

    public static boolean passwordPassed;

    private static Context context;

    private Tracker tracker;

    ////

    public static Context getContext() {
        return context;
    }

    ////

    public static String getAppTitle() {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    ////

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        startAppBlockerService();
    }

    ////

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(777);
        }

        return tracker;
    }

    ////

    private void startAppBlockerService() {
        if (DependencyInjection.provideAppBlockerSettings().loadAppBlockerServiceStatus()) {
            startService(new Intent(this, AppBlockerService.class));
        }
    }
}
