package evgenykravtsov.appblocker.presentation.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.presenter.TestExercisePresenter;

public class TestExerciseActivity extends AppCompatActivity
        implements TestExercisePresenter.View {

    public static final String EXTRA_TEST_KEY = "extra_test_key";
    public static final int EXTRA_MATH_TO_TEST = 0;
    public static final int EXTRA_PICTURES_TO_TEST = 1;
    public static final int EXTRA_CLOCK_TO_TEST = 2;
    public static final int EXTRA_COLOR_TO_TEST = 3;
    public static final int EXTRA_MEMORY_TO_TEST = 4;

    private TestExercisePresenter presenter;

    private ExerciseType exerciseTypeToTest;

    ////

    @Override
    protected void onCreate(Bundle savedInstaneState) {
        super.onCreate(savedInstaneState);
        setContentView(R.layout.activity_test_exercise);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) finish();

        assert extras != null;
        int exerciseTypeCode = extras.getInt(EXTRA_TEST_KEY);

        switch (exerciseTypeCode) {
            case EXTRA_MATH_TO_TEST: exerciseTypeToTest = ExerciseType.Math; break;
            case EXTRA_PICTURES_TO_TEST: exerciseTypeToTest = ExerciseType.Pictures; break;
            case EXTRA_CLOCK_TO_TEST: exerciseTypeToTest = ExerciseType.Clock; break;
            case EXTRA_COLOR_TO_TEST: exerciseTypeToTest = ExerciseType.Color; break;
            case EXTRA_MEMORY_TO_TEST: exerciseTypeToTest = ExerciseType.Memory; break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.requestExercise(exerciseTypeToTest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void showExercise(Fragment exerciseFragment) {
        if (exerciseFragment != null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.test_exercise_activity_fragment_container, exerciseFragment).commit();
        }
    }

    ////

    private void bindPresenter() {
        presenter = new TestExercisePresenter(this);
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }
}
