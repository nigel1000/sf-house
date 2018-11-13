package sf.house.project.demo.druid;

import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import com.alibaba.druid.support.logging.Log;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by nijianfeng on 2018/11/14.
 */

@Component
public class DefineStatLogger implements DruidDataSourceStatLogger {

    @Override
    public void log(DruidDataSourceStatValue druidDataSourceStatValue) {

    }

    @Override
    public void configFromProperties(Properties properties) {

    }

    @Override
    public void setLogger(Log log) {

    }

    @Override
    public void setLoggerName(String s) {

    }
}
