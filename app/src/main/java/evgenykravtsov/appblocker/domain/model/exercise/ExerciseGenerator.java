package evgenykravtsov.appblocker.domain.model.exercise;

import java.util.Random;

public class ExerciseGenerator {

    public MathExercise generateMathExercise(MathSettings mathSettings) {
        Random random = new Random();

        int min = mathSettings.loadMinResult();
        int max = mathSettings.loadMaxResult();
        String[] operators = mathSettings.loadOperatorSet().split(",");
        String operator = operators[random.nextInt(operators.length)];

        switch (operator) {
            case "+":
                int firstOperand;
                int secondOperand;
                do {
                    firstOperand = random.nextInt(max) + min;
                    secondOperand = random.nextInt(max) + min;
                } while (isOperandsAdditionOverMaxResult(firstOperand, secondOperand, max));

                int result = firstOperand + secondOperand;
                return new MathExercise(firstOperand, operator, secondOperand, result);
        }

        return null;
    }

    private boolean isOperandsAdditionOverMaxResult(int firstOperand, int secondOperand, int max) {
        return firstOperand + secondOperand > max;
    }
}
