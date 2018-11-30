package sf.house.bean.util;

import java.util.function.Supplier;

/**
 * Created by hznijianfeng on 2018/11/22.
 */

public class LogicUtil {

    // 级联判断条件配置 中心配置和地区配置似的层级配置
    public static boolean ifCan(Boolean centerConfig, Boolean... descConfigs) {
        if (centerConfig == null) {
            centerConfig = Boolean.FALSE;
        }
        if (descConfigs == null) {
            return centerConfig;
        }
        int length = descConfigs.length;
        for (int i = length - 1; i >= 0; i--) {
            if (descConfigs[i] != null) {
                return descConfigs[i];
            }
        }
        return centerConfig;
    }

    public static boolean ifCan(Supplier<Boolean> centerConfig, Supplier<Boolean>... descConfigs) {
        Boolean centerConfigValue;
        if (centerConfig == null) {
            centerConfigValue = Boolean.FALSE;
        } else {
            centerConfigValue = centerConfig.get();
            if(centerConfigValue == null){
                centerConfigValue = Boolean.FALSE;
            }
        }
        if (descConfigs == null) {
            return centerConfigValue;
        }
        int length = descConfigs.length;
        for (int i = length - 1; i >= 0; i--) {
            if (descConfigs[i] != null) {
                Boolean tempValue = descConfigs[i].get();
                if (tempValue != null) {
                    return tempValue;
                }
            }
        }
        return centerConfigValue;
    }

}
