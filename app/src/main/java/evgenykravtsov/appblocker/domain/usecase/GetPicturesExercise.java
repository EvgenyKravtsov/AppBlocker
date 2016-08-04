package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesExercise;

public class GetPicturesExercise implements UseCase {

    @Override
    public void execute() {
        ExerciseGenerator exerciseGenerator = new ExerciseGenerator();
        PicturesExercise picturesExercise = exerciseGenerator.generatePicturesExercise();
        EventBus.getDefault().post(new Executed(picturesExercise));
    }

    ////

    public class Executed {

        private final PicturesExercise picturesExercise;

        ////

        public Executed(PicturesExercise picturesExercise) {
            this.picturesExercise = picturesExercise;
        }

        ////

        public PicturesExercise getPicturesExercise() {
            return picturesExercise;
        }
    }
}
