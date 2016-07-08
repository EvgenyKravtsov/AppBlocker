package evgenykravtsov.appblocker.domain.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppManagerTest {

    private AppManager appManager;

    private SystemController systemControllerMock;
    private AppRepository appRepositoryMock;

    ////

    @Before
    public void setUp() {
        systemControllerMock = mock(SystemController.class);
        appRepositoryMock = mock(AppRepository.class);
        appManager = new AppManager(systemControllerMock, appRepositoryMock);
    }



    ////

    @Test
    public void getApps_noBlockedApps_resultListContaintsNoBlockedApps() {
        when(systemControllerMock.getInstalledApps()).thenReturn(new ArrayList<App>());
        when(appRepositoryMock.getBlockedApps()).thenReturn(new ArrayList<App>());

        boolean containsBlocked = false;
        for (App app : appManager.getApps()) {
            if (app.isBlocked()) containsBlocked = true;
        }

        assertFalse(containsBlocked);
    }

    @Test
    public void getApps_someBlockedApps_sameCountOfBlockedAppsInReturnedList() {
        when(systemControllerMock.getInstalledApps()).thenReturn(generateTestAppList());
        when(appRepositoryMock.getBlockedApps()).thenReturn(generateTestAppList());

        int blockedAppsCount = appRepositoryMock.getBlockedApps().size();
        int blockedAppsCountInReturnedList = 0;
        for (App app : appManager.getApps()) {
            if (app.isBlocked()) blockedAppsCountInReturnedList++;
        }

        assertEquals(blockedAppsCount, blockedAppsCountInReturnedList);
    }

    ////

    private List<App> generateTestAppList() {
        List<App> testApps = new ArrayList<>();
        testApps.add(new App("one", "one"));
        testApps.add(new App("two", "two"));
        return testApps;
    }
}
