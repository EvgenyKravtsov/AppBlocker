package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.domain.usecase.GetExerciseType;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class BlockerPresenter {

    public interface View {

        void showExercise(ExerciseType exerciseType);
    }

    ////

    private View view;

    ////

    public BlockerPresenter(View view) {
        this.view = view;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestExerciseType() {
        UseCaseThreadPool threadPool = UseCaseThreadPool.getInstance();
        threadPool.execute(UseCaseFactory.provideGetExerciseTypeUseCase());
    }

    public boolean ifSessionSolved(int numberOfSolvedExercies) {
        ExerciseSettings exerciseSettings = DependencyInjection.provideExerciseSettings();
        return numberOfSolvedExercies == exerciseSettings.loadSessionExerciseNumber();
    }

    ////

    @Subscribe
    public void onEvent(GetExerciseType.Executed event) {
        view.showExercise(event.getExerciseType());
    }
}
