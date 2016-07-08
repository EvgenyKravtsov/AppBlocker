package evgenykravtsov.appblocker.presentation.presenter;

import org.junit.Before;
import org.junit.Test;

import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainViewPresenterTest {

    private MainViewPresenter mainViewPresenter;

    private MainViewPresenter.View viewMock;
    private AppBlockerSettings appBlockerSettingsMock;
    private UseCaseThreadPool useCaseThreadPoolMock;
    private MainViewPresenter mainViewPresenterSpy;

    ////

    @Before
    public void setUp() {
        viewMock = mock(MainViewPresenter.View.class);
        appBlockerSettingsMock = mock(AppBlockerSettings.class);
        useCaseThreadPoolMock = mock(UseCaseThreadPool.class);
        mainViewPresenter = new MainViewPresenter(
                viewMock,
                appBlockerSettingsMock,
                useCaseThreadPoolMock);

        mainViewPresenterSpy = spy(mainViewPresenter);
    }

    ////

    @Test
    public void toggleAppBlocker_appBlockerServiceOff_startAppBlockerCalled() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(false);
        mainViewPresenterSpy.toggleAppBlocker();
        verify(mainViewPresenterSpy).startAppBlocker();
    }

    @Test
    public void toggleAppBlocker_appBlockerServiceOn_stopAppBlockerCalled() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(true);
        mainViewPresenterSpy.toggleAppBlocker();
        verify(mainViewPresenterSpy).stopAppBlocker();
    }

    @Test
    public void toggleAppBlocker_appBlockerServiceOff_setBlockControlStateTrue() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(false);
        mainViewPresenterSpy.toggleAppBlocker();
        verify(viewMock).setBlockControlState(true);
    }

    @Test
    public void toggleAppBlocker_appBlockerServiceOn_setBlockControlStateFalse() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(true);
        mainViewPresenterSpy.toggleAppBlocker();
        verify(viewMock).setBlockControlState(false);
    }
}
