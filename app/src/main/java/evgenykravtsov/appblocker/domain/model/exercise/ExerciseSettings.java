package evgenykravtsov.appblocker.domain.model.exercise;

public interface ExerciseSettings {

    String KEY_MATH_EXERCISE_TYPE = "key_math_exercise_type";
    boolean DEFAULT_MATH_EXERCISE_TYPE_STATUS = true;
    String KEY_PICTURES_EXERCISE_TYPE = "key_pictures_exercise_type";
    boolean DEFAULT_PICTURES_EXERCISE_TYPE_STATUS = false;
    String KEY_CLOCK_EXERCISE_TYPE = "key_clock_exercise_type";
    boolean DEFAULT_CLOCK_EXERCISE_TYPE_STATUS = false;
    String KEY_COLOR_EXERCISE_TYPE = "key_color_exercise_type";
    boolean DEFAULT_COLOR_EXERCISE_TYPE_STATUS = false;
    String KEY_SESSION_EXERCISE_NUMBER = "key_session_exercise_number";
    int DEFAULT_SESSION_EXERCISE_NUMBER = 5;
    String KEY_SOUND_SUPPORT_STATUS = "key_sound_support_status";
    boolean DEFAULT_SOUND_SUPPORT_STATUS = true;

    ////

    boolean loadExerciseTypeStatus(ExerciseType exerciseType);

    void saveExerciseTypeStatus(ExerciseType exerciseType, boolean status);

    int loadSessionExerciseNumber();

    void saveSessionExerciseNumber(int number);

    boolean loadSoundSupportStatus();

    void saveSoundSupportStatus(boolean status);
}
