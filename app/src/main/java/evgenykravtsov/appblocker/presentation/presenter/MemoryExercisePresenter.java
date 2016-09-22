package evgenykravtsov.appblocker.presentation.presenter;

import android.app.Fragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemoryExercise;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemorySettings;
import evgenykravtsov.appblocker.domain.usecase.GetMemoryExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.adapter.MemoryExercisePitureListAdapter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class MemoryExercisePresenter {

    public interface View {

        void showMemoryExercise(MemoryExercise memoryExercise);

        void revealSpecifiedPicture();

        void exerciseSolved();

        void notifyCheckResult(boolean solved);

        void finish();
    }

    ////

    protected View view;
    protected MemoryExercisePitureListAdapter adapter;
    protected MemoryExercise memoryExercise;
    protected MemorySettings memorySettings;
    protected ExerciseSettings exerciseSettings;
    protected SystemController systemController;
    protected int mistakes;

    private UseCaseThreadPool threadPool;

    ////

    public MemoryExercisePresenter(View view) {
        this.view = view;
        memorySettings = DependencyInjection.provideMemorySettings();
        exerciseSettings = DependencyInjection.provideExerciseSettings();
        systemController = DependencyInjection.provideSystemController();
        threadPool = UseCaseThreadPool.getInstance();
    }

    ////

    public void setAdapter(MemoryExercisePitureListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setMemoryExercise(MemoryExercise memoryExercise) {
        this.memoryExercise = memoryExercise;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestMemoryExercise() {
        threadPool.execute(UseCaseFactory.provideGetMemoryExerciseUseCase());
    }

    public void onMemoryPictureChosen(int pictureIndex) {
        if (pictureIndex == memoryExercise.getCorrectPictureIndex()) {
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
            mistakes++;
            if (mistakes > memorySettings.loadNumberOfMistakes())
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(BlockerActivity.EXERCISE_CHANGE_DELAY);
                            requestMemoryExercise();
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
    public void onEvent(GetMemoryExercise.Executed event) {
        setMemoryExercise(event.getMemoryExercise());
        mistakes = 0;

        view.showMemoryExercise(event.getMemoryExercise());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(memorySettings.loadDisplayTime());
                    ((Fragment) view).getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.hideMemoryPictures();
                            view.revealSpecifiedPicture();
                        }
                    });
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }).start();
    }
}
