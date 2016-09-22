package evgenykravtsov.appblocker.domain.usecase;


import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemoryExercise;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemorySettings;

public class GetMemoryExercise implements UseCase {

    private ExerciseGenerator exerciseGenerator;
    private EventBus eventBus;
    private MemorySettings memorySettings;

    ////

    public GetMemoryExercise(
            ExerciseGenerator exerciseGenerator,
            EventBus eventBus,
            MemorySettings memorySettings) {

        this.exerciseGenerator = exerciseGenerator;
        this.eventBus = eventBus;
        this.memorySettings = memorySettings;
    }

    ////

    @Override
    public void execute() {
        MemoryExercise memoryExercise = exerciseGenerator.generateMemoryExercise(memorySettings);
        if (memoryExercise != null) eventBus.post(new Executed(memoryExercise));
    }

    ////

    public class Executed {

        private final MemoryExercise memoryExercise;

        ////

        public Executed(MemoryExercise memoryExercise) {
            this.memoryExercise = memoryExercise;
        }

        ////

        public MemoryExercise getMemoryExercise() {
            return memoryExercise;
        }
    }
}
