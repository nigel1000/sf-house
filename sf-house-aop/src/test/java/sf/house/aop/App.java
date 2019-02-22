package sf.house.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.aop.order.demo.OrderTest;
import sf.house.aop.test.User;
import sf.house.aop.test.UserDao;
import sf.house.aop.test.ValidTest;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
public class App {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 从Spring 容器中获取内容
        UserDao userDao = (UserDao) applicationContext.getBean("userDao");
        // catchExcp
        log.info("---------------------addUser---------------------------");
        userDao.addUser(User.getInstance());
        log.info("---------------------getUser---------------------------");
        userDao.getUser(true);
        log.info("---------------------getUser---------------------------");
        userDao.getUser(false);

        // preDefine
        ValidTest valid = (ValidTest) applicationContext.getBean("validImpl");
        valid.valid(valid, 2);

        log.info("--------------------- order ---------------------------");
        OrderTest orderTest = (OrderTest)applicationContext.getBean("orderTest");
        log.info("--------------------- excp ---------------------------");
        orderTest.excp();
        log.info("--------------------- ret ---------------------------");
        orderTest.ret();
    }
}
