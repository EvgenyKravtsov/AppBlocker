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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.presenter.MathExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestMathExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class MathExerciseFragment extends Fragment
        implements MathExercisePresenter.View {

    private MathExercisePresenter presenter;

    private RelativeLayout mainLayout;
    private TextView firstOperandTextView;
    private TextView operatorTextView;
    private TextView secondOperandTextView;
    private EditText resultEditText;
    private Button unblockButton;

    private int mode;

    ////

    public static MathExerciseFragment newInstance(int mode) {
        MathExerciseFragment fragment = new MathExerciseFragment();

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

        android.view.View layout = inflater.inflate(R.layout.fragment_math_exercise, container, false);
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
        presenter.requestMathExercise();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPresenter();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    ////

    @Override
    public void showMathExercise(MathExercise mathExercise) {
        firstOperandTextView.setText(String
                .format(Locale.ROOT, "%d", mathExercise.getFirstOperand()));
        operatorTextView.setText(mathExercise.getOperator());
        secondOperandTextView.setText(String
                .format(Locale.ROOT, "%d", mathExercise.getSecondOperand()));
    }

    @Override
    public void exerciseSolved() {
        ((BlockerActivity) getActivity()).solveExercise();
    }

    @Override
    public void notifyCheckResult(boolean solved) {
        showCorrectnessSnackbar(solved);
    }

    ////

    private void bindPresenter() {
        switch (mode) {
            case BlockerActivity.MODE_STANDARD:
                presenter = new MathExercisePresenter(this, UseCaseThreadPool.getInstance());
                break;
            case BlockerActivity.MODE_TEST:
                presenter = new TestMathExercisePresenter(this, UseCaseThreadPool.getInstance());
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
        mainLayout = (RelativeLayout) layout
                .findViewById(R.id.math_exercise_fragment_main_layout);
        firstOperandTextView = (TextView) layout
                .findViewById(R.id.math_exercise_fragment_first_operand_text_view);
        operatorTextView = (TextView) layout
                .findViewById(R.id.math_exercise_fragment_operator_text_view);
        secondOperandTextView = (TextView) layout
                .findViewById(R.id.math_exercise_fragment_second_operand_text_view);
        resultEditText = (EditText) layout
                .findViewById(R.id.math_exercise_fragment_result_edit_text);
        unblockButton = (Button) layout
                .findViewById(R.id.math_exercise_fragment_unblock_button);
    }

    private void bindViewListeners() {
        unblockButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                String resultString = resultEditText.getText().toString();
                if (!resultString.equals("")) presenter.checkResult(Integer.parseInt(resultString));
            }
        });
    }

    private void showCorrectnessSnackbar(boolean solved) {
        String message = solved ? "Correct!" : "Incorrect!";
        Drawable icon = solved ?
                getResources().getDrawable(R.drawable.correct_icon) :
                getResources().getDrawable(R.drawable.incorrect_icon);

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
