package evgenykravtsov.appblocker.domain.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppBlockerTest {

    private AppBlocker appBlocker;

    private SystemController systemControllerMock;
    private AppBlockerSettings appBlockerSettingsMock;
    private AppManager appManagerMock;
    private AppBlocker.Operation operationMock;
    private AppBlocker appBlockerSpy;

    ////

    @Before
    public void setUp() {
        systemControllerMock = mock(SystemController.class);
        appBlockerSettingsMock = mock(AppBlockerSettings.class);
        appManagerMock = mock(AppManager.class);
        operationMock = mock(AppBlocker.Operation.class);
        appBlocker = new AppBlocker(
                systemControllerMock,
                appBlockerSettingsMock,
                appManagerMock,
                operationMock);
        appBlockerSpy = spy(appBlocker);
    }

    ////

    @Test
    public void isForegroundAppBlocked_appNotBlocked_setDeniedProcessNameNotCalled() {
        when(appManagerMock.getBlockedApps()).thenReturn(new ArrayList<App>());
        when(systemControllerMock.getForegroundApps()).thenReturn(new ArrayList<App>());
        appBlockerSpy.isForegroundAppBlocked();
        verify(appBlockerSpy, never()).setDeniedProcessName(anyString());
    }

    @Test
    public void isForegroundAppBlocked_appBlocked_deniedProcessNameSet() {
        when(appManagerMock.getBlockedApps()).thenReturn(generateTestAppList());
        when(appManagerMock.getForegroundApps()).thenReturn(generateTestAppList());
        appBlockerSpy.isForegroundAppBlocked();
        verify(appBlockerSpy).setDeniedProcessName(anyString());
    }

    @Test
    public void isForegroundAppBlocked_appNotBlocked_returnFalse() {
        when(appManagerMock.getBlockedApps()).thenReturn(new ArrayList<App>());
        when(systemControllerMock.getForegroundApps()).thenReturn(new ArrayList<App>());
        assertFalse(appBlocker.isForegroundAppBlocked());
    }

    @Test
    public void isForegroundAppBlocked_appBlocked_returnTrue() {
        when(appManagerMock.getBlockedApps()).thenReturn(generateTestAppList());
        when(appManagerMock.getForegroundApps()).thenReturn(generateTestAppList());
        assertTrue(appBlocker.isForegroundAppBlocked());
    }

    @Test
    public void isAllowedAppGoneFromForeground_appNotGone_returnFalse() {
        when(appManagerMock.getForegroundApps()).thenReturn(generateTestAppList());
        when(appBlockerSpy.getAllowedProcessName()).thenReturn("one");
        assertFalse(appBlockerSpy.isAllowedAppGoneFromForeground());
    }

    @Test
    public void isAllowedAppGoneFromForeground_appGone_returnTrue() {
        when(appManagerMock.getForegroundApps()).thenReturn(generateTestAppList());
        when(appBlockerSpy.getAllowedProcessName()).thenReturn("");
        assertTrue(appBlockerSpy.isAllowedAppGoneFromForeground());
    }

    @Test
    public void isDeniedAppAllowed_notAllowed_returnFalse() {
        when(appBlockerSpy.getDeniedProcessName()).thenReturn("one");
        when(appBlockerSpy.getAllowedProcessName()).thenReturn("");
        assertFalse(appBlockerSpy.isDeniedAppAllowed());
    }

    @Test
    public void isDeniedAppAllowed_allowed_returnTrue() {
        when(appBlockerSpy.getDeniedProcessName()).thenReturn("one");
        when(appBlockerSpy.getAllowedProcessName()).thenReturn("one");
        assertTrue(appBlockerSpy.isDeniedAppAllowed());
    }

    ////

    private List<App> generateTestAppList() {
        List<App> testApps = new ArrayList<>();
        testApps.add(new App("one", "one"));
        testApps.add(new App("two", "two"));
        return testApps;
    }
}
