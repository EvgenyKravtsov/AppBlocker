package evgenykravtsov.appblocker;

import evgenykravtsov.appblocker.data.sharedpreferences.SharedPreferencesHelper;
import evgenykravtsov.appblocker.data.sqlite.SqliteHelper;
import evgenykravtsov.appblocker.domain.model.AppBlocker;
import evgenykravtsov.appblocker.domain.model.AppManager;
import evgenykravtsov.appblocker.domain.model.AppRepository;
import evgenykravtsov.appblocker.domain.model.SystemController;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseSettings;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathSettings;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesRepository;
import evgenykravtsov.appblocker.external.android.AppBlockerController;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;
import evgenykravtsov.appblocker.external.android.InternalPicturesRepository;
import evgenykravtsov.appblocker.external.android.SystemControllerAndroid;

public class DependencyInjection {

    public static AppRepository provideAppRepository() {
        return SqliteHelper.getInstance(AppBlockerController.getContext());
    }

    public static SystemController provideSystemController() {
        return new SystemControllerAndroid();
    }

    public static AppBlockerSettings provideAppBlockerSettings() {
        return new SharedPreferencesHelper();
    }

    public static AppManager provideAppManager() {
        return new AppManager(provideSystemController(),
                provideAppRepository());
    }

    public static AppBlocker provideAppBlocker() {
        return new AppBlocker(
                provideSystemController(),
                provideAppBlockerSettings(),
                provideAppManager(),
                new SystemControllerAndroid());
    }

    public static ExerciseSettings provideExerciseSettings() {
        return new SharedPreferencesHelper();
    }

    public static MathSettings provideMathSettings() {
        return new SharedPreferencesHelper();
    }

    public static PicturesRepository providePicturesRepository() {
        return new InternalPicturesRepository(AppBlockerController.getContext());
    }
}
