package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.clock.ClockExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.presenter.ClockExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestClockExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class ClockExerciseFragment extends Fragment
        implements ClockExercisePresenter.View {

    private ClockExercisePresenter presenter;

    private LinearLayout mainLayout;
    private ImageView hourArrowImageView;
    private ImageView minuteArrowImageView;
    private EditText hoursEditText;
    private EditText minutesEditText;
    private Button checkButton;

    private int mode;

    ////

    public static ClockExerciseFragment newInstance(int mode) {
        ClockExerciseFragment fragment = new ClockExerciseFragment();

        Bundle args = new Bundle();
        args.putInt(BlockerActivity.KEY_EXERCISE_FRAGMENT_MODE, mode);
        fragment.setArguments(args);

        return fragment;
    }

    ////

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater,
                                          ViewGroup container,
                                          Bundle savedInstanceState) {

        mode = getArguments().getInt(BlockerActivity.KEY_EXERCISE_FRAGMENT_MODE);

        android.view.View layout = inflater.inflate(R.layout.fragment_clock_exercise, container, false);
        bindViews(layout);
        bindViewListeners();
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.requestClockExercise();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void showClockExercise(ClockExercise clockExercise) {
        hourArrowImageView.setRotation(
                clockExercise.getHours() * 30 + 0.25f * clockExercise.getMinutes());
        minuteArrowImageView.setRotation(clockExercise.getMinutes() * 6);
    }

    @Override
    public void exerciseSolved() {
        ((BlockerActivity) getActivity()).solveExercise();
    }

    @Override
    public void notifyCheckResult(boolean solved) {
        showCorrectnessSnackbar(solved);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    ////

    private void bindPresenter() {
        switch (mode) {
            case BlockerActivity.MODE_STANDARD:
                presenter = new ClockExercisePresenter(this, UseCaseThreadPool.getInstance());
                break;
            case BlockerActivity.MODE_TEST:
                presenter = new TestClockExercisePresenter(this, UseCaseThreadPool.getInstance());
                break;
        }

        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void bindViews(android.view.View layout) {
        mainLayout = (LinearLayout) layout
                .findViewById(R.id.clock_exercise_fragment_main_layout);
        hourArrowImageView = (ImageView) layout
                .findViewById(R.id.clock_exercise_fragment_hour_arrow_image_view);
        minuteArrowImageView = (ImageView) layout
                .findViewById(R.id.clock_exercise_fragment_minute_arrow_image_view);
        hoursEditText = (EditText) layout
                .findViewById(R.id.clock_exercise_fragment_hours_edit_text);
        minutesEditText = (EditText) layout
                .findViewById(R.id.clock_exercise_fragment_minutes_edit_text);
        checkButton = (Button) layout
                .findViewById(R.id.clock_exercise_fragment_check_button);
    }

    private void bindViewListeners() {
        checkButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                String hours = hoursEditText.getText().toString();
                String minutes = minutesEditText.getText().toString();

                if (!hours.equals("") && !minutes.equals("")) {
                    presenter.checkResult(
                            Integer.parseInt(hours),
                            Integer.parseInt(minutes));
                }
            }
        });
    }

    private void showCorrectnessSnackbar(boolean solved) {
        String message = solved ? "Correct!" : "Incorrect!";
        Drawable icon = solved ?
                getResources().getDrawable(R.drawable.block_control_on_button_icon) :
                getResources().getDrawable(R.drawable.block_control_off_button_icon);

        Snackbar snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT);
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ImageView imageView = new ImageView(getActivity());
        imageView.setImageDrawable(icon);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(layoutParams);

        ((Snackbar.SnackbarLayout) snackbarLayout).addView(imageView);

        snackbar.show();
    }
}
