package sf.house.util.dubbo.invoker;

import com.alibaba.dubbo.common.URL;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hznijianfeng on 2019/1/17.
 */


public class InvokerParam {

    @Getter
    @Setter
    private String zkAddress;
    @Getter
    @Setter
    private String host = "127.0.0.1";
    @Getter
    @Setter
    private String protocol = "dubbo";
    @Getter
    @Setter
    private String version = "1.0";
    @Getter
    @Setter
    private String group;
    @Getter
    @Setter
    private int port = 20880;
    @Getter
    private String applicationName = "invoker-debug";
    @Getter
    private Class mockClass;
    @Getter
    private String mockClassName;


    public Map<String, String> getParams() {
        Map<String, String> params = Maps.newHashMap();
        params.put("anyhost", "true");
        params.put("application", applicationName);
        params.put("check", "false");
        params.put("default.timeout", "5000");
        params.put("side", "consumer");
        params.put("path", mockClassName);
        params.put("interface", mockClassName);
        params.put("group", group);
        params.put("version", version);
        return params;
    }

    public Map<String, String> getInvokerParam(URL providerUrl) {
        Map<String, String> param = Maps.newHashMap();
        param.put("protocol", providerUrl.getProtocol());
        param.put("address", providerUrl.getAddress());
        param.put("path", mockClassName);
        param.put("group", providerUrl.getParameter("group"));
        return param;
    }

    public void setMockClass(Class mockClass) {
        this.mockClass = mockClass;
        this.mockClassName = mockClass.getName();
    }

}
