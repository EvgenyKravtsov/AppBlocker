package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;
import evgenykravtsov.appblocker.domain.usecase.GetApps;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.billing.BillingSettings;
import evgenykravtsov.appblocker.presentation.onboarding.OnboardingSettings;

public class MainPresenter {

    public interface View {

        void showApps(List<App> apps);

        void setBlockControlState(boolean state);

        void notifyBlockControlStateChanged(boolean state);

        void showSetParentPasswordRecommendation();

        void showUserFeedbackRequest();
    }

    ////

    private AppBlockerSettings appBlockerSettings;
    private View view;
    private UseCaseThreadPool threadPool;
    private OnboardingSettings onboardingSettings;
    private BillingSettings billingSettings;

    ////

    public MainPresenter(View view,
                         AppBlockerSettings appBlockerSettings,
                         UseCaseThreadPool threadPool) {

        this.view = view;
        this.appBlockerSettings = appBlockerSettings;
        this.threadPool = threadPool;
        this.onboardingSettings = DependencyInjection.provideOnboardingSettings();
        this.billingSettings = DependencyInjection.provideBillingSettings();

        checkOnboardingEvents();
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
            view.notifyBlockControlStateChanged(false);
        } else {
            startAppBlocker();
            view.setBlockControlState(true);
            view.notifyBlockControlStateChanged(true);
        }
    }

    public void startAppBlocker() {
        if (!onboardingSettings.loadSetParentPasswordRecommendationStatus()) {
            view.showSetParentPasswordRecommendation();
            onboardingSettings.saveSetParentPasswordRecommendationStatus(true);
        }

        threadPool.execute(UseCaseFactory.provideStartAppBlockerUseCase());
    }

    public void stopAppBlocker() {
        threadPool.execute(UseCaseFactory.provideStopAppBlockerUseCase());
    }

    public boolean getAppBlockerStatus() {
        return appBlockerSettings.loadAppBlockerServiceStatus();
    }

    public boolean getPasswordActivationStatus() {
        return appBlockerSettings.loadPasswordActivationStatus();
    }

    public boolean checkPassword(String password) {
        String savedPassword = appBlockerSettings.loadPassword();
        return savedPassword.equals(password);
    }

    public void appHasBeenShared() {
        billingSettings.saveAppSharedStatus(true);
    }

    ////

    private void checkOnboardingEvents() {
        int numberOfAppLaunches = onboardingSettings.loadNumberOfAppLaunches();
        onboardingSettings.saveNumberOfAppLaunches(++numberOfAppLaunches);

        if (onboardingSettings.loadNumberOfAppLaunches() ==
                OnboardingSettings.NUMBER_OF_APP_LAUNCHES_FOR_USER_FEEDBACK)
            view.showUserFeedbackRequest();
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetApps.Executed event) {
        view.showApps(event.getApps());
    }
}
