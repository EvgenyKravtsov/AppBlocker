package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;
import evgenykravtsov.appblocker.domain.model.exercise.MathExercise;
import evgenykravtsov.appblocker.domain.model.exercise.MathSettings;

public class GetMathExercise implements UseCase {

    private ExerciseGenerator exerciseGenerator;
    private MathSettings mathSettings;
    private EventBus eventBus;

    ////

    public GetMathExercise(ExerciseGenerator exerciseGenerator,
                           MathSettings mathSettings,
                           EventBus eventBus) {
        this.exerciseGenerator = exerciseGenerator;
        this.mathSettings = mathSettings;
        this.eventBus = eventBus;
    }

    ////

    @Override
    public void execute() {
        MathExercise mathExercise = exerciseGenerator.generateMathExercise(mathSettings);
        if (mathExercise != null) eventBus.post(new Executed(mathExercise));
    }

    public class Executed {

        private final MathExercise mathExercise;

        ////

        public Executed(MathExercise mathExercise) {
            this.mathExercise = mathExercise;
        }

        ////

        public MathExercise getMathExercise() {
            return mathExercise;
        }
    }
}
