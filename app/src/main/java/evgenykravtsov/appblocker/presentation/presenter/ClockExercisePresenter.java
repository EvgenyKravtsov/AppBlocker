package evgenykravtsov.appblocker.presentation.presenter;

import android.util.Log;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import evgenykravtsov.appblocker.domain.model.exercise.clock.ClockExercise;
import evgenykravtsov.appblocker.domain.usecase.GetClockExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class ClockExercisePresenter {

    public interface View {

        void showClockExercise(ClockExercise clockExercise);

        void exerciseSolved();
    }

    ////

    private View view;
    private ClockExercise clockExercise;
    private UseCaseThreadPool threadPool;

    ////

    public ClockExercisePresenter(View view, UseCaseThreadPool threadPool) {
        this.view = view;
        this.threadPool = threadPool;
    }

    ////

    public void setClockExercise(ClockExercise clockExercise) {
        this.clockExercise = clockExercise;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestClockExercise() {
        threadPool.execute(UseCaseFactory.provideGetClockExerciseUseCase());
    }

    public void checkResult(int hours, int minutes) {
        if (hours == clockExercise.getHours() && minutes == clockExercise.getMinutes()) {
            threadPool.execute(UseCaseFactory.provideAllowAppUseCase());
            view.exerciseSolved();
        }
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetClockExercise.Executed event) {
        setClockExercise(event.getClockExercise());
        view.showClockExercise(event.getClockExercise());
    }
}
