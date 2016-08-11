package evgenykravtsov.appblocker.presentation.presenter;

import org.junit.Before;
import org.junit.Test;

import evgenykravtsov.appblocker.domain.model.exercise.math.MathExercise;
import evgenykravtsov.appblocker.domain.usecase.UseCase;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MathExercisePresenterTest {

    private MathExercisePresenter mathExercisePresenter;

    private MathExercisePresenter.View viewMock;
    private MathExercise mathExerciseMock;
    private UseCaseThreadPool useCaseThreadPoolMock;

    ////

    @Before
    public void setUp() {
        viewMock = mock(MathExercisePresenter.View.class);
        mathExerciseMock = mock(MathExercise.class);
        useCaseThreadPoolMock = mock(UseCaseThreadPool.class);
        mathExercisePresenter = new MathExercisePresenter(viewMock, useCaseThreadPoolMock);
    }

    ////

    @Test
    public void checkResult_resultIncorrect_appNotAllowed() {
        when(mathExerciseMock.getResult()).thenReturn(1);
        mathExercisePresenter.setMathExercise(mathExerciseMock);
        mathExercisePresenter.checkResult(2);
        verify(useCaseThreadPoolMock, never()).execute(any(UseCase.class));
    }

    @Test
    public void checkResult_resultIncorrect_viewNotFinished() {
        when(mathExerciseMock.getResult()).thenReturn(1);
        mathExercisePresenter.setMathExercise(mathExerciseMock);
        mathExercisePresenter.checkResult(2);
        verify(viewMock, never()).exerciseSolved();
    }
}
