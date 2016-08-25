package evgenykravtsov.appblocker.presentation.presenter;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class TestMathExercisePresenter extends MathExercisePresenter {

    public TestMathExercisePresenter(View view, UseCaseThreadPool threadPool) {
        super(view, threadPool);
    }

    ////

    @Override
    public void checkResult(int result) {
        if (result == mathExercise.getResult()) {
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
                        requestMathExercise();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
