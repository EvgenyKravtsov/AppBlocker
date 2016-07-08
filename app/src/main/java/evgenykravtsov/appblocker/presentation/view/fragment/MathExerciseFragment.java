package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.MathExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.presenter.MathExerciseViewPresenter;

public class MathExerciseFragment extends Fragment
        implements MathExerciseViewPresenter.View {

    private MathExerciseViewPresenter presenter;

    private TextView firstOperandTextView;
    private TextView operatorTextView;
    private TextView secondOperandTextView;
    private EditText resultEditText;
    private Button unblockButton;

    ////

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater,
                                          ViewGroup container,
                                          Bundle savedInstanceState) {
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
    public void finish() {
        getActivity().finish();
    }

    ////

    private void bindPresenter() {
        presenter = new MathExerciseViewPresenter(this, UseCaseThreadPool.getInstance());
        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void bindViews(android.view.View layout) {
        firstOperandTextView = (TextView)
                layout.findViewById(R.id.math_exercise_fragment_first_operand_text_view);
        operatorTextView = (TextView)
                layout.findViewById(R.id.math_exercise_fragment_operator_text_view);
        secondOperandTextView = (TextView)
                layout.findViewById(R.id.math_exercise_fragment_second_operand_text_view);
        resultEditText = (EditText)
                layout.findViewById(R.id.math_exercise_fragment_result_edit_text);
        unblockButton = (Button)
                layout.findViewById(R.id.math_exercise_fragment_unblock_button);
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
}