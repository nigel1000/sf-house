package sf.house.mybatis.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.bean.base.PageHelper;
import sf.house.bean.beans.page.PageParam;
import sf.house.mybatis.test.dao.TestMapper;
import sf.house.mybatis.test.domain.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by nijianfeng on 2018/8/16.
 */
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 从Spring 容器中获取内容
        TestMapper testMapper = (TestMapper) applicationContext.getBean("testMapper");

        Long id = testCreate(testMapper);
        testCreates(testMapper);

        testUpdate(testMapper, id);
        testDelete(testMapper, id);
        testDeletes(testMapper, Arrays.asList(testCreate(testMapper), testCreate(testMapper)));
        testList(testMapper, new Test());
        testPaging(testMapper, new Test());
    }

    private static void testPaging(TestMapper testMapper, Test test) {
        System.out.println("--------------testPaging------------------");
        System.out.println(PageHelper.paging(testMapper, test, PageParam.valueOfByPageNo(1, 4)));
    }

    private static void testList(TestMapper testMapper, Test test) {
        System.out.println("--------------testList&count------------------");
        System.out.println(testMapper.list(test));
        System.out.println(testMapper.count(test));
    }

    private static void testUpdate(TestMapper testMapper, Long id) {
        System.out.println("--------------testUpdate------------------");
        System.out.println(testMapper.load(id));
        Test update = new Test();
        update.setId(id);
        update.setStringType("after update");
        System.out.println(testMapper.update(update));
        System.out.println(testMapper.load(id));
    }


    private static void testDeletes(TestMapper testMapper, List<Long> ids) {
        System.out.println("--------------testDeletes&loads------------------");
        System.out.println(ids);
        System.out.println(testMapper.loads(ids));
        System.out.println(testMapper.deletes(ids));
        System.out.println(testMapper.loads(ids));
    }

    private static void testDelete(TestMapper testMapper, Long id) {
        System.out.println("--------------testDelete------------------");
        System.out.println(testMapper.delete(id));
    }

    private static void testCreates(TestMapper testMapper) {
        System.out.println("--------------testCreates------------------");
        List<Test> lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            lists.add(genTest());
        }
        testMapper.creates(lists);
        System.out.println(lists);
    }

    private static Long testCreate(TestMapper testMapper) {
        System.out.println("--------------testCreate------------------");
        Test test = genTest();
        testMapper.create(test);
        System.out.println(test);
        return test.getId();
    }

    private static Test genTest() {
        Test test = new Test();
        test.setBigintType(1L);
        test.setBitType(Boolean.FALSE);
        test.setCharType("char");
        test.setDatetimeType(new Date());
        test.setDateType(new Date());
        test.setDecimalType(new BigDecimal(1));
        test.setDoubleType(new BigDecimal("11.11"));
        test.setIntType(3);
        test.setMediumintType(23L);
        test.setMediumtextType("你好");
        test.setSmallintType(23);
        test.setStringType("测试");
        test.setTinyintType(3);
        return test;
    }

}
