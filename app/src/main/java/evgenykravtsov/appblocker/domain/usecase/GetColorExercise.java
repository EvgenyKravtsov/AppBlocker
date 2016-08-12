package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorExercise;

public class GetColorExercise implements UseCase {

    private ExerciseGenerator exerciseGenerator;
    private EventBus eventBus;

    ////

    public GetColorExercise(ExerciseGenerator exerciseGenerator, EventBus eventBus) {
        this.exerciseGenerator = exerciseGenerator;
        this.eventBus = eventBus;
    }

    ////

    @Override
    public void execute() {
        ColorExercise colorExercise = exerciseGenerator.generateColorExercise();
        if (colorExercise != null) eventBus.post(new Executed(colorExercise));
    }

    ////

    public class Executed {

        private final ColorExercise colorExercise;

        ////

        public Executed(ColorExercise colorExercise) {
            this.colorExercise = colorExercise;
        }

        ////

        public ColorExercise getColorExercise() {
            return colorExercise;
        }
    }
}
