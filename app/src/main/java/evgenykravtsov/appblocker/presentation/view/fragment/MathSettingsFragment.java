package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.presentation.presenter.MathSettingsPresenter;

public class MathSettingsFragment extends Fragment
        implements MathSettingsPresenter.View {

    private MathSettingsPresenter presenter;

    private EditText maxResultEditText;
    private CheckBox additionCheckBox;
    private CheckBox substractionCheckBox;
    private CheckBox multiplicationCheckBox;
    private CheckBox divisionCheckBox;

    private int operationsActivatedCount;

    ////

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        bindPresenter();

        View layout = inflater.inflate(R.layout.fragment_math_settings, container, false);
        bindViews(layout);
        establishInitialViewsState();
        bindViewListeners();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.setMaxValue(Integer.parseInt(maxResultEditText.getText().toString()));
        unbindPresenter();
    }

    ////

    private void bindViews(View layout) {
        maxResultEditText = (EditText)
                layout.findViewById(R.id.math_settings_fragment_max_result_edit_text);
        additionCheckBox = (CheckBox)
                layout.findViewById(R.id.math_settings_fragment_addition_check_box);
        substractionCheckBox = (CheckBox)
                layout.findViewById(R.id.math_settings_fragment_substraction_check_box);
        multiplicationCheckBox = (CheckBox)
                layout.findViewById(R.id.math_settings_fragment_multiplication_check_box);
        divisionCheckBox = (CheckBox)
                layout.findViewById(R.id.math_settings_fragment_division_check_box);

    }

    private void bindViewListeners() {
        additionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (operationsActivatedCount == 1) {
                        // TODO Notify user
                        additionCheckBox.setChecked(true);
                        return;
                    }

                    operationsActivatedCount--;
                    presenter.setAddition(false);
                } else {
                    operationsActivatedCount++;
                    presenter.setAddition(true);
                }
            }
        });

        substractionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (operationsActivatedCount == 1) {
                        // TODO Notify user
                        substractionCheckBox.setChecked(true);
                        return;
                    }

                    operationsActivatedCount--;
                    presenter.setSubstraction(false);
                } else {
                    operationsActivatedCount++;
                    presenter.setSubstraction(true);
                }
            }
        });

        multiplicationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (operationsActivatedCount == 1) {
                        // TODO Notify user
                        multiplicationCheckBox.setChecked(true);
                        return;
                    }

                    operationsActivatedCount--;
                    presenter.setMultiplication(false);
                } else {
                    operationsActivatedCount++;
                    presenter.setMultiplication(true);
                }
            }
        });

        divisionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    if (operationsActivatedCount == 1) {
                        // TODO Notify user
                        divisionCheckBox.setChecked(true);
                        return;
                    }

                    operationsActivatedCount--;
                    presenter.setDivision(false);
                } else {
                    operationsActivatedCount++;
                    presenter.setDivision(true);
                }
            }
        });
    }

    private void bindPresenter() {
        presenter = new MathSettingsPresenter(this);
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }

    private void establishInitialViewsState() {
        maxResultEditText.setText(
                String.format(Locale.ROOT, "%d", presenter.getMaxResult()));

        boolean additionEnabled = presenter.getAddition();
        if (additionEnabled) operationsActivatedCount++;
        additionCheckBox.setChecked(additionEnabled);

        boolean substractionEnabled = presenter.getSubstraction();
        if (substractionEnabled) operationsActivatedCount++;
        substractionCheckBox.setChecked(substractionEnabled);

        boolean multiplicationEnabled = presenter.getMultiplication();
        if (multiplicationEnabled) operationsActivatedCount++;
        multiplicationCheckBox.setChecked(multiplicationEnabled);

        boolean divisionEnabled = presenter.getDivision();
        if (divisionEnabled) operationsActivatedCount++;
        divisionCheckBox.setChecked(divisionEnabled);
    }
}
