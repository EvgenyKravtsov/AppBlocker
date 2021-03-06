package evgenykravtsov.appblocker.external.android;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.AppBlocker;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class SystemControllerAndroid implements SystemController, AppBlocker.Operation {

    private Context context;
    private PackageManager packageManager;
    private ActivityManager activityManager;
    private MediaPlayer mediaPlayer;

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

            App app = null;

            try {
                app = mapApplicationInfoToApp(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (app != null && !app.getTitle().equals(AppBlockerController.getAppTitle()))
                installedApps.add(app);
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

    @Override
    public void playSoundTip(SoundTipType type) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = prepareMediaPlayerForSoundTip(type);
        mediaPlayer.start();
    }

    @Override
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

    private App mapApplicationInfoToApp(ApplicationInfo applicationInfo) throws PackageManager.NameNotFoundException {
        App app = new App(
                applicationInfo.loadLabel(packageManager).toString(),
                applicationInfo.processName);
        app.setIcon(packageManager.getApplicationIcon(applicationInfo.processName));
        return app;
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

    private MediaPlayer prepareMediaPlayerForSoundTip(SoundTipType type) {

        int soundResId;

        switch (type) {
            case OddPictureTip: soundResId = R.raw.odd_picture_tip; break;
            case ColorTipRed: soundResId = R.raw.color_tip_red; break;
            case ColorTipOrange: soundResId = R.raw.color_tip_orange; break;
            case ColorTipYellow: soundResId = R.raw.color_tip_yellow; break;
            case ColorTipGreen: soundResId = R.raw.color_tip_green; break;
            case ColorTipLightBlue: soundResId = R.raw.color_tip_light_blue; break;
            case ColorTipBlue: soundResId = R.raw.color_tip_blue; break;
            case ColorTipPurple: soundResId = R.raw.color_tip_purple; break;
            case ColorTipWhite: soundResId = R.raw.color_tip_white; break;
            case ColorTipGray: soundResId = R.raw.color_tip_gray; break;
            case ColorTipBlack: soundResId = R.raw.color_tip_black; break;
            case ColorTipPink: soundResId = R.raw.color_tip_pink; break;
            case ColorTipBrown: soundResId = R.raw.color_tip_brown; break;
            case RightAnswerTip: soundResId = R.raw.right_answer_tip; break;
            case WrongAnswerTip: soundResId = R.raw.wrong_answer_tip; break;
            default: soundResId = R.raw.odd_picture_tip;
        }

        return MediaPlayer.create(context, soundResId);
    }
}

































