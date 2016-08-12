package evgenykravtsov.appblocker.domain.model.exercise.color;

public class ColorExercise {

    private final ColorType colorType;

    ////

    public ColorExercise(ColorType colorType) {
        this.colorType = colorType;
    }

    ////

    public ColorType getColorType() {
        return colorType;
    }
}
