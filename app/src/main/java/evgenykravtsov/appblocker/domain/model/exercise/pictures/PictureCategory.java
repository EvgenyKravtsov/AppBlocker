package evgenykravtsov.appblocker.domain.model.exercise.pictures;

public enum PictureCategory {

    Cats, Cars, Dogs, Flowers;

    public static PictureCategory getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
