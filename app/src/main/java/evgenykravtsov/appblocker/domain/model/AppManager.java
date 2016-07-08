package evgenykravtsov.appblocker.domain.model;

import java.util.List;

public class AppManager {

    private SystemController systemController;
    private AppRepository appRepository;

    ////

    public AppManager(SystemController systemController,
                      AppRepository appRepository) {
        this.systemController = systemController;
        this.appRepository = appRepository;
    }

    ////

    public List<App> getApps() {
        List<App> apps = systemController.getInstalledApps();
        List<App> blockedApps = appRepository.getBlockedApps();

        for (App app : apps) {
            if (blockedApps.contains(app)) app.setBlocked(true);
        }

        return apps;
    }

    public List<App> getBlockedApps() {
        return appRepository.getBlockedApps();
    }

    public List<App> getForegroundApps() {
        return systemController.getForegroundApps();
    }

    public void addAppToBlockedApps(App app) {
        appRepository.addAppToBlockList(app);
    }

    public void removeAppFromBlockedApps(App app) {
        appRepository.removeAppFromBlockList(app);
    }
}
