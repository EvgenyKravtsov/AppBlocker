package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorType;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class TestColorExercisePresenter extends ColorExercisePresenter {

    public TestColorExercisePresenter(
            View view,
            UseCaseThreadPool threadPool,
            SystemController systemController) {

        super(view, threadPool, systemController);
    }

    ////

    @Override
    public void checkResult(ColorType colorType) {
        if (colorType == colorExercise.getColorType()) {
            view.finish();
            view.notifyCheckResult(true);
        } else {
            view.notifyCheckResult(false);
            requestColorExercise();
        }
    }
}
