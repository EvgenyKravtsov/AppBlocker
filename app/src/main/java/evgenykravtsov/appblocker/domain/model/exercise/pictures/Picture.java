package evgenykravtsov.appblocker.domain.model.exercise.pictures;

import android.graphics.drawable.Drawable;

public class Picture {

    private final Drawable image;

    ////

    public Picture(Drawable image) {
        this.image = image;
    }

    ////

    public Drawable getImage() {
        return image;
    }
}
