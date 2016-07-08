package evgenykravtsov.appblocker.domain.model;

import java.util.List;

public interface AppRepository {

    List<App> getBlockedApps();

    void addAppToBlockList(App app);

    void removeAppFromBlockList(App app);
}
