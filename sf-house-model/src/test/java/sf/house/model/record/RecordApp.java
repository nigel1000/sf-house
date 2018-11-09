package sf.house.model.record;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sf.house.bean.util.IdUtil;
import sf.house.model.record.api.RecordLogContext;
import sf.house.model.record.api.RecordLogDto;
import sf.house.model.record.api.RecordLogService;
import sf.house.model.record.demo.DemoRecordContext;

/**
 * Created by nijianfeng on 2018/9/12.
 */
public class RecordApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        RecordLogService recordLogService = (RecordLogService) applicationContext.getBean("recordLogServiceImpl");
        recordLogService.record(DemoRecordContext.ModuleType.DEMO_MODULE.setBizType(DemoRecordContext.BizType.JOB_ADD), RecordLogDto.gen(IdUtil.uuidHex()));
        recordLogService.record(DemoRecordContext.ModuleType.DEMO_MODULE.setBizType(RecordLogContext.CommonBizType.ADD), RecordLogDto.gen(IdUtil.uuidHex()));
    }

}
