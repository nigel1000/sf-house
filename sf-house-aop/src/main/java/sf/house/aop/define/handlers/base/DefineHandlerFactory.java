package sf.house.aop.define.handlers.base;


import org.springframework.stereotype.Component;
import sf.house.aop.define.enums.RunType;
import sf.house.aop.define.handlers.ValidHandler;

import javax.annotation.Resource;

/**
 * Created by hznijianfeng on 2018/9/2.
 */

@Component
public class DefineHandlerFactory {

    @Resource
    private ValidHandler validHandler;


    public PreDefineHandler createHandler(RunType runType) {
        if (runType == null) {
            return null;
        }
        switch (runType) {
            case VALID:
                return validHandler;
        }
        return null;
    }
}
