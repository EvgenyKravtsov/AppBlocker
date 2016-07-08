package evgenykravtsov.appblocker.domain.usecase;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.AppBlocker;

public class StartAppBlocker implements UseCase {

    @Override
    public void execute() {
        AppBlocker appBlocker = DependencyInjection.provideAppBlocker();
        appBlocker.startBlocking();
    }
}
