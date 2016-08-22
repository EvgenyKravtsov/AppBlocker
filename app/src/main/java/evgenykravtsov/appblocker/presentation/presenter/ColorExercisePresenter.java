package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorExercise;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorType;
import evgenykravtsov.appblocker.domain.usecase.GetColorExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class ColorExercisePresenter {

    public interface View {

        void showColorExercise(ColorExercise colorExercise);

        void exerciseSolved();

        void notifyCheckResult(boolean solved);

        void finish();

        void hideSoundButton();
    }

    ////

    protected View view;
    protected ColorExercise colorExercise;

    private UseCaseThreadPool threadPool;
    private SystemController systemController;
    private SoundTipType soundTipType;
    private ExerciseSettings exerciseSettings;

    ////

    public ColorExercisePresenter(
            View view,
            UseCaseThreadPool threadPool,
            SystemController systemController) {

        this.view = view;
        this.threadPool = threadPool;
        this.systemController = systemController;

        exerciseSettings = DependencyInjection.provideExerciseSettings();

        if (!exerciseSettings.loadSoundSupportStatus()) view.hideSoundButton();
    }

    ////

    public void setColorExercise(ColorExercise colorExercise) {
        this.colorExercise = colorExercise;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestColorExercise() {
        threadPool.execute(UseCaseFactory.provideGetColorExerciseUseCase());
    }

    public void checkResult(ColorType colorType) {
        if (colorType == colorExercise.getColorType()) {
            threadPool.execute(UseCaseFactory.provideAllowAppUseCase());
            view.notifyCheckResult(true);
            view.exerciseSolved();
        } else {
            view.notifyCheckResult(false);
            requestColorExercise();
        }
    }

    public void playSoundTip() {
        systemController.playSoundTip(soundTipType);
    }

    ////

    private SoundTipType prepareSoundTipTypeForColor(ColorType colorType) {
        switch (colorType) {
            case Red:
                return SoundTipType.ColorTipRed;
            case Orange:
                return SoundTipType.ColorTipOrange;
            case Yellow:
                return SoundTipType.ColorTipYellow;
            case Green:
                return SoundTipType.ColorTipGreen;
            case LightBlue:
                return SoundTipType.ColorTipLightBlue;
            case Blue:
                return SoundTipType.ColorTipBlue;
            case Purple:
                return SoundTipType.ColorTipPurple;
            case White:
                return SoundTipType.ColorTipWhite;
            case Gray:
                return SoundTipType.ColorTipGray;
            case Black:
                return SoundTipType.ColorTipBlack;
            default:
                return null;
        }
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetColorExercise.Executed event) {
        ColorExercise colorExercise = event.getColorExercise();
        setColorExercise(colorExercise);
        soundTipType = prepareSoundTipTypeForColor(colorExercise.getColorType());
        view.showColorExercise(colorExercise);
        if (exerciseSettings.loadSoundSupportStatus()) playSoundTip();
    }
}





















