package evgenykravtsov.appblocker.presentation.view.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.billing.BillingSettings;
import evgenykravtsov.appblocker.billing.IabHelper;
import evgenykravtsov.appblocker.billing.IabResult;
import evgenykravtsov.appblocker.billing.Inventory;
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
    private Button unlockPicturesButton;
    private Button unlockClockButton;

    private int activatedExerciseTypesCount;

    private IabHelper iabHelper;
    private BillingSettings billingSettings;

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
        billingSettings = DependencyInjection.provideBillingSettings();

        prepareActionBar();
        bindViews();
        bindViewListeners();

        prepareIab();
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
    protected void onDestroy() {
        super.onDestroy();
        if (iabHelper != null)
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }

        iabHelper = null;
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

    @Override
    public void showExerciseSettingsTip() {
        showOnboardingDialog(getString(R.string.exercise_settings_screen_exercises_tip_text), null);
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

        unlockPicturesButton = (Button) findViewById(R.id.exercise_settings_activity_unlock_pictures_button);
        unlockClockButton = (Button) findViewById(R.id.exercise_settings_activity_unlock_clock_button);
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
                if (!presenter.isAppShared()) {
                    showShareMathDialog();
                    return;
                }

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
                        showSnackbar(getString(R.string.exercise_settings_screen_one_type_warning));
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
                        showSnackbar(getString(R.string.exercise_settings_screen_one_type_warning));
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
                if (!billingSettings.loadClockExercisePurchaseStatus()) {
                    showPurchaseDialog(ExerciseType.Clock);
                    clockCheckBox.setChecked(!checked);
                    return;
                }

                if (!checked) {
                    if (activatedExerciseTypesCount == 1) {
                        showSnackbar(getString(R.string.exercise_settings_screen_one_type_warning));
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
                        showSnackbar(getString(R.string.exercise_settings_screen_one_type_warning));
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

        unlockPicturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engagePurchase(ExerciseType.Pictures);
            }
        });

        unlockClockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engagePurchase(ExerciseType.Clock);
            }
        });
    }

    private void prepareIab() {
        String base64EncodedPublicKey;
        base64EncodedPublicKey = BillingSettings.KEY_1
                + BillingSettings.KEY_2
                + BillingSettings.KEY_3
                + BillingSettings.KEY_4
                + BillingSettings.KEY_5
                + BillingSettings.KEY_6
                + BillingSettings.KEY_7;
        iabHelper = new IabHelper(this, base64EncodedPublicKey);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) Log.d("debug", "error initializing iabHelper");
                else {
                    Log.d("debug", "iabHelper success");

                    List<String> skuList = new ArrayList<>();
                    skuList.add(BillingSettings.ODD_PICTURE_EXERCISE_SKU);
                    skuList.add(BillingSettings.CLOCK_EXERCISE_SKU);

                    try {
                        iabHelper.queryInventoryAsync(true, skuList, null, new IabHelper.QueryInventoryFinishedListener() {

                            @Override
                            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                onPlayStoreInventoryReceived(result, inv);
                            }
                        });
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void establishInitialViewsState() {
        parentPasswordCheckBox.setChecked(presenter.getPasswordActivationStatus());
        soundSupportCheckBox.setChecked(presenter.getSoundSupportStatus());
        numberEditText.setText(String.format(Locale.ROOT, "%d", presenter.getSessionExerciseNumber()));
    }

    private void navigateToTestExerciseActivity(ExerciseType exerciseToTest) {
        int exerciseTypeCode = 5;

        switch (exerciseToTest) {
            case Math:
                exerciseTypeCode = TestExerciseActivity.EXTRA_MATH_TO_TEST;
                break;
            case Pictures:
                exerciseTypeCode = TestExerciseActivity.EXTRA_PICTURES_TO_TEST;
                break;
            case Clock:
                exerciseTypeCode = TestExerciseActivity.EXTRA_CLOCK_TO_TEST;
                break;
            case Color:
                exerciseTypeCode = TestExerciseActivity.EXTRA_COLOR_TO_TEST;
                break;
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
        showSnackbar(getString(R.string.exercise_settings_screen_password_dont_match));
    }

    private void notifyPasswordSaved() {
        showSnackbar(getString(R.string.exercise_settings_screen_password_saved));
    }

    private void showOnboardingDialog(String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, positiveListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //// Control callbacks

    private void onParentPasswordStateChanged() {
        if (!presenter.getPasswordSetStatus()) showSetPasswordDialog();
        else presenter.setPasswordActivationStatus(parentPasswordCheckBox.isChecked());
    }

    private void showShareMathDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.exercise_settings_share_math_text))
                .setPositiveButton(getString(R.string.exercise_settings_screen_share),
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share"));
                        presenter.appHasBeenShared();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPurchaseDialog(final ExerciseType exerciseType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.exercise_settings_purchase_text))
                .setPositiveButton(getString(R.string.exercise_settings_purchase_dialog_button_label),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                engagePurchase(exerciseType);
                                dialogInterface.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onPlayStoreInventoryReceived(IabResult result, Inventory inv) {
        boolean oddPictureExercisePurchsed;
        boolean clockExercisePurchased;

        if (result.isFailure()) {
            oddPictureExercisePurchsed = billingSettings.loadOddPicturePurchaseStatus();
            clockExercisePurchased = billingSettings.loadClockExercisePurchaseStatus();
        } else {
            oddPictureExercisePurchsed = inv.hasPurchase(BillingSettings.ODD_PICTURE_EXERCISE_SKU);
            clockExercisePurchased = inv.hasPurchase(BillingSettings.CLOCK_EXERCISE_SKU);
            billingSettings.saveOddPicturePurchaseStatus(oddPictureExercisePurchsed);
            billingSettings.saveClockExercisePurchaseStatus(clockExercisePurchased);
        }

        if (oddPictureExercisePurchsed) unlockPicturesButton.setVisibility(View.GONE);
        if (clockExercisePurchased) unlockClockButton.setVisibility(View.GONE);
    }

    private void engagePurchase(ExerciseType exerciseType) {
        String sku = "";

        switch (exerciseType) {
            case Pictures: sku = BillingSettings.ODD_PICTURE_EXERCISE_SKU; break;
            case Clock: sku = BillingSettings.CLOCK_EXERCISE_SKU; break;
        }

        try {
            iabHelper.launchPurchaseFlow(
                    ExerciseSettingsActivity.this,
                    sku,
                    BillingSettings.PURCHASE_REQUEST_CODE,
                    null,
                    "");
        } catch (Exception e) {
            e.printStackTrace();

            // TODO Delete test code
            Log.d("debug", "Error during purchase process");
        }
    }
}








































