package evgenykravtsov.appblocker.presentation.presenter;

import org.junit.Before;
import org.junit.Test;

import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    private MainPresenter mainPresenter;

    private MainPresenter.View viewMock;
    private AppBlockerSettings appBlockerSettingsMock;
    private UseCaseThreadPool useCaseThreadPoolMock;
    private MainPresenter mainPresenterSpy;

    ////

    @Before
    public void setUp() {
        viewMock = mock(MainPresenter.View.class);
        appBlockerSettingsMock = mock(AppBlockerSettings.class);
        useCaseThreadPoolMock = mock(UseCaseThreadPool.class);
        mainPresenter = new MainPresenter(
                viewMock,
                appBlockerSettingsMock,
                useCaseThreadPoolMock);

        mainPresenterSpy = spy(mainPresenter);
    }

    ////

    @Test
    public void toggleAppBlocker_appBlockerServiceOff_startAppBlockerCalled() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(false);
        mainPresenterSpy.toggleAppBlocker();
        verify(mainPresenterSpy).startAppBlocker();
    }

    @Test
    public void toggleAppBlocker_appBlockerServiceOn_stopAppBlockerCalled() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(true);
        mainPresenterSpy.toggleAppBlocker();
        verify(mainPresenterSpy).stopAppBlocker();
    }

    @Test
    public void toggleAppBlocker_appBlockerServiceOff_setBlockControlStateTrue() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(false);
        mainPresenterSpy.toggleAppBlocker();
        verify(viewMock).setBlockControlState(true);
    }

    @Test
    public void toggleAppBlocker_appBlockerServiceOn_setBlockControlStateFalse() {
        when(appBlockerSettingsMock.loadAppBlockerServiceStatus()).thenReturn(true);
        mainPresenterSpy.toggleAppBlocker();
        verify(viewMock).setBlockControlState(false);
    }
}
