package evgenykravtsov.appblocker.domain.usecase;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;

import evgenykravtsov.appblocker.domain.model.exercise.ExerciseGenerator;
import evgenykravtsov.appblocker.domain.model.exercise.MathExercise;
import evgenykravtsov.appblocker.domain.model.exercise.MathSettings;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetMathExerciseTest {

    private GetMathExercise getMathExercise;

    private ExerciseGenerator exerciseGeneratorMock;
    private MathSettings mathSettingsMock;
    private EventBus eventBusMock;

    ////

    @Before
    public void setUp() {
        exerciseGeneratorMock = mock(ExerciseGenerator.class);
        mathSettingsMock = mock(MathSettings.class);
        eventBusMock = mock(EventBus.class);
        getMathExercise = new GetMathExercise(
                exerciseGeneratorMock,
                mathSettingsMock,
                eventBusMock);
    }

    ////

    @Test
    public void execute_exerciseGeneratorReturnsNull_eventNotFired() {
        when(exerciseGeneratorMock.generateMathExercise(mathSettingsMock)).thenReturn(null);
        getMathExercise.execute();
        verify(eventBusMock, never()).post(null);
    }

    @Test
    public void execute_exerciseGeneratorReturnsValid_eventFired() {
        when(exerciseGeneratorMock.generateMathExercise(mathSettingsMock)).thenReturn(mock(MathExercise.class));
        getMathExercise.execute();
        verify(eventBusMock).post(any());
    }
}
