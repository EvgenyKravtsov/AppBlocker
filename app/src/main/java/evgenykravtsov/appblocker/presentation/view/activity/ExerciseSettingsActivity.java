package evgenykravtsov.appblocker.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.Locale;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.presenter.ExerciseSettingsPresenter;

public class ExerciseSettingsActivity extends AppCompatActivity
        implements ExerciseSettingsPresenter.View {

    // TODO Add setting of count of exercises for one block session

    private ExerciseSettingsPresenter presenter;
    private ExerciseSettings exerciseSettings;

    private EditText numberEditText;
    private Button mathButton;
    private CheckBox mathCheckBox;
    private FrameLayout mathLayout;
    private Button picturesButton;
    private CheckBox picturesCheckBox;
    private Button clockButton;
    private CheckBox clockCheckBox;
    private Button colorButton;
    private CheckBox colorCheckBox;

    private int activatedExerciseTypesCount;

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_settings);

        exerciseSettings = DependencyInjection.provideExerciseSettings();

        bindViews();
        bindViewListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        establishInitialViewsState();
    }

    @Override
    protected void onStop() {
        super.onStop();

        presenter.setSessionExerciseNumber(
                Integer.parseInt(numberEditText.getText().toString())
        );

        unbindPresenter();
    }

    ////

    private void bindPresenter() {
        presenter = new ExerciseSettingsPresenter(this);
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }

    private void bindViews() {
        numberEditText = (EditText) findViewById(R.id.exercise_settings_activity_number_edit_text);

        mathButton = (Button) findViewById(R.id.exercise_settings_activity_math_button);

        mathCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_math_check_box);
        boolean mathStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Math);
        if (mathStatus) activatedExerciseTypesCount++;
        mathCheckBox.setChecked(mathStatus);

        mathLayout = (FrameLayout) findViewById(R.id.exercise_settings_activity_math_layout);

        picturesButton = (Button) findViewById(R.id.exercise_settings_activity_pictures_button);

        picturesCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_pictures_check_box);
        boolean picturesStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Pictures);
        if (picturesStatus) activatedExerciseTypesCount++;
        picturesCheckBox.setChecked(picturesStatus);

        clockButton = (Button) findViewById(R.id.exercise_settings_activity_clock_button);

        clockCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_clock_check_box);
        boolean clockStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Clock);
        if (clockStatus) activatedExerciseTypesCount++;
        clockCheckBox.setChecked(clockStatus);

        colorButton = (Button) findViewById(R.id.exercise_settings_activity_color_button);

        colorCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_color_check_box);
        boolean colorStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Color);
        if (colorStatus) activatedExerciseTypesCount++;
        colorCheckBox.setChecked(colorStatus);
    }

    private void bindViewListeners() {
        mathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = mathLayout.getVisibility();
                if (visibility == View.GONE) mathLayout.setVisibility(View.VISIBLE);
                if (visibility == View.VISIBLE) mathLayout.setVisibility(View.GONE);
            }
        });

        mathCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (activatedExerciseTypesCount == 1) {
                        // TODO Notify user
                        mathCheckBox.setChecked(true);
                        return;
                    }

                    activatedExerciseTypesCount--;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Math, false);
                } else {
                    activatedExerciseTypesCount++;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Math, true);
                }
            }
        });

        picturesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (activatedExerciseTypesCount == 1) {
                        // TODO Notify user
                        picturesCheckBox.setChecked(true);
                        return;
                    }

                    activatedExerciseTypesCount--;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Pictures, false);
                } else {
                    activatedExerciseTypesCount++;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Pictures, true);
                }
            }
        });

        clockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (activatedExerciseTypesCount == 1) {
                        // TODO Notify user
                        clockCheckBox.setChecked(true);
                        return;
                    }

                    activatedExerciseTypesCount--;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Clock, false);
                } else {
                    activatedExerciseTypesCount++;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Clock, true);
                }
            }
        });

        colorCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (activatedExerciseTypesCount == 1) {
                        // TODO Notify user
                        colorCheckBox.setChecked(true);
                        return;
                    }

                    activatedExerciseTypesCount--;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Color, false);
                } else {
                    activatedExerciseTypesCount++;
                    exerciseSettings.saveExerciseTypeStatus(ExerciseType.Color, true);
                }
            }
        });
    }

    private void establishInitialViewsState() {
        numberEditText.setText(
                String.format(Locale.ROOT, "%d",presenter.getSessionExerciseNumber()));
    }
}







































