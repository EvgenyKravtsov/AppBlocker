package evgenykravtsov.appblocker.presentation.presenter;


import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.external.android.AppBlockerController;
import evgenykravtsov.appblocker.presentation.onboarding.OnboardingSettings;

public class ExerciseSettingsPresenter {

    public interface View {

        void showExerciseSettingsTip();
    }

    ////

    private View view;
    private ExerciseSettings exerciseSettings;
    private AppBlockerSettings appBlockerSettings;
    private OnboardingSettings onboardingSettings;

    ////

    public ExerciseSettingsPresenter(View view) {
        this.view = view;
        exerciseSettings = DependencyInjection.provideExerciseSettings();
        appBlockerSettings = DependencyInjection.provideAppBlockerSettings();
        onboardingSettings = DependencyInjection.provideOnboardingSettings();

        checkOnboardingEvents();
    }

    ////

    public void unbindView() {
        view = null;
    }

    public int getSessionExerciseNumber() {
        return exerciseSettings.loadSessionExerciseNumber();
    }

    public void setSessionExerciseNumber(int number) {
        exerciseSettings.saveSessionExerciseNumber(number);
    }

    public boolean getSoundSupportStatus() {
        return exerciseSettings.loadSoundSupportStatus();
    }

    public void setSoundSupportStatus(boolean status) {
        exerciseSettings.saveSoundSupportStatus(status);
    }

    public boolean getPasswordSetStatus() {
        return appBlockerSettings.loadPasswordSetStatus();
    }

    public void setPassword(String password) {
        appBlockerSettings.savePasswordSetStatus(true);
        appBlockerSettings.savePasswordActivationStatus(true);
        appBlockerSettings.savePassword(password);
        AppBlockerController.passwordPassed = true;
    }

    public boolean getPasswordActivationStatus() {
        return appBlockerSettings.loadPasswordActivationStatus();
    }

    public void setPasswordActivationStatus(boolean status) {
        appBlockerSettings.savePasswordActivationStatus(status);
    }

    ////

    private void checkOnboardingEvents() {
        if (!onboardingSettings.loadExerciseSettingsTipStatus()) {
            view.showExerciseSettingsTip();
            onboardingSettings.saveExerciseSettingsTipStatus(true);
        }
    }
}
