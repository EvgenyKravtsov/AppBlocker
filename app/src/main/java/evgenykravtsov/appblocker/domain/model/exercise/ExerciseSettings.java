package evgenykravtsov.appblocker.domain.model.exercise;

public interface ExerciseSettings {

    String KEY_MATH_EXERCISE_TYPE = "key_math_exercise_type";
    boolean DEFAULT_MATH_EXERCISE_TYPE_STATUS = true;

    ////

    boolean loadExerciseTypeStatus(ExerciseType exerciseType);

    void saveExerciseTypeStatus(ExerciseType exerciseType, boolean status);
}
