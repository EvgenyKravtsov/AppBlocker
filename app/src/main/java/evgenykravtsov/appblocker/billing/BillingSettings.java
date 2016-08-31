package evgenykravtsov.appblocker.billing;

public interface BillingSettings {

    String KEY_1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn2RX5+P84nV+Zr";
    String KEY_2 = "ZYiEs84LtpJjgsRiIBf1tvhYrYyDYphYbVNbpWr6J/OTC7sUUFJO40Xcde";
    String KEY_3 = "K2dSlK0o4mB0Z4FfT0jwtYigHlE52LSx2nQB3MLPmBAHbGjebTVNpaNkOV";
    String KEY_4 = "Jz5zYnnQXiFj8gugiIfK4IKbaetCcgcBJRwznTrVUVMf1WqBbLdG8QVkCc";
    String KEY_5 = "RGx5RKpEsdvhnQmHJStYVvxk9WLFt4CTpA5rJMi1JsMJGmcEI7uKXZ/EUA";
    String KEY_6 = "ydP2KhFLPUoACYIpX5vn25l73CjQWCOw5SXPU0D0wMs5QOgB6i2z4E/stz";
    String KEY_7 = "Nnlgl0yXLWEFj+/+cWixDfrWyVF7BxPCQxFtiwIDAQAB";

    String ODD_PICTURE_EXERCISE_SKU = "odd_picture_exercise";
    String CLOCK_EXERCISE_SKU = "clock_exercise";

    int PURCHASE_REQUEST_CODE = 777;

    String KEY_APP_SHARED_STATUS = "key_app_shared_status";
    boolean DEFAULT_APP_SHARED_STATUS = false;
    String KEY_ODD_PICTURE_EXERCISE_PURCHASE_STATUS = "key_odd_picture_exercise_purchase_status";
    boolean DEFAULT_ODD_PICTURE_EXERCISE_PURCHASE_STATUS = false;
    String KEY_CLOCK_EXERCISE_PURCHASE_STATUS = "key_clock_exercise_purchase_status";
    boolean DEFAULT_CLOCK_EXERCISE_PURCHASE_STATUS = false;

    ////

    boolean loadAppSharedStatus();

    void saveAppSharedStatus(boolean status);

    boolean loadOddPicturePurchaseStatus();

    void saveOddPicturePurchaseStatus(boolean status);

    boolean loadClockExercisePurchaseStatus();

    void saveClockExercisePurchaseStatus(boolean status);
}
