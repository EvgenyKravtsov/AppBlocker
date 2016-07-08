package evgenykravtsov.appblocker.domain.model.exercise;

public interface MathSettings {

    String KEY_MIN_RESULT = "key_min_result";
    int DEFAULT_MIN_RESULT = 1;
    String KEY_MAX_RESULT = "key_max_result";
    int DEFAULT_MAX_RESULT = 10;
    String KEY_OPERATOR_SET = "key_operator_set";
    String DEFAULT_OPERATOR_SET = "+";

    ////

    int loadMinResult();

    void saveMinResult(int minResult);

    int loadMaxResult();

    void saveMaxResult(int maxResult);

    String loadOperatorSet();

    void saveOperatorSet(String operatorSet);
}
