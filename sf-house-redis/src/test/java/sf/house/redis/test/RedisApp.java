package sf.house.redis.test;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.bean.excps.UnifiedException;
import sf.house.bean.util.ThreadPoolUtil;
import sf.house.redis.test.demo.*;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class RedisApp {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        log.info("################### testGetSetWithExpire ###########################");
        testGetSetWithExpire(applicationContext);

        log.info("################### testDelete ###########################");
        testDelete(applicationContext);

        log.info("################### testBatchGetSet ###########################");
        testBatchGetSet(applicationContext);

        log.info("################### testBatchDelete ###########################");
        testBatchDelete(applicationContext);

        log.info("################### testDeleteTwo ###########################");
        testDeleteTwo(applicationContext);

        log.info("################### testReleaseLock ###########################");
        testReleaseLock(applicationContext);

        Thread.sleep(10000);
        System.exit(0);

    }

    private static void testDeleteTwo(ApplicationContext applicationContext) {
        BatchDelete batchDelete = (BatchDelete) applicationContext.getBean("batchDelete");
        BatchGetSet batchGetSet = (BatchGetSet) applicationContext.getBean("batchGetSet");

        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3, 4), 2);
        batchDelete.deleteTwo(Lists.newArrayList(1, 2, 3));
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3, 4), 2);
    }

    private static void testBatchDelete(ApplicationContext applicationContext) {
        BatchDelete batchDelete = (BatchDelete) applicationContext.getBean("batchDelete");
        BatchGetSet batchGetSet = (BatchGetSet) applicationContext.getBean("batchGetSet");

        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);
        batchDelete.getBatchGetSet(Lists.newArrayList(1, 2, 3));
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);

        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3, 4), 2);
        batchDelete.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3));
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3, 4), 2);
    }

    private static void testBatchGetSet(ApplicationContext applicationContext) {
        BatchGetSet batchGetSet = (BatchGetSet) applicationContext.getBean("batchGetSet");
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3), 2);
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3), 2);
        batchGetSet.getBatchGetSet(Lists.newArrayList(1, 2, 3, 4), 2);

        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3), 2);
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3), 2);
        batchGetSet.getBatchGetSetDiff(Lists.newArrayList(1, 2, 3, 4), 2);

    }

    private static void testDelete(ApplicationContext applicationContext) {
        Delete delete = (Delete) applicationContext.getBean("delete");
        GetSetWithExpire getSetWithExpire = (GetSetWithExpire) applicationContext.getBean("getSetWithExpire");

        getSetWithExpire.getSetWithExpireAuto(1, 2);
        delete.getSetWithExpireAuto(1, 2);
        getSetWithExpire.getSetWithExpireAuto(1, 2);

        getSetWithExpire.getSetWithExpireDiy(1, 2);
        delete.getSetWithExpireDiy(1, 2);
        getSetWithExpire.getSetWithExpireDiy(1, 2);

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
        for (int i = 0; i < 5; i++) {
            ThreadPoolUtil.exec(() -> {
                try {
                    releaseLock.releaseLockAuto(1, 2);
                } catch (UnifiedException ex) {
                    log.info("获取 auto 锁失败");
                }
                try {
                    releaseLock.releaseLockDiy(1, 2);
                } catch (UnifiedException ex) {
                    log.info("获取 diy 锁失败");
                }
            });
        }

    }

}
