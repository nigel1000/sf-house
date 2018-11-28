package sf.house.mybatis.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.aop.helper.TransactionHelper;
import sf.house.bean.util.ThreadPoolUtil;

/**
 * Created by hznijianfeng on 2018/10/30.
 */

@Slf4j
public class TransactionHelperTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        TransactionHelper transactionHelper = (TransactionHelper) applicationContext.getBean("transactionHelper");
        log.info("transactionHelper in");
        ThreadPoolUtil.exec(() -> executeTransaction(transactionHelper));
        ThreadPoolUtil.exec(() -> executeTransaction(transactionHelper));
        ThreadPoolUtil.exec(() -> executeTransaction(transactionHelper));
        log.info("transactionHelper out");
        Thread.sleep(3000);
        System.exit(-1);
    }

    private static void executeTransaction(TransactionHelper transactionHelper) {
        transactionHelper.aroundBiz(() -> {
            log.info("transactionHelper start");
            transactionHelper.afterCommit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info(" afterTransactionCommitExecutor start1");
            });
            transactionHelper.afterCommit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info(" afterTransactionCommitExecutor start2");
            });
            log.info("transactionHelper end");
        });
    }
}
