package sf.house.aop.define.handlers.base;

import org.aspectj.lang.JoinPoint;
import sf.house.aop.annotation.PreDefine;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

public interface PreDefineHandler {

    void handler(JoinPoint point, PreDefine preDefine);

}
