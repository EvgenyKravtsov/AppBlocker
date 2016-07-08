package evgenykravtsov.appblocker.domain.usecase;

import evgenykravtsov.appblocker.domain.model.AppBlocker;

public class AllowApp implements UseCase {

    private AppBlocker appBlocker;

    ////

    public AllowApp(AppBlocker appBlocker) {
        this.appBlocker = appBlocker;
    }

    ////

    @Override
    public void execute() {
        appBlocker.setAllowedProcessName(appBlocker.getDeniedProcessName());
    }
}
