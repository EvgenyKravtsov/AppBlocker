package evgenykravtsov.appblocker.presentation.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.Picture;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesExercise;
import evgenykravtsov.appblocker.presentation.presenter.PicturesExercisePresenter;
import evgenykravtsov.appblocker.presentation.view.activity.BlockerActivity;

public class PictureExerciseFragment extends Fragment
        implements PicturesExercisePresenter.View {

    private PicturesExercisePresenter presenter;

    private ImageView[] imageViews;

    ////

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

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
        presenter = new PicturesExercisePresenter(this);
        EventBus.getDefault().register(presenter);
    }

    public void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }
}

































