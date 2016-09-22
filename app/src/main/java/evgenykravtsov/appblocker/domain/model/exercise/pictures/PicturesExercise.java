package evgenykravtsov.appblocker.domain.model.exercise.pictures;

import evgenykravtsov.appblocker.domain.model.exercise.Picture;

public class PicturesExercise {

    private final Picture[] pictures;
    private final int correctPictureIndex;

    ////

    public PicturesExercise(Picture[] pictures, int correctPictureIndex) {
        this.pictures = pictures;
        this.correctPictureIndex = correctPictureIndex;
    }

    ////

    public Picture[] getPictures() {
        return pictures;
    }

    public int getCorrectPictureIndex() {
        return correctPictureIndex;
    }
}
