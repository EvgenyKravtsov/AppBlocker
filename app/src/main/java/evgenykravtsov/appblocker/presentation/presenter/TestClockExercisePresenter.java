package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class TestClockExercisePresenter extends ClockExercisePresenter {

    public TestClockExercisePresenter(View view, UseCaseThreadPool threadPool) {
        super(view, threadPool);
    }

    ////

    @Override
    public void checkResult(int hours, int minutes) {
        if (hours == clockExercise.getHours() && minutes == clockExercise.getMinutes()) {
            view.notifyCheckResult(true);
            view.finish();
        } else view.notifyCheckResult(false);
    }
}
