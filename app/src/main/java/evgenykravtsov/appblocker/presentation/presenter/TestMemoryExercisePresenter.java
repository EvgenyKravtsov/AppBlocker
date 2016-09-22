package evgenykravtsov.appblocker.presentation.presenter;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class TestMemoryExercisePresenter extends MemoryExercisePresenter {

    public TestMemoryExercisePresenter(View view) {
        super(view);
    }

    ////

    @Override
    public void onMemoryPictureChosen(int pictureIndex) {
        if (pictureIndex == memoryExercise.getCorrectPictureIndex()) {
            view.notifyCheckResult(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(BlockerActivity.EXERCISE_CHANGE_DELAY);
                        view.finish();
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
}
