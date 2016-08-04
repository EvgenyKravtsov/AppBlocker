package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;

import evgenykravtsov.appblocker.domain.model.exercise.math.MathExercise;
import evgenykravtsov.appblocker.domain.usecase.GetMathExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class MathExercisePresenter {

    public interface View {

        void showMathExercise(MathExercise mathExercise);

        void finish();
    }

    ////

    private View view;
    private MathExercise mathExercise;
    private UseCaseThreadPool threadPool;

    ////

    public MathExercisePresenter(View view, UseCaseThreadPool threadPool) {
        this.view = view;
        this.threadPool = threadPool;
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
            view.finish();
        }
    }

    ////

    @Subscribe
    public void onEvent(GetMathExercise.Executed event) {
        setMathExercise(event.getMathExercise());
        view.showMathExercise(event.getMathExercise());
    }
}
