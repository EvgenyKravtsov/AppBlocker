package evgenykravtsov.appblocker.domain.model.exercise.clock;

public class ClockExercise {

    private final int hours;
    private final int minutes;

    ////

    public ClockExercise(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    ////

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }
}
