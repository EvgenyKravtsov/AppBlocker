package evgenykravtsov.appblocker.domain.model.exercise.memory;


import evgenykravtsov.appblocker.domain.model.exercise.Picture;

public class MemoryExercise {

    private final Picture[] pictures;
    private final int correctPictureIndex;

    ////

    public MemoryExercise(Picture[] pictures, int correctPictureIndex) {
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
