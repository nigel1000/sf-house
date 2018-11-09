package sf.house.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.PreDefine;
import sf.house.aop.define.enums.RunType;
import sf.house.aop.define.handlers.base.DefineHandlerFactory;
import sf.house.aop.define.handlers.base.PreDefineHandler;
import sf.house.aop.util.AspectUtil;

import javax.annotation.Resource;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Aspect
@Component
@Order(value = -1)
@Slf4j
public class PreDefineAspectJ {

    @Pointcut("@within(sf.house.aop.annotation.PreDefine)")
    public void clazz() {}

    @Pointcut("@annotation(sf.house.aop.annotation.PreDefine)")
    public void method() {}

    @Resource
    private DefineHandlerFactory defineHandlerFactory;

    @Before("clazz() || method()")
    public void before(final JoinPoint point) {
        PreDefine preDefine = AspectUtil.getAnnotation(point, PreDefine.class);
        RunType[] runTypes = preDefine.runType();
        if (runTypes.length == 0) {
            return;
        }
        for (RunType runType : runTypes) {
            PreDefineHandler preDefineHandler = defineHandlerFactory.createHandler(runType);
            if (preDefineHandler != null) {
                preDefineHandler.handler(point, preDefine);
            }
        }
    }

}

