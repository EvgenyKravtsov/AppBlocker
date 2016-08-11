package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;
import evgenykravtsov.appblocker.domain.model.exercise.clock.ClockExercise;

public class GetClockExercise implements UseCase {

    @Override
    public void execute() {
        ExerciseGenerator exerciseGenerator = new ExerciseGenerator();
        ClockExercise clockExercise = exerciseGenerator.generateClockExercise();
        EventBus.getDefault().post(new Executed(clockExercise));
    }

    ////

    public class Executed {

        private final ClockExercise clockExercise;

        ////

        public Executed(ClockExercise clockExercise) {
            this.clockExercise = clockExercise;
        }

        ////

        public ClockExercise getClockExercise() {
            return clockExercise;
        }
    }
}
