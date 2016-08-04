package evgenykravtsov.appblocker.domain.model.exercise.pictures;

public enum PictureCategory {

    Transport, Tools, Clothes, Vegetables;

    public static PictureCategory getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
