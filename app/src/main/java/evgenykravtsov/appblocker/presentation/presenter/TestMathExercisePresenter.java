package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class TestMathExercisePresenter extends MathExercisePresenter {

    public TestMathExercisePresenter(View view, UseCaseThreadPool threadPool) {
        super(view, threadPool);
    }

    ////

    @Override
    public void checkResult(int result) {
        if (result == mathExercise.getResult()) {
            view.notifyCheckResult(true);
            view.finish();
        } else view.notifyCheckResult(false);
    }
}
