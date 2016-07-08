package evgenykravtsov.appblocker.external.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import evgenykravtsov.appblocker.DependencyInjection;

public class AppBlockerController extends Application {

    private static Context context;

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

    private void startAppBlockerService() {
        if (DependencyInjection.provideAppBlockerSettings().loadAppBlockerServiceStatus()) {
            startService(new Intent(this, AppBlockerService.class));
        }
    }
}