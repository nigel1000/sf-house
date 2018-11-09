package sf.house.model.retry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.bean.beans.page.PageResult;
import sf.house.bean.excps.UnifiedException;
import sf.house.model.retry.api.RetryMsgDto;
import sf.house.model.retry.api.RetryMsgService;
import sf.house.model.retry.demo.DemoRetryMeta;
import sf.house.model.retry.demo.DemoRetryProcess;

/**
 * Created by nijianfeng on 2018/9/12.
 */
public class RetryApp {


    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        DemoRetryProcess demoRetryProcess = (DemoRetryProcess) applicationContext.getBean("demoRetryProcess");
        RetryMsgService retryMsgService = (RetryMsgService) applicationContext.getBean("retryMsgServiceImpl");

        retryMsgService.create(
                RetryMsgDto.gen(PageResult.empty(), UnifiedException.gen("RetryApp", "test")),
                DemoRetryMeta.DEMO);
        demoRetryProcess.handleRetry();
    }

}
