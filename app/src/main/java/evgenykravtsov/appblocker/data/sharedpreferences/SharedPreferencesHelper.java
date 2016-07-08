package evgenykravtsov.appblocker.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.domain.model.exercise.MathSettings;
import evgenykravtsov.appblocker.external.android.AppBlockerController;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

public class SharedPreferencesHelper
        implements AppBlockerSettings, ExerciseSettings, MathSettings {

    private static final String SHARED_PREFERENCES =
            AppBlockerController.class.getSimpleName() + "_shared_preferences";

    private SharedPreferences sharedPreferences;

    ////

    public SharedPreferencesHelper() {
        sharedPreferences = AppBlockerController.getContext()
                .getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    ////

    @Override
    public boolean loadAppBlockerServiceStatus() {
        return loadBoolean(KEY_APP_BLOCKER_SERVICE_STATUS, DEFAULT_APP_BLOCKER_SERVICE_STATUS);
    }

    @Override
    public void saveAppBlockerServiceStatus(boolean status) {
        saveBoolean(KEY_APP_BLOCKER_SERVICE_STATUS, status);
    }

    @Override
    public String loadDeniedProcessName() {
        return loadString(KEY_DENIED_PROCESS_NAME, DEFAULT_DENIED_PROCESS_NAME);
    }

    @Override
    public void saveDeniedProcessName(String processName) {
        saveString(KEY_DENIED_PROCESS_NAME, processName);
    }

    @Override
    public String loadAllowedProcessName() {
        return loadString(KEY_ALLOWED_PROCESS_NAME, DEFAULT_ALLOWED_PROCESS_NAME);
    }

    @Override
    public void saveAllowedProcessName(String processName) {
        saveString(KEY_ALLOWED_PROCESS_NAME, processName);
    }

    ////

    @Override
    public ExerciseType loadExerciseType() {
        String type = loadString(
                ExerciseSettings.KEY_EXERCISE_TYPE,
                ExerciseSettings.DEFAULT_EXERCISE_TYPE);
        return mapStringToExerciseType(type);
    }

    ////

    @Override
    public int loadMinResult() {
        return loadInt(KEY_MIN_RESULT, DEFAULT_MIN_RESULT);
    }

    @Override
    public void saveMinResult(int minResult) {
        saveInt(KEY_MIN_RESULT, minResult);
    }

    @Override
    public int loadMaxResult() {
        return loadInt(KEY_MAX_RESULT, DEFAULT_MAX_RESULT);
    }

    @Override
    public void saveMaxResult(int maxResult) {
        saveInt(KEY_MAX_RESULT, maxResult);
    }

    @Override
    public String loadOperatorSet() {
        return loadString(KEY_OPERATOR_SET, DEFAULT_OPERATOR_SET);
    }

    @Override
    public void saveOperatorSet(String operatorSet) {
        saveString(KEY_OPERATOR_SET, operatorSet);
    }

    ////

    private void saveLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    private long loadLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    private void saveInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int loadInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean loadBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    private void saveString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String loadString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    ////

    private ExerciseType mapStringToExerciseType(String type) {
        switch (type) {
            case ExerciseSettings.MATH_EXERCISE_TYPE:
                return ExerciseType.Math;
        }

        return ExerciseType.Math;
    }
}
