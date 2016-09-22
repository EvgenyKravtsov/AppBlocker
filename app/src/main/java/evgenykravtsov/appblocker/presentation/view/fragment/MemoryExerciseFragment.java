package evgenykravtsov.appblocker.presentation.view.fragment;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.SoundTipType;
import evgenykravtsov.appblocker.domain.model.exercise.Picture;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemoryExercise;
import evgenykravtsov.appblocker.presentation.adapter.MemoryExercisePitureListAdapter;
import evgenykravtsov.appblocker.presentation.presenter.MemoryExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestMemoryExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class MemoryExerciseFragment extends Fragment
        implements MemoryExercisePresenter.View {

    private MemoryExercisePresenter presenter;

    private LinearLayout exerciseLayoutLinearLayout;
    private CardView specifiedPictureLayoutCardView;
    private ImageView specifiedPictureImageView;
    private RecyclerView picturesListRecyclerView;

    private int mode;

    ////

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater,
                                          ViewGroup container,
                                          Bundle savedInstanceState) {

        mode = getArguments().getInt(BlockerActivity.KEY_EXERCISE_FRAGMENT_MODE);

        android.view.View layout = inflater.inflate(R.layout.fragment_memory_exercise, container, false);
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
        presenter.requestMemoryExercise();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void showMemoryExercise(MemoryExercise memoryExercise) {
        specifiedPictureLayoutCardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        specifiedPictureImageView.setImageDrawable(
                memoryExercise.getPictures()[memoryExercise.getCorrectPictureIndex()].getImage());
        specifiedPictureImageView.setVisibility(View.GONE);
        preparePicturesList(memoryExercise.getPictures());
    }

    @Override
    public void revealSpecifiedPicture() {
        specifiedPictureImageView.setVisibility(View.VISIBLE);

        int colorFrom = getResources().getColor(R.color.colorAccent);
        int colorTo = getResources().getColor(android.R.color.white);

        final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animator.setDuration(400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                specifiedPictureLayoutCardView.setCardBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        animator.start();
    }

    @Override
    public void exerciseSolved() {
        ((BlockerActivity) getActivity()).solveExercise();
    }

    @Override
    public void notifyCheckResult(boolean solved) {
        SoundTipType soundTipType = solved ? SoundTipType.RightAnswerTip : SoundTipType.WrongAnswerTip;
        presenter.playSoundTip(soundTipType);

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
                exerciseLayoutLinearLayout.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });

        animator.start();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    ////

    public static MemoryExerciseFragment newInstance(int mode) {
        MemoryExerciseFragment fragment = new MemoryExerciseFragment();

        Bundle args = new Bundle();
        args.putInt(BlockerActivity.KEY_EXERCISE_FRAGMENT_MODE, mode);
        fragment.setArguments(args);

        return fragment;
    }

    ////

    private void bindPresenter() {
        switch (mode) {
            case BlockerActivity.MODE_STANDARD:
                presenter = new MemoryExercisePresenter(this);
                break;
            case BlockerActivity.MODE_TEST:
                presenter = new TestMemoryExercisePresenter(this);
        }

        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void bindViews(View layout) {
        exerciseLayoutLinearLayout = (LinearLayout) layout.findViewById(R.id.exercise_layout);
        specifiedPictureLayoutCardView = (CardView) layout.findViewById(R.id.specified_picture_layout);
        specifiedPictureImageView = (ImageView) layout.findViewById(R.id.specified_picture);
        picturesListRecyclerView = (RecyclerView) layout.findViewById(R.id.pictures_list);
    }

    private void bindViewListeners() {}

    private void preparePicturesList(Picture[] pictures) {
        picturesListRecyclerView.setHasFixedSize(true);
        picturesListRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        MemoryExercisePitureListAdapter adapter =
                new MemoryExercisePitureListAdapter(presenter, pictures);

        picturesListRecyclerView.setAdapter(adapter);
        presenter.setAdapter(adapter);
    }
}
