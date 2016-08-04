package evgenykravtsov.appblocker.domain.model.exercise.math;

public class MathExercise {

    private final int firstOperand;
    private final String operator;
    private final int secondOperand;
    private final int result;

    ////

    public MathExercise(int firstOperand, String operator, int secondOperand, int result) {
        this.firstOperand = firstOperand;
        this.operator = operator;
        this.secondOperand = secondOperand;
        this.result = result;
    }

    ////

    public int getFirstOperand() {
        return firstOperand;
    }

    public String getOperator() {
        return operator;
    }

    public int getSecondOperand() {
        return secondOperand;
    }

    public int getResult() {
        return result;
    }
}
