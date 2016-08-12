package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesExercise;
import evgenykravtsov.appblocker.domain.usecase.GetPicturesExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class PicturesExercisePresenter {

    public interface View {

        void showPicturesExercise(PicturesExercise picturesExercise);

        void exerciseSolved();

        void notifyCheckResult(boolean solved);
    }

    ////

    private View view;
    private PicturesExercise picturesExercise;
    private UseCaseThreadPool threadPool;

    ////

    public PicturesExercisePresenter(View view) {
        this.view = view;
        threadPool = UseCaseThreadPool.getInstance();
    }

    ////

    public void setPicturesExercise(PicturesExercise picturesExercise) {
        this.picturesExercise = picturesExercise;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestPicturesExrcise() {
        threadPool.execute(UseCaseFactory.provideGetPicturesExerciseUseCase());
    }

    public void checkResult(int chosenPictureIndex) {
        if (chosenPictureIndex == picturesExercise.getCorrectPictureIndex()) {
            threadPool.execute(UseCaseFactory.provideAllowAppUseCase());
            view.notifyCheckResult(true);
            view.exerciseSolved();
        } else {
            view.notifyCheckResult(false);
            requestPicturesExrcise();
        }
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetPicturesExercise.Executed event) {
        setPicturesExercise(event.getPicturesExercise());
        view.showPicturesExercise(event.getPicturesExercise());
    }
}
