package evgenykravtsov.appblocker.domain.model.exercise.math;

public interface MathSettings {

    String KEY_MAX_RESULT = "key_max_result";
    int DEFAULT_MAX_RESULT = 10;
    String KEY_ADDITION_STATUS = "key_addition_status";
    boolean DEFAULT_ADDITION_STATUS = true;
    String KEY_SUBSTRACTION_STATUS = "key_substraction_status";
    boolean DEFAULT_SUBSTRACTION_STATUS = false;
    String KEY_MULTIPLICATION_STATUS = "key_multiplication_status";
    boolean DEFAULT_MULTIPLICATION_STATUS = false;
    String KEY_DIVISION_STATUS = "key_division_status";
    boolean DEFAULT_DIVISION_STATUS = false;

    ////

    int loadMaxResult();

    void saveMaxResult(int maxResult);

    boolean loadAdditionStatus();

    void saveAdditionStatus(boolean enabled);

    boolean loadSubstractionStatus();

    void saveSubstractionStatus(boolean enabled);

    boolean loadMultiplicationStatus();

    void saveMultiplicationStatus(boolean enabled);

    boolean loadDivisionStatus();

    void saveDivisionStatus(boolean enabled);
}
