package evgenykravtsov.appblocker.domain.model;

public interface AppBlockerSettings {

    String KEY_APP_BLOCKER_SERVICE_STATUS = "key_app_blocker_service_status";
    boolean DEFAULT_APP_BLOCKER_SERVICE_STATUS = false;
    String KEY_DENIED_PROCESS_NAME = "key_denied_process_name";
    String DEFAULT_DENIED_PROCESS_NAME = "";
    String KEY_ALLOWED_PROCESS_NAME = "key_allowed_process_name";
    String DEFAULT_ALLOWED_PROCESS_NAME = "";
    String KEY_PASSWORD_SET_STATUS = "key_password_key_status";
    boolean DEFAULT_PASSWORD_SET_STATUS = false;
    String KEY_PASSWORD = "key_password";
    String DEFAULT_PASSWORD = "";
    String KEY_PASSWORD_ACTIVATION_STATUS = "key_password_activation_status";
    boolean DEFAULT_PASSWORD_ACTIVATION_STATUS = false;

    ////

    boolean loadAppBlockerServiceStatus();

    void saveAppBlockerServiceStatus(boolean status);

    String loadDeniedProcessName();

    void saveDeniedProcessName(String processName);

    String loadAllowedProcessName();

    void saveAllowedProcessName(String processName);

    boolean loadPasswordSetStatus();

    void savePasswordSetStatus(boolean status);

    String loadPassword();

    void savePassword(String password);

    boolean loadPasswordActivationStatus();

    void savePasswordActivationStatus(boolean status);
}
