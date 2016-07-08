package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;

public class GetExerciseType implements UseCase {

    @Override
    public void execute() {
        ExerciseSettings exerciseSettings = DependencyInjection.provideExerciseSettings();
        EventBus.getDefault().post(new Executed(exerciseSettings.loadExerciseType()));
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
