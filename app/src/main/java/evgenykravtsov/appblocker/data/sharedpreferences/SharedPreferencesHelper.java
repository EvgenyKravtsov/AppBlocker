package evgenykravtsov.appblocker.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseType;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathSettings;
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
    public boolean loadExerciseTypeStatus(ExerciseType exerciseType) {
        switch (exerciseType) {
            case Math:
                return loadBoolean(KEY_MATH_EXERCISE_TYPE, DEFAULT_MATH_EXERCISE_TYPE_STATUS);
            default:
                return false;
        }
    }

    @Override
    public void saveExerciseTypeStatus(ExerciseType exerciseType, boolean status) {
        switch (exerciseType) {
            case Math:
                saveBoolean(KEY_MATH_EXERCISE_TYPE, status);
        }
    }

    ////

    @Override
    public int loadMaxResult() {
        return loadInt(KEY_MAX_RESULT, DEFAULT_MAX_RESULT);
    }

    @Override
    public void saveMaxResult(int maxResult) {
        saveInt(KEY_MAX_RESULT, maxResult);
    }

    @Override
    public boolean loadAdditionStatus() {
        return loadBoolean(MathSettings.KEY_ADDITION_STATUS, MathSettings.DEFAULT_ADDITION_STATUS);
    }

    @Override
    public void saveAdditionStatus(boolean enabled) {
        saveBoolean(MathSettings.KEY_ADDITION_STATUS, enabled);
    }

    @Override
    public boolean loadSubstractionStatus() {
        return loadBoolean(
                MathSettings.KEY_SUBSTRACTION_STATUS,
                MathSettings.DEFAULT_SUBSTRACTION_STATUS
        );
    }

    @Override
    public void saveSubstractionStatus(boolean enabled) {
        saveBoolean(MathSettings.KEY_SUBSTRACTION_STATUS, enabled);
    }

    @Override
    public boolean loadMultiplicationStatus() {
        return loadBoolean(
                MathSettings.KEY_MULTIPLICATION_STATUS,
                MathSettings.DEFAULT_MULTIPLICATION_STATUS
        );
    }

    @Override
    public void saveMultiplicationStatus(boolean enabled) {
        saveBoolean(MathSettings.KEY_MULTIPLICATION_STATUS, enabled);
    }

    @Override
    public boolean loadDivisionStatus() {
        return loadBoolean(MathSettings.KEY_DIVISION_STATUS, MathSettings.DEFAULT_DIVISION_STATUS);
    }

    @Override
    public void saveDivisionStatus(boolean enabled) {
        saveBoolean(MathSettings.KEY_DIVISION_STATUS, enabled);
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
}
