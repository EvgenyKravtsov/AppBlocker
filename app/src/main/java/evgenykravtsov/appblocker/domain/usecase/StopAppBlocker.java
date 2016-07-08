package evgenykravtsov.appblocker.domain.usecase;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.AppBlocker;

public class StopAppBlocker implements UseCase {

    @Override
    public void execute() {
        AppBlocker appBlocker = DependencyInjection.provideAppBlocker();
        appBlocker.stopBlocking();
    }
}
