package evgenykravtsov.appblocker.domain.model.exercise.memory;


public interface MemorySettings {

    String KEY_NUMBER_OF_PICTURES = "key_number_of_pictures";
    int DEFAULT_NUMBER_OF_PICTURES = 4;
    String KEY_NUMBER_OF_MISTAKER = "key_number_of_mistakes";
    int DEFAULT_NUMBER_OF_MISTAKES = 1;
    String KEY_DISPLAY_TIME = "key_display_time";
    int DEFAULT_DISPLAY_TIME = 4;

    ////

    int loadNumberOfPictures();

    void saveNumberOfPictures(int numberOfPictures);

    int loadNumberOfMistakes();

    void saveNumberOfMistakes(int numberOfMistakes);

    int loadDisplayTime(); // Seconds

    void saveDisplayTime(int displayTime); // Seconds
}
