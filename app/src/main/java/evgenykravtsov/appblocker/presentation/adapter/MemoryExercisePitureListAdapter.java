package evgenykravtsov.appblocker.presentation.adapter;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.exercise.Picture;
import evgenykravtsov.appblocker.external.android.AppBlockerController;
import evgenykravtsov.appblocker.presentation.presenter.MemoryExercisePresenter;

public class MemoryExercisePitureListAdapter extends RecyclerView.Adapter<MemoryExercisePitureListAdapter.ViewHolder> {

    private final MemoryExercisePresenter presenter;
    private final Picture[] memoryPictures;
    private boolean hidden;

    ////

    public MemoryExercisePitureListAdapter(
            MemoryExercisePresenter presenter,
            Picture[] memoryPictures) {

        this.presenter = presenter;
        this.memoryPictures = memoryPictures;
    }


    ////

    public void hideMemoryPictures() {
        hidden = true;
        notifyDataSetChanged();
    }

    ////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemoryExercisePitureListAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_memory_pictures, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Picture picture = memoryPictures[position];
        holder.memoryPictureImageView.setBackgroundDrawable(picture.getImage());

        if (hidden) {
            hideMemoryPictureAnimation(holder.memoryPictureImageView);
            holder.memoryPictureImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    revealMemoryPictureAnimation(holder.memoryPictureImageView);
                    holder.memoryPictureImageView.setImageDrawable(picture.getImage());
                    presenter.onMemoryPictureChosen(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return memoryPictures.length;
    }

    ////

    private void hideMemoryPictureAnimation(final ImageView imageView) {
        Context context = AppBlockerController.getContext();
        int colorFrom = context.getResources().getColor(android.R.color.transparent);
        int colorTo = context.getResources().getColor(R.color.colorAccent);

        final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animator.setDuration(400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                imageView.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        animator.start();
    }

    private void revealMemoryPictureAnimation(final ImageView imageView) {
        Context context = AppBlockerController.getContext();
        int colorFrom = context.getResources().getColor(R.color.colorAccent);
        int colorTo = context.getResources().getColor(android.R.color.transparent);

        final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        animator.setDuration(400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                imageView.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        animator.start();
    }

    ////

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView memoryPictureImageView;

        ////

        public ViewHolder(View itemView) {
            super(itemView);
            memoryPictureImageView = (ImageView) itemView.findViewById(R.id.memory_picture);
        }
    }
}
