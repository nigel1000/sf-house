package sf.house.redis.test;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.aop.util.SplitUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.redis.test.demo.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class RedisApp {

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        log.info("################### testGetSetWithExpire ###########################");
        testGetSetWithExpire(applicationContext);

        log.info("################### testDelete ###########################");
        testDelete(applicationContext);

        log.info("################### testBatchGetSet ###########################");
        testBatchGetSet(applicationContext);

        log.info("################### testBatchDelete ###########################");
        testBatchDelete(applicationContext);

        log.info("################### testReleaseLock ###########################");
        testReleaseLock(applicationContext);

        executor.shutdown();
    }

    private static void testBatchDelete(ApplicationContext applicationContext) {
        BatchDelete batchDelete = (BatchDelete) applicationContext.getBean("batchDelete");
        BatchGetSet batchGetSet = (BatchGetSet) applicationContext.getBean("batchGetSet");

        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);
        batchDelete.batchDelete(Lists.newArrayList(1, 2, 3));
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);
    }

    private static void testBatchGetSet(ApplicationContext applicationContext) {
        BatchGetSet batchGetSet = (BatchGetSet) applicationContext.getBean("batchGetSet");
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3), 2);
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3), 2);
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);

        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(11, 21, 31), 2);
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(11, 21, 31), 2);
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(11, 21, 31, 41), 2);

    }

    private static void testDelete(ApplicationContext applicationContext) {
        Delete delete = (Delete) applicationContext.getBean("delete");
        GetSetWithExpire getSetWithExpire = (GetSetWithExpire) applicationContext.getBean("getSetWithExpire");

        getSetWithExpire.getSetWithExpireAuto(1, 2);
        getSetWithExpire.getSetWithExpireAuto(1, 2);
        delete.delete(1, 2);
        getSetWithExpire.getSetWithExpireAuto(1, 2);
        getSetWithExpire.getSetWithExpireAuto(1, 2);

    }

    private static void testGetSetWithExpire(ApplicationContext applicationContext) {
        GetSetWithExpire getSetWithExpire = (GetSetWithExpire) applicationContext.getBean("getSetWithExpire");

        getSetWithExpire.getSetWithExpireAuto(1, 2);
        getSetWithExpire.getSetWithExpireAuto(1, 2);

        getSetWithExpire.getSetWithExpireDiy(1, 2);
        getSetWithExpire.getSetWithExpireDiy(1, 2);

    }

    private static void testReleaseLock(ApplicationContext applicationContext) {

        ReleaseLock releaseLock = (ReleaseLock) applicationContext.getBean("releaseLock");
        SplitUtil.Execute<Integer> autoExecute = (t) -> {
            try {
                releaseLock.releaseLockAuto(1, 2);
            } catch (UnifiedException ex) {
                log.info("获取 auto 锁失败");
            }
        };
        run(autoExecute);
        run(autoExecute);
        run(autoExecute);

        SplitUtil.Execute<Integer> diyExecute = (t) -> {
            try {
                releaseLock.releaseLockDiy(1, 2);
            } catch (UnifiedException ex) {
                log.info("获取 diy 锁失败");
            }
        };
        run(diyExecute);
        run(diyExecute);
        run(diyExecute);
        run(diyExecute);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void run(SplitUtil.Execute<Integer> execute) {
        executor.submit(() -> {
            SplitUtil.splitExecute(Lists.newArrayList(1), 1, execute);
        });
    }
}
