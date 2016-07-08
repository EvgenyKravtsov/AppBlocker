package evgenykravtsov.appblocker.domain.usecase;

import org.junit.Before;
import org.junit.Test;

import evgenykravtsov.appblocker.domain.model.AppBlocker;
import evgenykravtsov.appblocker.domain.model.AppBlockerSettings;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckForegroundAppTest {

    private CheckForegroundApp checkForegroundApp;

    private AppBlocker appBlockerMock;

    ////

    @Before
    public void setUp() {
        appBlockerMock = mock(AppBlocker.class);
        checkForegroundApp = new CheckForegroundApp(appBlockerMock);
    }

    ////

    @Test
    public void execute_allowedAppNotGoneFromForeground_setAllowedProcessNameNotCalled() {
        when(appBlockerMock.isAllowedAppGoneFromForeground()).thenReturn(false);
        checkForegroundApp.execute();
        verify(appBlockerMock, never()).setAllowedProcessName(anyString());
    }

    @Test
    public void execute_allowedAppGoneFromForeground_allowedProcessNameSetToDefault() {
        when(appBlockerMock.isAllowedAppGoneFromForeground()).thenReturn(true);
        checkForegroundApp.execute();
        verify(appBlockerMock)
                .setAllowedProcessName(AppBlockerSettings.DEFAULT_ALLOWED_PROCESS_NAME);
    }

    @Test
    public void execute_appNotBlocked_blockOperationNotCalled() {
        when(appBlockerMock.isForegroundAppBlocked()).thenReturn(false);
        checkForegroundApp.execute();
        verify(appBlockerMock, never()).executeBlockOperation();
    }

    @Test
    public void execute_appBlocked_blockOperationCalled() {
        when(appBlockerMock.isForegroundAppBlocked()).thenReturn(true);
        checkForegroundApp.execute();
        verify(appBlockerMock).executeBlockOperation();
    }

    @Test
    public void execute_deniedAppIsAllowed_blockOperationNotCalled() {
        when(appBlockerMock.isForegroundAppBlocked()).thenReturn(true);
        when(appBlockerMock.isDeniedAppAllowed()).thenReturn(true);
        checkForegroundApp.execute();
        verify(appBlockerMock, never()).executeBlockOperation();
    }

    @Test
    public void execute_deniedAppIsNotAllowed_blockOperationCalled() {
        when(appBlockerMock.isForegroundAppBlocked()).thenReturn(true);
        when(appBlockerMock.isDeniedAppAllowed()).thenReturn(false);
        checkForegroundApp.execute();
        verify(appBlockerMock).executeBlockOperation();
    }
}
