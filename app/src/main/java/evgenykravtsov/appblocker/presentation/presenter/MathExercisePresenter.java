package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathExercise;
import evgenykravtsov.appblocker.domain.usecase.GetMathExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

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
            threadPool.execute(UseCaseFactory.provideAllowAppUseCase());
            view.notifyCheckResult(true);
            view.exerciseSolved();
        } else view.notifyCheckResult(false);
    }

    public void playSoundTip(SoundTipType soundTipType) {
        if (exerciseSettings.loadSoundSupportStatus()) systemController.playSoundTip(soundTipType);
    }

    ////

    @Subscribe
    public void onEvent(GetMathExercise.Executed event) {
        setMathExercise(event.getMathExercise());
        view.showMathExercise(event.getMathExercise());
    }
}
