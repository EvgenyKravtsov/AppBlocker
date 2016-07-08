package evgenykravtsov.appblocker.domain.usecase;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseCaseThreadPool {

    private static final int POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int TIMEOUT = 30;

    private static UseCaseThreadPool instance;

    private ThreadPoolExecutor threadPoolExecutor;

    ////

    private UseCaseThreadPool() {
        threadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
    }

    public static synchronized UseCaseThreadPool getInstance() {
        if (instance == null) {
            instance = new UseCaseThreadPool();
        }

        return instance;
    }

    ////

    public void execute(final UseCase useCase) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                useCase.execute();
            }
        });
    }
}
