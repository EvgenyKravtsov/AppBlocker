package evgenykravtsov.appblocker.presentation.presenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorExercise;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorType;
import evgenykravtsov.appblocker.domain.usecase.GetColorExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

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

    private boolean exerciseTypeTipPlayed;

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
                        exerciseTypeTipPlayed = false;
                        requestColorExercise();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void playSoundTip() {
        systemController.playSoundTip(soundTipType);
    }

    public void playCorrectnessSoundTip(boolean solved) {
        SoundTipType soundTipType = solved ? SoundTipType.RightAnswerTip : SoundTipType.WrongAnswerTip;
        if (exerciseSettings.loadSoundSupportStatus()) systemController.playSoundTip(soundTipType);
    }

    ////

    private SoundTipType prepareSoundTipTypeForColor(ColorType colorType) {
        switch (colorType) {
            case Red: return SoundTipType.ColorTipRed;
            case Orange: return SoundTipType.ColorTipOrange;
            case Yellow: return SoundTipType.ColorTipYellow;
            case Green: return SoundTipType.ColorTipGreen;
            case LightBlue: return SoundTipType.ColorTipLightBlue;
            case Blue: return SoundTipType.ColorTipBlue;
            case Purple: return SoundTipType.ColorTipPurple;
            case White: return SoundTipType.ColorTipWhite;
            case Gray: return SoundTipType.ColorTipGray;
            case Black: return SoundTipType.ColorTipBlack;
            case Pink: return SoundTipType.ColorTipPink;
            case Brown: return SoundTipType.ColorTipBrown;
            default: return SoundTipType.ColorTipRed;
        }
    }

    ////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetColorExercise.Executed event) {
        ColorExercise colorExercise = event.getColorExercise();
        setColorExercise(colorExercise);
        soundTipType = prepareSoundTipTypeForColor(colorExercise.getColorType());
        view.showColorExercise(colorExercise);
        if (exerciseSettings.loadSoundSupportStatus() && !exerciseTypeTipPlayed) {
            playSoundTip();
            exerciseTypeTipPlayed = true;
        }
    }
}





















