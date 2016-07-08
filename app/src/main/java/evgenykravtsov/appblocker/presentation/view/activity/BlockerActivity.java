package evgenykravtsov.appblocker.presentation.view.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.presenter.BlockerViewPresenter;
import evgenykravtsov.appblocker.presentation.view.fragment.MathExerciseFragment;

public class BlockerActivity extends AppCompatActivity implements BlockerViewPresenter.View {

    private BlockerViewPresenter presenter;

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
        }

        getFragmentManager().beginTransaction()
                .add(R.id.blocker_activity_fragment_container, fragment).commit();
    }

    ////

    private void bindPresenter() {
        presenter = new BlockerViewPresenter(this);
        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }
}
