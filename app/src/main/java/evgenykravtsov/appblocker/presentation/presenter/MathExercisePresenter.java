package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathExercise;
import evgenykravtsov.appblocker.domain.usecase.GetMathExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class MathExercisePresenter {

    public interface View {

        void showMathExercise(MathExercise mathExercise);

        void exerciseSolved();

        void notifyCheckResult(boolean solved);

        void finish();
    }

    ////

    protected View view;
    protected MathExercise mathExercise;

    private ExerciseSettings exerciseSettings;
    private SystemController systemController;
    private UseCaseThreadPool threadPool;

    ////

    public MathExercisePresenter(View view, UseCaseThreadPool threadPool) {
        this.view = view;
        this.threadPool = threadPool;

        exerciseSettings = DependencyInjection.provideExerciseSettings();
        systemController = DependencyInjection.provideSystemController();
    }

    ////

    public void setMathExercise(MathExercise mathExercise) {
        this.mathExercise = mathExercise;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestMathExercise() {
        threadPool.execute(UseCaseFactory.provideGetMathExerciseUseCase());
    }

    public void checkResult(int result) {
        if (result == mathExercise.getResult()) {
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
                        requestMathExercise();
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
    public void onEvent(GetMathExercise.Executed event) {
        setMathExercise(event.getMathExercise());
        view.showMathExercise(event.getMathExercise());
    }
}
