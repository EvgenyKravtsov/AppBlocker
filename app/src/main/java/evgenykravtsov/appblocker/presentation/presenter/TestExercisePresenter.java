package evgenykravtsov.appblocker.presentation.presenter;

import android.app.Fragment;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;
import evgenykravtsov.appblocker.presentation.view.fragment.ClockExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.ColorExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.MathExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.MemoryExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.PictureExerciseFragment;

public class TestExercisePresenter {

    public interface View {

        void showExercise(Fragment exerciseFragment);
    }

    ////

    private View view;

    ////

    public TestExercisePresenter(View view) {
        this.view = view;
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void requestExercise(ExerciseType exerciseType) {
        Fragment fragment = null;

        switch (exerciseType) {
            case Math:
                fragment = MathExerciseFragment.newInstance(BlockerActivity.MODE_TEST);
                break;
            case Pictures:
                fragment = PictureExerciseFragment.newInstance(BlockerActivity.MODE_TEST);
                break;
            case Clock:
                fragment = ClockExerciseFragment.newInstance(BlockerActivity.MODE_TEST);
                break;
            case Color:
                fragment = ColorExerciseFragment.newInstance(BlockerActivity.MODE_TEST);
                break;
            case Memory:
                fragment = MemoryExerciseFragment.newInstance(BlockerActivity.MODE_TEST);
                break;
        }

        view.showExercise(fragment);
    }
}
