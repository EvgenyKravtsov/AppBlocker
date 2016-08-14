package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.Picture;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.presenter.MathExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.PicturesExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestMathExercisePresenter;
import evgenykravtsov.appblocker.presentation.presenter.TestPicturesExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class PictureExerciseFragment extends Fragment
        implements PicturesExercisePresenter.View {

    private PicturesExercisePresenter presenter;

    private ImageView[] imageViews;

    private int mode;

    ////

    public static PictureExerciseFragment newInstance(int mode) {
        PictureExerciseFragment fragment = new PictureExerciseFragment();

        Bundle args = new Bundle();
        args.putInt(BlockerActivity.KEY_EXERCISE_FRAGMENT_MODE, mode);
        fragment.setArguments(args);

        return fragment;
    }

    ////

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        mode = getArguments().getInt(BlockerActivity.KEY_EXERCISE_FRAGMENT_MODE);

        View layout = inflater.inflate(
                R.layout.fragment_picture_exercise, container, false
        );

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
        presenter.requestPicturesExrcise();
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
    public void showPicturesExercise(PicturesExercise picturesExercise) {
        Picture[] pictures = picturesExercise.getPictures();

        for (int i = 0; i < pictures.length; i++) {
            imageViews[i].setImageDrawable(pictures[i].getImage());
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

    ////

    private void bindViews(View layout) {
        imageViews = new ImageView[4];

        imageViews[0] = (ImageView)
                layout.findViewById(R.id.picture_exercise_fragment_picture_1);
        imageViews[1] = (ImageView)
                layout.findViewById(R.id.picture_exercise_fragment_picture_2);
        imageViews[2] = (ImageView)
                layout.findViewById(R.id.picture_exercise_fragment_picture_3);
        imageViews[3] = (ImageView)
                layout.findViewById(R.id.picture_exercise_fragment_picture_4);
    }

    private void bindViewListeners() {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView imageView = (ImageView) view;

                    int chosenPictureIndex = 1000;
                    for (int i = 0; i < imageViews.length; i++) {
                        if (imageView == imageViews[i]) chosenPictureIndex = i;
                    }

                    presenter.checkResult(chosenPictureIndex);
                }
            });
        }
    }

    public void bindPresenter() {
        switch (mode) {
            case BlockerActivity.MODE_STANDARD:
                presenter = new PicturesExercisePresenter(this);
                break;
            case BlockerActivity.MODE_TEST:
                presenter = new TestPicturesExercisePresenter(this);
                break;
        }

        EventBus.getDefault().register(presenter);
    }

    public void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }
}

































