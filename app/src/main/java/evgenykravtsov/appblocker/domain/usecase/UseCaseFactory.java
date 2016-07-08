package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;

public class UseCaseFactory {

    public static GetApps provideGetAppsUseCase() {
        return new GetApps();
    }

    public static BlockApp provideBlockAppUseCase(App app) {
        return new BlockApp(app);
    }

    public static UnblockApp provideUnblockAppUseCase(App app) {
        return new UnblockApp(app);
    }

    public static StartAppBlocker provideStartAppBlockerUseCase() {
        return new StartAppBlocker();
    }

    public static StopAppBlocker provideStopAppBlockerUseCase() {
        return new StopAppBlocker();
    }

    public static CheckForegroundApp provideCheckForegroundAppUseCase() {
        return new CheckForegroundApp(DependencyInjection.provideAppBlocker());
    }

    public static AllowApp provideAllowAppUseCase() {
        return new AllowApp(DependencyInjection.provideAppBlocker());
    }

    public static GetExerciseType provideGetExerciseTypeUseCase() {
        return new GetExerciseType();
    }

    public static GetMathExercise provideGetMathExerciseUseCase() {
        return new GetMathExercise(
                new ExerciseGenerator(),
                DependencyInjection.provideMathSettings(),
                EventBus.getDefault());
    }
}
