import com.alibaba.dubbo.rpc.service.GenericService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sf.house.aop.util.JsonUtil;
import sf.house.bean.util.SplitUtil;
import sf.house.util.dubbo.DubboUtil;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/11/12.
 */

@Slf4j
public class DubboUtilTest {

    public static void main(String[] args) {

        String className = "com.api.outer.goods.GoodsReadApiFacade";
        String methodName = "queryGoodsDesc";
        String methodParamType = "java.util.List";
        String methodParamValue = "[[59149962]]";
        String version = "1.0";
        String group = "stable_masterjd";
        String zkAddress = "127.0.0.1";
        Integer zkPort = 2181;

        String[] methodParamTypes = null;
        Object[] methodParamValues = null;
        if (StringUtils.isNotEmpty(methodParamType)) {
            methodParamTypes = SplitUtil.split2Array(methodParamType, ",");
        }
        if (StringUtils.isNotEmpty(methodParamValue)) {
            methodParamValues = JsonUtil.json2bean(methodParamValue, List.class).toArray();
        }

        GenericService service = DubboUtil.getGenericService(className, group, version, zkAddress, zkPort);
        Object result = service.$invoke(methodName, methodParamTypes, methodParamValues);
        log.info("#################{}", result);

        result = DubboUtil.getResultByTelnet(className, methodName, "[59149962]",
                DubboUtil.getUrlByGenericService(className, group, version, zkAddress, zkPort));
        log.info("#################{}", result);

        System.exit(0);
    }

}
