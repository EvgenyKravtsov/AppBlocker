package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;

public class ExerciseSettingsPresenter {

    public interface View {}

    ////

    private View view;

    ////

    public ExerciseSettingsPresenter(View view) {
        this.view = view;
    }

    ////

    public void unbindView() {
        view = null;
    }
}
