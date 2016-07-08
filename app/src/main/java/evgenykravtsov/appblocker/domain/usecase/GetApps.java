package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.AppManager;

public class GetApps implements UseCase {

    @Override
    public void execute() {
        AppManager appManager = DependencyInjection.provideAppManager();
        EventBus.getDefault().post(new Executed(appManager.getApps()));
    }

    ////

    public class Executed {

        private final List<App> apps;

        ////

        public Executed(List<App> apps) {
            this.apps = apps;
        }

        ////

        public List<App> getApps() {
            return apps;
        }
    }
}
