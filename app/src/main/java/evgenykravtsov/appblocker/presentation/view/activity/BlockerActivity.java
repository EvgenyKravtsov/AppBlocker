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

    // TODO Write getInstance method for all fragments

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
                fragment = new MathExerciseFragment();
                break;
            case Pictures:
                fragment = new PictureExerciseFragment();
                break;
            case Clock:
                fragment = new ClockExerciseFragment();
                break;
            case Color:
                fragment = new ColorExerciseFragment();
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
