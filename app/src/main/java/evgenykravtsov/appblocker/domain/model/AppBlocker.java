package evgenykravtsov.appblocker.domain.model;

import android.util.Log;

import java.util.List;

public class AppBlocker {

    private SystemController systemController;
    private AppBlockerSettings appBlockerSettings;
    private AppManager appManager;
    private Operation operation;

    ////

    public AppBlocker(SystemController systemController,
                      AppBlockerSettings appBlockerSettings,
                      AppManager appManager,
                      Operation operation) {
        this.systemController = systemController;
        this.appBlockerSettings = appBlockerSettings;
        this.appManager = appManager;
        this.operation = operation;
    }

    ////

    public void startBlocking() {
        systemController.startAppBlockerService();
        appBlockerSettings.saveAppBlockerServiceStatus(true);
    }

    public void stopBlocking() {
        systemController.stopAppBlockerService();
        appBlockerSettings.saveAppBlockerServiceStatus(false);
    }

    public boolean isForegroundAppBlocked() {
        List<App> blockedApps = appManager.getBlockedApps();
        List<App> foregroundApps = appManager.getForegroundApps();

        for (App app : foregroundApps) {
            if (blockedApps.contains(app)) {
                setDeniedProcessName(app.getProcessName());
                return true;
            }
        }

        return false;
    }

    public boolean isAllowedAppGoneFromForeground() {
        List<App> foregroundApps = appManager.getForegroundApps();

        for (App app : foregroundApps) {
            if (app.getProcessName().equals(getAllowedProcessName())) return false;
        }

        return true;
    }

    public boolean isDeniedAppAllowed() {
        return getDeniedProcessName().equals(getAllowedProcessName());
    }

    public void executeBlockOperation() {
        operation.execute();
    }

    public String getDeniedProcessName() {
        return appBlockerSettings.loadDeniedProcessName();
    }

    public void setDeniedProcessName(String processName) {
        appBlockerSettings.saveDeniedProcessName(processName);
    }

    public String getAllowedProcessName() {
        return appBlockerSettings.loadAllowedProcessName();
    }

    public void setAllowedProcessName(String processName) {
        appBlockerSettings.saveAllowedProcessName(processName);
    }

    ////

    public interface Operation {

        void execute();
    }
}
