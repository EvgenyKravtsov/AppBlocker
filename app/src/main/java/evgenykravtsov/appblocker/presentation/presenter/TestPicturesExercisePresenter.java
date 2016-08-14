package evgenykravtsov.appblocker.presentation.presenter;

import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;

public class TestPicturesExercisePresenter extends PicturesExercisePresenter {

    public TestPicturesExercisePresenter(View view) {
        super(view);
    }

    ////

    @Override
    public void checkResult(int chosenPictureIndex) {
        if (chosenPictureIndex == picturesExercise.getCorrectPictureIndex()) {
            view.notifyCheckResult(true);
            view.finish();
        } else {
            view.notifyCheckResult(false);
            requestPicturesExrcise();
        }
    }
}
