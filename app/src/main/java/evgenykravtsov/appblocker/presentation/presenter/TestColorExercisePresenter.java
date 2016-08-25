package evgenykravtsov.appblocker.presentation.presenter;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorType;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

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
            view.notifyCheckResult(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(BlockerActivity.EXERCISE_CHANGE_DELAY);
                        view.finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            view.notifyCheckResult(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(BlockerActivity.EXERCISE_CHANGE_DELAY);
                        requestColorExercise();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
