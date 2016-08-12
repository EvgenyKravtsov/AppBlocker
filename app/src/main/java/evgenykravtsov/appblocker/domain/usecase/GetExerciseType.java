package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;

public class GetExerciseType implements UseCase {

    @Override
    public void execute() {
        ExerciseSettings exerciseSettings = DependencyInjection.provideExerciseSettings();

        boolean mathStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Math);
        boolean picturesStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Pictures);
        boolean clockStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Clock);
        boolean colorStatus = exerciseSettings.loadExerciseTypeStatus(ExerciseType.Color);

        List<ExerciseType> activatedExercuseTypes = new ArrayList<>();

        if (mathStatus) activatedExercuseTypes.add(ExerciseType.Math);
        if (picturesStatus) activatedExercuseTypes.add(ExerciseType.Pictures);
        if (clockStatus) activatedExercuseTypes.add(ExerciseType.Clock);
        if (colorStatus) activatedExercuseTypes.add(ExerciseType.Color);

        Random random = new Random();

        EventBus.getDefault().post(new Executed(
                activatedExercuseTypes.get(random.nextInt(activatedExercuseTypes.size()))));
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
