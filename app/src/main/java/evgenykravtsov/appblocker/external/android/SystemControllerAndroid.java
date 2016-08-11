package evgenykravtsov.appblocker.external.android;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.AppBlocker;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class SystemControllerAndroid implements SystemController, AppBlocker.Operation {

    private Context context;
    private PackageManager packageManager;
    private ActivityManager activityManager;

    ////

    public SystemControllerAndroid() {
        this.context = AppBlockerController.getContext();
        this.packageManager = context.getPackageManager();
        this.activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    ////

    @Override
    public List<App> getInstalledApps() {
        List<ApplicationInfo> applicationInfoList = getApplicationInfoList();
        List<App> installedApps = new ArrayList<>();

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;

            App app = mapApplicationInfoToApp(applicationInfo);
            if (!app.getTitle().equals(AppBlockerController.getAppTitle())) installedApps.add(app);
        }

        return installedApps;
    }

    @Override
    public List<App> getForegroundApps() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP)
            return getForegroundAppsPostLollipop();
        else return getForefroundAppsPreLollipop();
    }

    @Override
    public void startAppBlockerService() {
        context.startService(new Intent(context, AppBlockerService.class));
    }

    @Override
    public void stopAppBlockerService() {
        context.stopService(new Intent(context, AppBlockerService.class));
    }

    ////

    @Override
    public void execute() {
        Intent intent = new Intent(context, BlockerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    ////

    private List<ApplicationInfo> getApplicationInfoList() {
        return packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
    }

    private App mapApplicationInfoToApp(ApplicationInfo applicationInfo) {
        return new App(applicationInfo.loadLabel(packageManager).toString(),
                applicationInfo.processName);
    }

    private String getAppTitle(ActivityManager.RunningAppProcessInfo appProcessInfo) {
        try {
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(
                    appProcessInfo.processName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<App> getForefroundAppsPreLollipop() {
        List<App> foregroundApps = new ArrayList<>();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList =
                activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcessInfoList) {
            if (appProcessInfo.importance ==
                    ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                foregroundApps.add(new App(
                        getAppTitle(appProcessInfo),
                        appProcessInfo.processName));
            }
        }

        return foregroundApps;
    }

    @SuppressLint("InlinedApi")
    private List<App> getForegroundAppsPostLollipop() {
        List<App> foregroundApps = new ArrayList<>();

        UsageStatsManager manager = (UsageStatsManager)
                context.getSystemService(Context.USAGE_STATS_SERVICE);

        long time = System.currentTimeMillis();

        List<UsageStats> appList = manager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 1000,
                time);

        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> sortedMap = new TreeMap<>();

            for (UsageStats usageStats : appList)
                sortedMap.put(usageStats.getLastTimeUsed(), usageStats);

            if (!sortedMap.isEmpty())
                foregroundApps.add(
                        new App("APP_NAME",
                                sortedMap.get(sortedMap.lastKey()).getPackageName()));
        }

        return foregroundApps;
    }
}

































