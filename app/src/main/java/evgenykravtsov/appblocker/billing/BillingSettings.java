package evgenykravtsov.appblocker.billing;

public interface BillingSettings {

    String KEY_APP_SHARED_STATUS = "key_app_shared_status";
    boolean DEFAULT_APP_SHARED_STATUS = false;

    ////

    boolean loadAppSharedStatus();

    void saveAppSharedStatus(boolean status);
}
