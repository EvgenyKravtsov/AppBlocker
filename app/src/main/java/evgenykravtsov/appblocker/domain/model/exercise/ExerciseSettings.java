package evgenykravtsov.appblocker.domain.model.exercise;

public interface ExerciseSettings {

    String KEY_EXERCISE_TYPE = "key_exercise_type";
    String MATH_EXERCISE_TYPE = "math";
    String DEFAULT_EXERCISE_TYPE = "math";

    ////

    ExerciseType loadExerciseType();
}
