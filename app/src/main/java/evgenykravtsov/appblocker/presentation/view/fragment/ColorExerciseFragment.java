package evgenykravtsov.appblocker.presentation.view.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import evgenykravtsov.appblocker.presentation.presenter.ColorExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestColorExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class ColorExerciseFragment extends Fragment
        implements ColorExercisePresenter.View {

    private ColorExercisePresenter presenter;

    private LinearLayout exerciseLayout;
    private TextView colorTextView;
    private FloatingActionButton soundButton;
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
                correctColorId = R.color.colorExerciseRed;
                break;
            case Orange:
                colorTitle = "ORANGE";
                correctColorId = R.color.colorExerciseOrange;
                break;
            case Yellow:
                colorTitle = "YELLOW";
                correctColorId = R.color.colorExerciseYellow;
                break;
            case Green:
                colorTitle = "GREEN";
                correctColorId = R.color.colorExerciseGreen;
                break;
            case LightBlue:
                colorTitle = "LIGHT BLUE";
                correctColorId = R.color.colorExerciseLightBlue;
                break;
            case Blue:
                colorTitle = "BLUE";
                correctColorId = R.color.colorExerciseBlue;
                break;
            case Purple:
                colorTitle = "PURPLE";
                correctColorId = R.color.colorExercisePurple;
                break;
            case White:
                colorTitle = "WHITE";
                correctColorId = R.color.colorExerciseWhite;
                break;
            case Gray:
                colorTitle = "GRAY";
                correctColorId = R.color.colorExerciseGray;
                break;
            case Black:
                colorTitle = "BLACK";
                correctColorId = R.color.colorExerciseBlack;
                break;
            case Pink:
                colorTitle = "PINK";
                correctColorId = R.color.colorExercisePink;
                break;
            case Brown:
                colorTitle = "BROWN";
                correctColorId = R.color.colorExerciseBrown;
                break;
        }

        colorTextView.setText(colorTitle);

        List<Integer> colorIds = new ArrayList<>();
        colorIds.add(R.color.colorExerciseRed);
        colorIds.add(R.color.colorExerciseOrange);
        colorIds.add(R.color.colorExerciseYellow);
        colorIds.add(R.color.colorExerciseGreen);
        colorIds.add(R.color.colorExerciseLightBlue);
        colorIds.add(R.color.colorExerciseBlue);
        colorIds.add(R.color.colorExercisePurple);
        colorIds.add(R.color.colorExerciseWhite);
        colorIds.add(R.color.colorExerciseGray);
        colorIds.add(R.color.colorExerciseBlack);
        colorIds.add(R.color.colorExercisePink);
        colorIds.add(R.color.colorExerciseBrown);

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
        presenter.playCorrectnessSoundTip(solved);
        notifyCorrectness(solved);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void hideSoundButton() {
        soundButton.setVisibility(View.GONE);
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
        exerciseLayout = (LinearLayout) layout
                .findViewById(R.id.color_exercise_fragment_exercise_layout);
        colorTextView = (TextView) layout
                .findViewById(R.id.color_exercise_fragment_color_text_view);
        soundButton = (FloatingActionButton) layout
                .findViewById(R.id.color_exercise_fragment_sound_button);

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
            case R.color.colorExerciseRed: return ColorType.Red;
            case R.color.colorExerciseOrange: return ColorType.Orange;
            case R.color.colorExerciseYellow: return ColorType.Yellow;
            case R.color.colorExerciseGreen: return ColorType.Green;
            case R.color.colorExerciseLightBlue: return ColorType.LightBlue;
            case R.color.colorExerciseBlue: return ColorType.Blue;
            case R.color.colorExercisePurple: return ColorType.Purple;
            case R.color.colorExerciseWhite: return ColorType.White;
            case R.color.colorExerciseGray: return ColorType.Gray;
            case R.color.colorExerciseBlack: return ColorType.Black;
            case R.color.colorExercisePink: return ColorType.Pink;
            case R.color.colorExerciseBrown: return ColorType.Brown;
        }

        return null;
    }

    private void notifyCorrectness(boolean solved) {
        int colorFrom = getResources().getColor(R.color.colorPrimaryLight);

        int colorTo = solved ?
                getResources().getColor(R.color.colorRightAnswer) :
                getResources().getColor(R.color.colorWrongAnswer);

        ValueAnimator animator = ValueAnimator.ofObject(
                new ArgbEvaluator(),
                colorFrom,
                colorTo,
                colorFrom);

        animator.setDuration(BlockerActivity.CORRECTNESS_ANIMATION_DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                exerciseLayout.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });

        animator.start();
    }
}




















