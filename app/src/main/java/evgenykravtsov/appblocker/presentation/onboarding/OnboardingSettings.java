package evgenykravtsov.appblocker.presentation.onboarding;

public interface OnboardingSettings {

    String KEY_SET_PARENT_PASSWORD_RECOMMENDATION_STATUS = "key_set_parent_password_recommendation_status";
    boolean DEFAULT_SET_PARENT_PASSWORD_RECOMMENDATION_STATUS = false;
    String KEY_EXERCISE_SETTINGS_TIP_STATUS = "key_exercise_settings_tip_status";
    boolean DEFAULT_EXERCISE_SETTINGS_TIP_STATUS = false;
    String KEY_NUMBER_OF_APP_LAUNCHES = "key_number_of_app_launches";
    int DEFAULT_NUMBER_OF_APP_LAUNCHES = 0;
    int NUMBER_OF_APP_LAUNCHES_FOR_USER_FEEDBACK = 15;

    ////

    boolean loadSetParentPasswordRecommendationStatus();

    void saveSetParentPasswordRecommendationStatus(boolean status);

    boolean loadExerciseSettingsTipStatus();

    void saveExerciseSettingsTipStatus(boolean status);

    int loadNumberOfAppLaunches();

    void saveNumberOfAppLaunches(int number);
}
