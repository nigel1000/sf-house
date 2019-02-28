package sf.house.bean.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.Throwables;
import sf.house.bean.util.trace.TraceIdUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Created by hznijianfeng on 2018/8/15.
 */

@Slf4j
public class ThreadPoolUtil {

    private ExecutorService executorService;

    private static class SingletonInstance {

        private static final ThreadPoolUtil INSTANCE = new ThreadPoolUtil();

        public static class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
            public void uncaughtException(Thread t, Throwable e) {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                log.error("线程名:{},异常信息:{}", t.getName(), stringWriter.toString());
            }
        }

        // @See Executors.DefaultThreadFactory
        public static class LogThreadFactory implements ThreadFactory {

            private static final AtomicInteger poolNumber = new AtomicInteger(1);
            private final ThreadGroup group;
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String namePrefix;

            LogThreadFactory() {
                SecurityManager s = System.getSecurityManager();
                group = (s != null) ? s.getThreadGroup() :
                        Thread.currentThread().getThreadGroup();
                namePrefix = "pool-" +
                        poolNumber.getAndIncrement() +
                        "-thread-";
            }

            public Thread newThread(Runnable r) {
                Thread t = new Thread(group, r,
                        namePrefix + threadNumber.getAndIncrement(),
                        0);
                t.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
                if (t.isDaemon())
                    t.setDaemon(false);
                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        }

        static {
            final int CORE_SIZE = Runtime.getRuntime().availableProcessors();// 线程池最少线程数
            final int MAX_SIZE = CORE_SIZE * 2;// 最大线程数
            final int KEEP_ALIVE_TIME = 60;// 最长等待时间
            final int QUEUE_SIZE = 1000;// 最大等待数
            INSTANCE.executorService = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(QUEUE_SIZE), new LogThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                INSTANCE.executorService.shutdown();
            }));
        }
    }

    private ThreadPoolUtil() {
    }

    public static void exec(Runnable command) {
        SingletonInstance.INSTANCE.executorService.execute(TraceIdUtil.wrap(command));
    }

    public static <T> Future<T> submit(Callable<T> command) {
        return SingletonInstance.INSTANCE.executorService.submit(TraceIdUtil.wrap(command));
    }

    public static <T> T get(Future<T> future, int milliSecond, Supplier<T> supplier) {
        try {
            return future.get(milliSecond, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            log.error("[ThreadPoolUtil getWithTimeout failed]", ex);
        }
        return supplier.get();

    }

    public static <T> T get(Future<T> future, Supplier<T> supplier) {
        try {
            return future.get();
        } catch (Exception ex) {
            log.error("[ThreadPoolUtil get failed]", ex);
        }
        return supplier.get();
    }

}
