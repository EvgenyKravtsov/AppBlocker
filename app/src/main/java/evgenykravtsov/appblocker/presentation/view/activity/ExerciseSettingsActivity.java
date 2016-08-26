package evgenykravtsov.appblocker.presentation.view.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;

import java.util.Locale;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.presenter.ExerciseSettingsPresenter;

public class ExerciseSettingsActivity extends AppCompatActivity
        implements ExerciseSettingsPresenter.View {

    private ExerciseSettingsPresenter presenter;
    private ExerciseSettings exerciseSettings;

    private ScrollView mainLayout;
    private EditText numberEditText;
    private CheckBox parentPasswordCheckBox;
    private CheckBox soundSupportCheckBox;
    private ImageButton expandMathButton;
    private CheckBox mathCheckBox;
    private FrameLayout mathLayout;
    private CheckBox picturesCheckBox;
    private CheckBox clockCheckBox;
    private CheckBox colorCheckBox;
    private Button testMathButton;
    private Button testPicturesButton;
    private Button testClockButton;
    private Button testColorButton;

    private int activatedExerciseTypesCount;

    ////

    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT);
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_settings);

        exerciseSettings = DependencyInjection.provideExerciseSettings();

        prepareActionBar();
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
    protected void onPause() {
        super.onPause();
        presenter.setSessionExerciseNumber(
                Integer.parseInt(numberEditText.getText().toString())
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////

    private void bindPresenter() {
        presenter = new ExerciseSettingsPresenter(this);
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }

    private void prepareActionBar() {
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindViews() {
        mainLayout = (ScrollView) findViewById(R.id.exercise_settings_activity_main_layout);

        numberEditText = (EditText) findViewById(R.id.exercise_settings_activity_number_edit_text);

        parentPasswordCheckBox = (CheckBox)
                findViewById(R.id.exercise_settings_activity_parent_password_check_box);

        soundSupportCheckBox = (CheckBox)
                findViewById(R.id.exercise_settings_activity_sound_support_check_box);

        expandMathButton = (ImageButton) findViewById(R.id.exercise_settings_activity_expand_math_button);

        mathCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_math_check_box);
        boolean mathStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Math);
        if (mathStatus) activatedExerciseTypesCount++;
        mathCheckBox.setChecked(mathStatus);

        mathLayout = (FrameLayout) findViewById(R.id.exercise_settings_activity_math_layout);

        picturesCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_pictures_check_box);
        boolean picturesStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Pictures);
        if (picturesStatus) activatedExerciseTypesCount++;
        picturesCheckBox.setChecked(picturesStatus);

        clockCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_clock_check_box);
        boolean clockStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Clock);
        if (clockStatus) activatedExerciseTypesCount++;
        clockCheckBox.setChecked(clockStatus);

        colorCheckBox = (CheckBox) findViewById(R.id.exercise_settings_activity_color_check_box);
        boolean colorStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Color);
        if (colorStatus) activatedExerciseTypesCount++;
        colorCheckBox.setChecked(colorStatus);

        testMathButton = (Button) findViewById(R.id.exrcise_settings_activity_test_math_button);
        testPicturesButton = (Button) findViewById(R.id.exrcise_settings_activity_test_pictures_button);
        testClockButton = (Button) findViewById(R.id.exrcise_settings_activity_test_clock_button);
        testColorButton = (Button) findViewById(R.id.exrcise_settings_activity_test_color_button);
    }

    private void bindViewListeners() {
        parentPasswordCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onParentPasswordStateChanged();
            }
        });

        soundSupportCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                presenter.setSoundSupportStatus(checked);
            }
        });

        expandMathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandMathButton.setRotation(180);
                int visibility = mathLayout.getVisibility();
                if (visibility == View.GONE) {
                    expandMathButton.setRotation(180);
                    mathLayout.setVisibility(View.VISIBLE);
                    mainLayout.scrollTo(0, expandMathButton.getBottom());
                }
                if (visibility == View.VISIBLE) {
                    expandMathButton.setRotation(0);
                    mainLayout.scrollTo(expandMathButton.getBottom(), mathCheckBox.getBottom());
                    mathLayout.setVisibility(View.GONE);
                }
            }
        });

        mathCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (activatedExerciseTypesCount == 1) {
                        showSnackbar("At least one type should be activated");
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
                        showSnackbar("At least one type should be activated");
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
                        showSnackbar("At least one type should be activated");
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
                        showSnackbar("At least one type should be activated");
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

        testMathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTestExerciseActivity(ExerciseType.Math);
            }
        });

        testPicturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTestExerciseActivity(ExerciseType.Pictures);
            }
        });

        testClockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTestExerciseActivity(ExerciseType.Clock);
            }
        });

        testColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToTestExerciseActivity(ExerciseType.Color);
            }
        });
    }

    private void establishInitialViewsState() {
        parentPasswordCheckBox.setChecked(presenter.getPasswordActivationStatus());
        soundSupportCheckBox.setChecked(presenter.getSoundSupportStatus());
        numberEditText.setText(
                String.format(Locale.ROOT, "%d",presenter.getSessionExerciseNumber()));
    }

    private void navigateToTestExerciseActivity(ExerciseType exerciseToTest) {
        int exerciseTypeCode = 5;

        switch (exerciseToTest) {
            case Math: exerciseTypeCode = TestExerciseActivity.EXTRA_MATH_TO_TEST; break;
            case Pictures: exerciseTypeCode = TestExerciseActivity.EXTRA_PICTURES_TO_TEST; break;
            case Clock: exerciseTypeCode = TestExerciseActivity.EXTRA_CLOCK_TO_TEST; break;
            case Color: exerciseTypeCode = TestExerciseActivity.EXTRA_COLOR_TO_TEST; break;
        }

        Intent intent = new Intent(this, TestExerciseActivity.class);
        intent.putExtra(TestExerciseActivity.EXTRA_TEST_KEY, exerciseTypeCode);
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    private void showSetPasswordDialog() {
        View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_set_password, null);

        final EditText passwordEditText = (EditText)
                dialogLayout.findViewById(R.id.set_password_dialog_password_edit_text);
        final EditText controlPasswordEditText = (EditText)
                dialogLayout.findViewById(R.id.set_password_dialog_control_password_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(dialogLayout)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        parentPasswordCheckBox.setChecked(presenter.getPasswordSetStatus());
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                String controlPassword = controlPasswordEditText.getText().toString();

                if (!password.equals(controlPassword))
                    notifyPasswordsDontMatch();
                else if (password.equals("") && controlPassword.equals(""))
                    notifyPasswordsDontMatch();
                else {
                    notifyPasswordSaved();
                    presenter.setPassword(password);
                    parentPasswordCheckBox.setChecked(presenter.getPasswordSetStatus());
                    dialog.dismiss();
                }
            }
        });
    }

    private void notifyPasswordsDontMatch() {
        showSnackbar("Passwords dont match");
    }

    private void notifyPasswordSaved() {
        showSnackbar("Password saved");
    }

    //// Control callbacks

    private void onParentPasswordStateChanged() {
        if (!presenter.getPasswordSetStatus()) showSetPasswordDialog();
        else presenter.setPasswordActivationStatus(parentPasswordCheckBox.isChecked());
    }
}








































