package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;

public class GetExerciseType implements UseCase {

    @Override
    public void execute() {
        // TODO Provide proper logic
        EventBus.getDefault().post(new Executed(ExerciseType.Math));
    }

    ////

    public class Executed {

        private final ExerciseType exerciseType;

        ////

        public Executed(ExerciseType exerciseType) {
            this.exerciseType = exerciseType;
        }

        ////

        public ExerciseType getExerciseType() {
            return exerciseType;
        }
    }
}
