package evgenykravtsov.appblocker.domain.usecase;

import evgenykravtsov.appblocker.domain.model.AppBlocker;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

public class CheckForegroundApp implements UseCase {

    private AppBlocker appBlocker;

    ////

    public CheckForegroundApp(AppBlocker appBlocker) {
        this.appBlocker = appBlocker;
    }

    ////

    @Override
    public void execute() {
        if (appBlocker.isAllowedAppGoneFromForeground())
            appBlocker.setAllowedProcessName(AppBlockerSettings.DEFAULT_ALLOWED_PROCESS_NAME);
        if (appBlocker.isForegroundAppBlocked())
            if (!appBlocker.isDeniedAppAllowed()) appBlocker.executeBlockOperation();
    }
}
