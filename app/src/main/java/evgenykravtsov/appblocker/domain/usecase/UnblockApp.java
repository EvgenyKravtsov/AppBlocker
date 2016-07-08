package evgenykravtsov.appblocker.domain.usecase;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.AppManager;

public class UnblockApp implements UseCase {

    private App app;

    ////

    public UnblockApp(App app) {
        this.app = app;
    }

    ////

    @Override
    public void execute() {
        AppManager appManager = DependencyInjection.provideAppManager();
        appManager.removeAppFromBlockedApps(app);
    }
}
