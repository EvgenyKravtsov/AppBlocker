package evgenykravtsov.appblocker.domain.model;

public interface AppBlockerSettings {

    String KEY_APP_BLOCKER_SERVICE_STATUS = "key_app_blocker_service_status";
    boolean DEFAULT_APP_BLOCKER_SERVICE_STATUS = false;
    String KEY_DENIED_PROCESS_NAME = "key_denied_process_name";
    String DEFAULT_DENIED_PROCESS_NAME = "";
    String KEY_ALLOWED_PROCESS_NAME = "key_allowed_process_name";
    String DEFAULT_ALLOWED_PROCESS_NAME = "";

    ////

    boolean loadAppBlockerServiceStatus();

    void saveAppBlockerServiceStatus(boolean status);

    String loadDeniedProcessName();

    void saveDeniedProcessName(String processName);

    String loadAllowedProcessName();

    void saveAllowedProcessName(String processName);
}
