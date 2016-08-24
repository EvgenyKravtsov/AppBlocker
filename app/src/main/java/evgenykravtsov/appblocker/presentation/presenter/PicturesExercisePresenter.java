package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesExercise;
import evgenykravtsov.appblocker.domain.usecase.GetPicturesExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class PicturesExercisePresenter {

    public interface View {

        void showPicturesExercise(PicturesExercise picturesExercise);

        void exerciseSolved();

        void notifyCheckResult(boolean solved);

        void hideSoundButton();

        void finish();
    }

    ////

    protected View view;
    protected PicturesExercise picturesExercise;

    private SystemController systemController;
    private UseCaseThreadPool threadPool;
    private ExerciseSettings exerciseSettings;

    private boolean exerciseTypeTipPlayed;

    ////

    public PicturesExercisePresenter(View view) {
        this.view = view;
        threadPool = UseCaseThreadPool.getInstance();
        systemController = DependencyInjection.provideSystemController();
        exerciseSettings = DependencyInjection.provideExerciseSettings();

        if (!exerciseSettings.loadSoundSupportStatus()) view.hideSoundButton();
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

    public void playSoundTip(SoundTipType soundTipType) {
        if (exerciseSettings.loadSoundSupportStatus()) systemController.playSoundTip(soundTipType);
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetPicturesExercise.Executed event) {
        setPicturesExercise(event.getPicturesExercise());
        view.showPicturesExercise(event.getPicturesExercise());
        if (exerciseSettings.loadSoundSupportStatus() && !exerciseTypeTipPlayed) {
            playSoundTip(SoundTipType.OddPictureTip);
            exerciseTypeTipPlayed = true;
        }
    }
}
