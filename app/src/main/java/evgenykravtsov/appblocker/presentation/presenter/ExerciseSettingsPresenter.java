package evgenykravtsov.appblocker.presentation.presenter;


import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;

public class ExerciseSettingsPresenter {

    public interface View {}

    ////

    private View view;
    private ExerciseSettings exerciseSettings;

    ////

    public ExerciseSettingsPresenter(View view) {
        this.view = view;
        exerciseSettings = DependencyInjection.provideExerciseSettings();
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
}
