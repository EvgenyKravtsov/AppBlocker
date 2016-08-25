package evgenykravtsov.appblocker.presentation.presenter;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class TestPicturesExercisePresenter extends PicturesExercisePresenter {

    public TestPicturesExercisePresenter(View view) {
        super(view);
    }

    ////

    @Override
    public void checkResult(int chosenPictureIndex) {
        if (chosenPictureIndex == picturesExercise.getCorrectPictureIndex()) {
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(BlockerActivity.EXERCISE_CHANGE_DELAY);
                        requestPicturesExrcise();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
