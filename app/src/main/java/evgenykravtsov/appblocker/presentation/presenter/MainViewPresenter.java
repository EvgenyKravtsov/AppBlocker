package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.usecase.GetApps;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

public class MainViewPresenter {

    public interface View {

        void showApps(List<App> apps);

        void setBlockControlState(boolean state);
    }

    ////

    private AppBlockerSettings appBlockerSettings;
    private View view;
    private UseCaseThreadPool threadPool;

    ////

    public MainViewPresenter(View view,
                             AppBlockerSettings appBlockerSettings,
                             UseCaseThreadPool threadPool) {
        this.view = view;
        this.appBlockerSettings = appBlockerSettings;
        this.threadPool = threadPool;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestApps() {
        threadPool.execute(UseCaseFactory.provideGetAppsUseCase());
    }

    public void toggleAppBlocker() {
        if (appBlockerSettings.loadAppBlockerServiceStatus()) {
            stopAppBlocker();
            view.setBlockControlState(false);
        } else {
            startAppBlocker();
            view.setBlockControlState(true);
        }
    }

    public void startAppBlocker() {
        threadPool.execute(UseCaseFactory.provideStartAppBlockerUseCase());
    }

    public void stopAppBlocker() {
        threadPool.execute(UseCaseFactory.provideStopAppBlockerUseCase());
    }

    public boolean getAppBlockerStatus() {
        return appBlockerSettings.loadAppBlockerServiceStatus();
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetApps.Executed event) {
        view.showApps(event.getApps());
    }
}
