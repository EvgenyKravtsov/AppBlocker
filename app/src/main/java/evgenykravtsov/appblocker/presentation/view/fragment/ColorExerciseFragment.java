package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorExercise;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorType;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.presenter.ClockExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.ColorExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestClockExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestColorExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class ColorExerciseFragment extends Fragment
        implements ColorExercisePresenter.View {

    private ColorExercisePresenter presenter;

    private TextView colorTextView;
    private Button soundButton;
    private Map<View, Boolean> colorViews;
    private int correctColorId;

    private int mode;

    ////

    public static ColorExerciseFragment newInstance(int mode) {
        ColorExerciseFragment fragment = new ColorExerciseFragment();

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

        android.view.View layout = inflater.inflate(R.layout.fragment_color_exercise, container, false);
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
        presenter.requestColorExercise();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void showColorExercise(ColorExercise colorExercise) {
        String colorTitle = "";
        correctColorId = 0;

        switch (colorExercise.getColorType()) {
            case Red:
                colorTitle = "RED";
                correctColorId = R.color.color_exercise_red;
                break;
            case Orange:
                colorTitle = "ORANGE";
                correctColorId = R.color.color_exercise_orange;
                break;
            case Yellow:
                colorTitle = "YELLOW";
                correctColorId = R.color.color_exercise_yellow;
                break;
            case Green:
                colorTitle = "GREEN";
                correctColorId = R.color.color_exercise_green;
                break;
            case LightBlue:
                colorTitle = "LIGHT BLUE";
                correctColorId = R.color.color_exercise_light_blue;
                break;
            case Blue:
                colorTitle = "BLUE";
                correctColorId = R.color.color_exercise_blue;
                break;
            case Purple:
                colorTitle = "PURPLE";
                correctColorId = R.color.color_exercise_purple;
                break;
            case White:
                colorTitle = "WHITE";
                correctColorId = R.color.color_exercise_white;
                break;
            case Gray:
                colorTitle = "GRAY";
                correctColorId = R.color.color_exercise_gray;
                break;
            case Black:
                colorTitle = "BLACK";
                correctColorId = R.color.color_exercise_black;
                break;
        }

        colorTextView.setText(colorTitle);

        List<Integer> colorIds = new ArrayList<>();
        colorIds.add(R.color.color_exercise_red);
        colorIds.add(R.color.color_exercise_orange);
        colorIds.add(R.color.color_exercise_yellow);
        colorIds.add(R.color.color_exercise_green);
        colorIds.add(R.color.color_exercise_light_blue);
        colorIds.add(R.color.color_exercise_blue);
        colorIds.add(R.color.color_exercise_purple);
        colorIds.add(R.color.color_exercise_white);
        colorIds.add(R.color.color_exercise_gray);
        colorIds.add(R.color.color_exercise_black);

        Random random = new Random();
        for (Map.Entry<View, Boolean> entry : colorViews.entrySet()) {
            int colorId = colorIds.remove(random.nextInt(colorIds.size()));
            entry.getKey().setBackgroundColor(getResources().getColor(colorId));

            if (colorId == correctColorId) colorViews.put(entry.getKey(), true);
        }
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

    @Override
    public void finish() {
        getActivity().finish();
    }

    ////

    private void bindPresenter() {
        switch (mode) {
            case BlockerActivity.MODE_STANDARD:
                presenter = new ColorExercisePresenter(
                    this,
                    UseCaseThreadPool.getInstance(),
                    DependencyInjection.provideSystemController());
                break;
            case BlockerActivity.MODE_TEST:
                presenter = new TestColorExercisePresenter(
                        this,
                        UseCaseThreadPool.getInstance(),
                        DependencyInjection.provideSystemController());
                break;
        }

        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void bindViews(View layout) {
        colorTextView = (TextView)
                layout.findViewById(R.id.color_exercise_fragment_color_text_view);
        soundButton = (Button)
                layout.findViewById(R.id.color_exercise_fragment_sound_button);

        colorViews = new HashMap<>();
        for (int i = 0; i < ColorType.values().length; i++) {
            String idTitle = "color_exercise_fragment_plate_" + (i + 1);
            View colorView = layout.findViewById(getResourceIdByString(idTitle));
            colorViews.put(colorView, false);
        }
    }

    private void bindViewListeners() {
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.playSoundTip();
            }
        });

        for (Map.Entry<View, Boolean> entry : colorViews.entrySet()) {
            entry.getKey().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (Map.Entry<View, Boolean> viewsEntry : colorViews.entrySet()) {
                        if (view == viewsEntry.getKey())
                            if (viewsEntry.getValue())
                                presenter.checkResult(colorIdToColorType(correctColorId));
                            else presenter.checkResult(null);
                    }
                }
            });
        }
    }

    private int getResourceIdByString(String idTitle) {
        Resources resources = getResources();
        return resources.getIdentifier(idTitle, "id", getActivity().getPackageName());
    }

    private ColorType colorIdToColorType(int colorId) {
        switch (colorId) {
            case R.color.color_exercise_red: return ColorType.Red;
            case R.color.color_exercise_orange: return ColorType.Orange;
            case R.color.color_exercise_yellow: return ColorType.Yellow;
            case R.color.color_exercise_green: return ColorType.Green;
            case R.color.color_exercise_light_blue: return ColorType.LightBlue;
            case R.color.color_exercise_blue: return ColorType.Blue;
            case R.color.color_exercise_purple: return ColorType.Purple;
            case R.color.color_exercise_white: return ColorType.White;
            case R.color.color_exercise_gray: return ColorType.Gray;
            case R.color.color_exercise_black: return ColorType.Black;
        }

        return null;
    }
}




















