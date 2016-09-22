package evgenykravtsov.appblocker.presentation.presenter;


import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemorySettings;

public class MemorySettingsPresenter {

    public interface View {

        void displayNumberOfPictures(int numberOfPictures);

        void displayNumberOfMistakes(int numberOfMistakes);

        void displayDisplayTime(int displayTime);
    }

    ////

    private View view;
    private MemorySettings memorySettings;

    ////

    public MemorySettingsPresenter(View view) {
        this.view = view;
        memorySettings = DependencyInjection.provideMemorySettings();
    }

    ////

    public void unbindView() {
        view = null;
    }

    public void onViewReady() {
        view.displayNumberOfPictures(memorySettings.loadNumberOfPictures());
        view.displayNumberOfMistakes(memorySettings.loadNumberOfMistakes());
        view.displayDisplayTime(memorySettings.loadDisplayTime());
    }

    public void onNumberOfPicturesChanged(int numberOfPictures) {
        if (numberOfPictures >= 2 && numberOfPictures <= 8) {
            memorySettings.saveNumberOfPictures(numberOfPictures);
            return;
        }

        int correctedNumberOfPictures = numberOfPictures;
        if (numberOfPictures < 2) correctedNumberOfPictures = 2;
        else if (numberOfPictures > 8) correctedNumberOfPictures = 8;
        memorySettings.saveNumberOfPictures(correctedNumberOfPictures);
        view.displayNumberOfPictures(correctedNumberOfPictures);
    }

    public void onNumberOfMistakesChanged(int numberOfMistakes) {
        memorySettings.saveNumberOfMistakes(numberOfMistakes);
    }

    public void onDisplaytimeChanged(int displayTime) {
        if (displayTime >= 1) {
            memorySettings.saveDisplayTime(displayTime);
            return;
        }

        if (displayTime < 1) {
            int correctedDisplayTime = 1;
            memorySettings.saveDisplayTime(correctedDisplayTime);
            view.displayDisplayTime(correctedDisplayTime);
        }
    }
}
