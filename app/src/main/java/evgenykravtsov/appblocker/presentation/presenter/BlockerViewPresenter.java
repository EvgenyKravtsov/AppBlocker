package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.domain.usecase.GetExerciseType;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class BlockerViewPresenter {

    public interface View {

        void showExercise(ExerciseType exerciseType);
    }

    ////

    private View view;

    ////

    public BlockerViewPresenter(View view) {
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

    ////

    @Subscribe
    public void onEvent(GetExerciseType.Executed event) {
        view.showExercise(event.getExerciseType());
    }
}
