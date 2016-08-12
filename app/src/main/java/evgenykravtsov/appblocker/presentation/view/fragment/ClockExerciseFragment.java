package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.clock.ClockExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.presenter.ClockExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class ClockExerciseFragment extends Fragment
        implements ClockExercisePresenter.View {

    private ClockExercisePresenter presenter;

    private ImageView hourArrowImageView;
    private ImageView minuteArrowImageView;
    private EditText hoursEditText;
    private EditText minutesEditText;
    private Button checkButton;

    ////

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater,
                                          ViewGroup container,
                                          Bundle savedInstanceState) {
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
        String message = solved ? "Correct!" : "Incorrect!";
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    ////

    private void bindPresenter() {
        presenter = new ClockExercisePresenter(this, UseCaseThreadPool.getInstance());
        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void bindViews(android.view.View layout) {
        hourArrowImageView = (ImageView)
                layout.findViewById(R.id.clock_exercise_fragment_hour_arrow_image_view);
        minuteArrowImageView = (ImageView)
                layout.findViewById(R.id.clock_exercise_fragment_minute_arrow_image_view);
        hoursEditText = (EditText)
                layout.findViewById(R.id.clock_exercise_fragment_hours_edit_text);
        minutesEditText = (EditText)
                layout.findViewById(R.id.clock_exercise_fragment_minutes_edit_text);
        checkButton = (Button)
                layout.findViewById(R.id.clock_exercise_fragment_check_button);
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
}
