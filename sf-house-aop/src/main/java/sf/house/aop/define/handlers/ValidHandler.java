package sf.house.aop.define.handlers;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.PreDefine;
import sf.house.aop.define.Valid;
import sf.house.aop.define.handlers.base.PreDefineHandler;
import sf.house.aop.define.handlers.base.PreDefineMeta;
import sf.house.bean.util.TypeUtil;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Component
public class ValidHandler implements PreDefineHandler {

    @Override
    public void handler(JoinPoint point, PreDefine preDefine) {
        PreDefineMeta preDefineMeta = new PreDefineMeta(point, preDefine);

        Object target = preDefineMeta.getTarget();
        if (target != null && TypeUtil.isAssignableFrom(Valid.class, target.getClass())) {
            ((Valid) target).validSelf();
        }
        Object[] args = preDefineMeta.getArgs();
        for (Object arg : args) {
            if (arg != null && TypeUtil.isAssignableFrom(Valid.class, arg.getClass())) {
                ((Valid) arg).validSelf();
            }
        }
    }
}
