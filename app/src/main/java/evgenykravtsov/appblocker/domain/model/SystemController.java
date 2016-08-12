package evgenykravtsov.appblocker.domain.model;

import java.util.List;

public interface SystemController {

    List<App> getInstalledApps();

    List<App> getForegroundApps();

    void startAppBlockerService();

    void stopAppBlockerService();

    void playSoundTip(SoundTipType type);

    boolean isInternetAvailable();
}
