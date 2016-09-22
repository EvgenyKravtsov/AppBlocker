package evgenykravtsov.appblocker.presentation.view.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Locale;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.presentation.presenter.MemorySettingsPresenter;

public class MemorySettingsFragment extends Fragment
        implements MemorySettingsPresenter.View {

    private MemorySettingsPresenter presenter;

    private EditText numberOfPicturesEditText;
    private EditText numberOfMistakesEditText;
    private EditText displayTimeEditText;

    ////

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_memory_settings, container, false);
        bindViews(layout);
        bindViewListeners();
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindPresenter();
        presenter.onViewReady();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void displayNumberOfPictures(int numberOfPictures) {
        numberOfPicturesEditText.setText(String.format(Locale.ROOT, "%d", numberOfPictures));
    }

    @Override
    public void displayNumberOfMistakes(int numberOfMistakes) {
        numberOfMistakesEditText.setText(String.format(Locale.ROOT, "%d", numberOfMistakes));
    }

    @Override
    public void displayDisplayTime(int displayTime) {
        displayTimeEditText.setText(String.format(Locale.ROOT, "%d", displayTime));
    }

    ////

    private void bindViews(View layout) {
        numberOfPicturesEditText = (EditText) layout
                .findViewById(R.id.memory_settings_fragment_number_of_pictures_edit_text);
        numberOfMistakesEditText = (EditText) layout
                .findViewById(R.id.memory_settings_fragment_number_of_mistakes_edit_text);
        displayTimeEditText = (EditText) layout
                .findViewById(R.id.memory_settings_fragment_display_time_edit_text);
    }

    private void bindViewListeners() {
        numberOfPicturesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(""))
                    presenter.onNumberOfPicturesChanged(Integer.parseInt(editable.toString()));
            }
        });
        numberOfMistakesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(""))
                    presenter.onNumberOfMistakesChanged(Integer.parseInt(editable.toString()));
            }
        });
        displayTimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(""))
                    presenter.onDisplaytimeChanged(Integer.parseInt(editable.toString()));
            }
        });
    }

    private void bindPresenter() {
        presenter = new MemorySettingsPresenter(this);
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }
}

































