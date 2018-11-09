package sf.house.mybatis.test;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.aop.helper.TransactionHelper;
import sf.house.bean.beans.page.PageParam;
import sf.house.bean.excps.UnifiedException;
import sf.house.mybatis.dao.page.PageHelper;
import sf.house.mybatis.test.dao.TestDao;
import sf.house.mybatis.test.dao.TestMapper;
import sf.house.mybatis.test.domain.Test;
import sf.house.mybatis.test.facade.TestFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nijianfeng on 2018/8/16.
 */

@Slf4j
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        try {
            TransactionHelper transactionHelper = (TransactionHelper) applicationContext.getBean("transactionHelper");
            transactionHelper.aroundBiz(() -> {
                // 从Spring 容器中获取内容
                TestMapper testMapper = (TestMapper) applicationContext.getBean("testMapper");

                Long id1 = testCreate(testMapper);
                testCreates(testMapper);
                testUpdate(testMapper, id1);
                testDelete(testMapper, id1);
                testDeletes(testMapper, Arrays.asList(testCreate(testMapper), testCreate(testMapper)));
                testList(testMapper, new Test());
                testPaging(testMapper, new Test());

                TestDao testDao = (TestDao) applicationContext.getBean("testDao");
                Long id2 = testCreate(testDao);
                testCreates(testDao);
                testUpdate(testDao, id2);
                testDelete(testDao, id2);
                testDeletes(testDao, Arrays.asList(testCreate(testDao), testCreate(testDao)));
                testList(testDao, new Test());
                testPaging(testDao, new Test());

                throw UnifiedException.gen("测试", "就是为了回滚");
            });
        } catch (UnifiedException ex) {
            log.info(Throwables.getStackTraceAsString(ex));
        }

        TestFacade testFacade = (TestFacade) applicationContext.getBean("testFacade");
        log.info("{}", testFacade.rollback$catchExcp());
    }

    private static void testPaging(TestMapper testMapper, Test test) {
        log.info("testMapper--------------testPaging------------------");
        log.info("{}", PageHelper.paging(testMapper, test, PageParam.valueOfByPageNo(1, 4)));
    }

    private static void testList(TestMapper testMapper, Test test) {
        log.info("testMapper--------------testList&count------------------");
        log.info("{}", testMapper.list(test));
        log.info("{}", testMapper.count(test));
    }

    private static void testUpdate(TestMapper testMapper, Long id) {
        log.info("testMapper--------------testUpdate------------------");
        log.info("{}", testMapper.load(id));
        Test update = new Test();
        update.setId(id);
        update.setStringType("after update");
        log.info("{}", testMapper.update(update));
        log.info("{}", testMapper.load(id));
    }


    private static void testDeletes(TestMapper testMapper, List<Long> ids) {
        log.info("testMapper--------------testDeletes&loads------------------");
        log.info("{}", ids);
        log.info("{}", testMapper.loads(ids));
        log.info("{}", testMapper.deletes(ids));
        log.info("{}", testMapper.loads(ids));
    }

    private static void testDelete(TestMapper testMapper, Long id) {
        log.info("testMapper--------------testDelete------------------");
        log.info("{}", testMapper.delete(id));
    }

    private static void testCreates(TestMapper testMapper) {
        log.info("testMapper--------------testCreates------------------");
        List<Test> lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add(Test.gen());
        }
        testMapper.creates(lists);
        log.info("{}", lists);
    }

    private static Long testCreate(TestMapper testMapper) {
        log.info("testMapper--------------testCreate------------------");
        Test test = Test.gen();
        testMapper.create(test);
        log.info("{}", test);
        return test.getId();
    }

    private static void testPaging(TestDao testDao, Test test) {
        log.info("testDao--------------testPaging------------------");
        log.info("{}", testDao.paging(test, PageParam.valueOfByPageNo(1, 4)));
    }

    private static void testList(TestDao testDao, Test test) {
        log.info("testDao--------------testList&count------------------");
        log.info("{}", testDao.list(test));
        log.info("{}", testDao.count(test));
    }

    private static void testUpdate(TestDao testDao, Long id) {
        log.info("testDao--------------testUpdate------------------");
        log.info("{}", testDao.load(id));
        Test update = new Test();
        update.setId(id);
        update.setStringType("after update");
        log.info("{}", testDao.update(update));
        log.info("{}", testDao.load(id));
    }


    private static void testDeletes(TestDao testDao, List<Long> ids) {
        log.info("testDao--------------testDeletes&loads------------------");
        log.info("{}", ids);
        log.info("{}", testDao.loads(ids));
        log.info("{}", testDao.deletes(ids));
        log.info("{}", testDao.loads(ids));
    }

    private static void testDelete(TestDao testDao, Long id) {
        log.info("--------------testDelete------------------");
        log.info("{}", testDao.delete(id));
    }

    private static void testCreates(TestDao testDao) {
        log.info("testDao--------------testCreates------------------");
        List<Test> lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add(Test.gen());
        }
        testDao.creates(lists);
        log.info("{}", lists);
    }

    private static Long testCreate(TestDao testDao) {
        log.info("testDao--------------testCreate------------------");
        Test test = Test.gen();
        testDao.create(test);
        log.info("{}", test);
        return test.getId();
    }


}
