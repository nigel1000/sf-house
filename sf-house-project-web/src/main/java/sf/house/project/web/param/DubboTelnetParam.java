package sf.house.project.web.param;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import sf.house.bean.excps.UnifiedException;

import java.io.Serializable;

/**
 * Created by hznijianfeng on 2018/10/9.
 */

@Data
public class DubboTelnetParam implements Serializable {


    private String className;
    private String methodName;
    private String methodParam;
    private String group;
    private String version;
    private String zkAddress;
    private Integer zkPort;


    public void validSelf() {
        if (StringUtils.isEmpty(className)) {
            throw UnifiedException.gen("类名 不能为空");
        }
        if (StringUtils.isEmpty(methodName)) {
            throw UnifiedException.gen("方法名 不能为空");
        }
        if (StringUtils.isEmpty(methodParam)) {
            this.methodParam = "";
        }
        if (StringUtils.isEmpty(group)) {
            throw UnifiedException.gen("服务组 不能为空");
        }
        if (StringUtils.isEmpty(version)) {
            throw UnifiedException.gen("服务版本 不能为空");
        }
        if (StringUtils.isEmpty(zkAddress)) {
            throw UnifiedException.gen("注册中心地址 不能为空");
        }
        if (zkPort == null) {
            throw UnifiedException.gen("注册中心端口 不能为空");
        }
    }

}
