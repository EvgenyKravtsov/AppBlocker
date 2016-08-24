package evgenykravtsov.appblocker.external.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;

public class AppBlockerService extends Service {

    private static final String TAG = AppBlockerService.class.getSimpleName();

    private boolean serviceStatus;

    ////

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceStatus = true;
        new Thread(new AppBlockerThread()).start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceStatus = false;
    }

    ////

    public class AppBlockerThread implements Runnable {

        @Override
        public void run() {
            while (serviceStatus) {
                try {
                    UseCaseFactory.provideCheckForegroundAppUseCase().execute();
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
