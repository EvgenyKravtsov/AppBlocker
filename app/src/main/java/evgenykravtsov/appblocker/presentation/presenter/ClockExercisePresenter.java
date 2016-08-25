package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.clock.ClockExercise;
import evgenykravtsov.appblocker.domain.usecase.GetClockExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class ClockExercisePresenter {

    public interface View {

        void showClockExercise(ClockExercise clockExercise);

        void exerciseSolved();

        void notifyCheckResult(boolean solved);

        void finish();
    }

    ////

    protected View view;
    protected ClockExercise clockExercise;

    private ExerciseSettings exerciseSettings;
    private SystemController systemController;
    private UseCaseThreadPool threadPool;

    ////

    public ClockExercisePresenter(View view, UseCaseThreadPool threadPool) {
        this.view = view;
        this.threadPool = threadPool;

        exerciseSettings = DependencyInjection.provideExerciseSettings();
        systemController = DependencyInjection.provideSystemController();
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
            view.notifyCheckResult(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(BlockerActivity.EXERCISE_CHANGE_DELAY);
                        threadPool.execute(UseCaseFactory.provideAllowAppUseCase());
                        view.exerciseSolved();
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
                        requestClockExercise();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void playSoundTip(SoundTipType soundTipType) {
        if (exerciseSettings.loadSoundSupportStatus()) systemController.playSoundTip(soundTipType);
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetClockExercise.Executed event) {
        setClockExercise(event.getClockExercise());
        view.showClockExercise(event.getClockExercise());
    }
}




















