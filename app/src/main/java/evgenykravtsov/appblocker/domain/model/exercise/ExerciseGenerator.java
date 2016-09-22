package evgenykravtsov.appblocker.domain.model.exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.domain.model.exercise.clock.ClockExercise;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorExercise;
import evgenykravtsov.appblocker.domain.model.exercise.color.ColorType;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathExercise;
import evgenykravtsov.appblocker.domain.model.exercise.math.MathSettings;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemoryExercise;
import evgenykravtsov.appblocker.domain.model.exercise.memory.MemorySettings;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PictureCategory;
import evgenykravtsov.appblocker.domain.model.exercise.pictures.PicturesExercise;

public class ExerciseGenerator {

    private Random random;
    private PicturesRepository picturesRepository;

    ////

    public ExerciseGenerator() {
        random = new Random();
        picturesRepository = DependencyInjection.providePicturesRepository();
    }

    ////

    public MathExercise generateMathExercise(MathSettings mathSettings) {
        int min = 1;
        int max = mathSettings.loadMaxResult();

        List<String> operators = new ArrayList<>();
        if (mathSettings.loadAdditionStatus()) operators.add("+");
        if (mathSettings.loadSubstractionStatus()) operators.add("-");
        if (mathSettings.loadMultiplicationStatus()) operators.add("*");
        if (mathSettings.loadDivisionStatus()) operators.add("/");

        String operator = operators.get(random.nextInt(operators.size()));

        int firstOperand;
        int secondOperand;
        int result;

        switch (operator) {
            case "+":

                do {
                    firstOperand = random.nextInt(max) + min;
                    secondOperand = random.nextInt(max) + min;
                } while (isOperandsAdditionOverMaxResult(firstOperand, secondOperand, max));

                result = firstOperand + secondOperand;
                return new MathExercise(firstOperand, operator, secondOperand, result);
            case "-":
                do {
                    firstOperand = random.nextInt(max) + min;
                    secondOperand = random.nextInt(max) + min;
                } while (isOperandsSubstracionBelowMinResult(firstOperand, secondOperand, min));

                result = firstOperand - secondOperand;
                return new MathExercise(firstOperand, operator, secondOperand, result);
            case "*":
                do {
                    firstOperand = random.nextInt(max) + min;
                    secondOperand = random.nextInt(max) + min;
                } while (isOperandsMultiplicationOverMaxResult(firstOperand, secondOperand, max));

                result = firstOperand * secondOperand;
                return new MathExercise(firstOperand, operator, secondOperand, result);
            case "/":
                do {
                    firstOperand = random.nextInt(max) + min;
                    secondOperand = random.nextInt(max) + min;
                } while (isOperandsDivisionBelowMinResult(firstOperand, secondOperand, min) ||
                    isOperandsDivisonIsNotDigit(firstOperand, secondOperand));

                result = firstOperand / secondOperand;
                return new MathExercise(firstOperand, operator, secondOperand, result);
        }

        return null;
    }

    public PicturesExercise generatePicturesExercise() {
        Map<PictureCategory, List<Picture>> categorizedPictures =
                picturesRepository.getCategorizedPictures();

        PictureCategory correctCategory = PictureCategory.getRandom();

        PictureCategory wrongCategory;
        do wrongCategory = PictureCategory.getRandom();
        while (correctCategory == wrongCategory);

        List<Picture> correctCategoryPictures = categorizedPictures.get(correctCategory);
        List<Picture> wrongCategoryPictures = categorizedPictures.get(wrongCategory);

        Picture correctPicture = correctCategoryPictures
                .get(random.nextInt(correctCategoryPictures.size()));
        Picture wrongPicture1 = wrongCategoryPictures
                .remove(random.nextInt(wrongCategoryPictures.size()));
        Picture wrongPicture2 = wrongCategoryPictures
                .remove(random.nextInt(wrongCategoryPictures.size()));
        Picture wrongPicture3 = wrongCategoryPictures
                .remove(random.nextInt(wrongCategoryPictures.size()));

        int correctPictureIndex = random.nextInt(4);
        Picture[] pictures = new Picture[4];

        pictures[correctPictureIndex] = correctPicture;

        for (int i = 0; i < 4; i++) {
            if (pictures[i] == null) {
                pictures[i] = wrongPicture1;
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (pictures[i] == null) {
                pictures[i] = wrongPicture2;
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (pictures[i] == null) {
                pictures[i] = wrongPicture3;
                break;
            }
        }

        return new PicturesExercise(pictures, correctPictureIndex);
    }

    public ClockExercise generateClockExercise() {
        int hours = random.nextInt(12) + 1;
        int minutes = random.nextInt(12);

        return new ClockExercise(hours, minutes * 5);
    }

    public ColorExercise generateColorExercise() {
        ColorType answerColor = ColorType.values()[random.nextInt(ColorType.values().length)];
        return new ColorExercise(answerColor);
    }

    public MemoryExercise generateMemoryExercise(MemorySettings memorySettings) {
        List<Picture> pictures = picturesRepository
                .getMemoryPictures(memorySettings.loadNumberOfPictures());
        Collections.shuffle(pictures);

        int correctPictureIndex = random.nextInt(pictures.size());
        return new MemoryExercise(pictures.toArray(new Picture[pictures.size()]), correctPictureIndex);
    }

    ////

    private boolean isOperandsAdditionOverMaxResult(int firstOperand, int secondOperand, int max) {
        return firstOperand + secondOperand > max;
    }

    private boolean isOperandsSubstracionBelowMinResult(int firstOperand, int secondOperand, int min) {
        return firstOperand - secondOperand < min;
    }

    private boolean isOperandsMultiplicationOverMaxResult(
            int firstOperand,
            int secondOperand,
            int max) {

        return firstOperand * secondOperand > max;
    }

    private boolean isOperandsDivisonIsNotDigit(int firstOperand, int secondsOperand) {
        return firstOperand % secondsOperand != 0;
    }

    private boolean isOperandsDivisionBelowMinResult(int firstOperand, int secondOperand, int min) {
        return firstOperand / secondOperand < min;
    }
}
