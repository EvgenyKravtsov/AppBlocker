package evgenykravtsov.appblocker.presentation.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.presenter.BlockerPresenter;
import evgenykravtsov.appblocker.presentation.view.fragment.ClockExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.ColorExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.MathExerciseFragment;
import evgenykravtsov.appblocker.presentation.view.fragment.PictureExerciseFragment;

public class BlockerActivity extends AppCompatActivity implements BlockerPresenter.View {

    public static final String KEY_EXERCISE_FRAGMENT_MODE = "key_exercise_fragment_mode";
    public static final int MODE_STANDARD = 0;
    public static final int MODE_TEST = 1;
    public static final int CORRECTNESS_ANIMATION_DURATION = 400;
    public static final int EXERCISE_CHANGE_DELAY = 800;

    private BlockerPresenter presenter;

    private int numberOfSolvedExericies;

    ////

    public void solveExercise() {
        numberOfSolvedExericies++;
        if (presenter.ifSessionSolved(numberOfSolvedExericies)) finish();
        else showNewExercise();
    }

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocker);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.requestExerciseType();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void showExercise(ExerciseType exerciseType) {
        Fragment fragment = null;

        switch (exerciseType) {
            case Math:
                fragment = MathExerciseFragment.newInstance(MODE_STANDARD);
                break;
            case Pictures:
                fragment = PictureExerciseFragment.newInstance(MODE_STANDARD);
                break;
            case Clock:
                fragment = ClockExerciseFragment.newInstance(MODE_STANDARD);
                break;
            case Color:
                fragment = ColorExerciseFragment.newInstance(MODE_STANDARD);
                break;
        }

        getFragmentManager().beginTransaction()
                .add(R.id.blocker_activity_fragment_container, fragment).commit();
    }

    ////

    private void bindPresenter() {
        presenter = new BlockerPresenter(this);
        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void showNewExercise() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(
                fragmentManager.findFragmentById(R.id.blocker_activity_fragment_container)
        ).commit();

        presenter.requestExerciseType();
    }
}
