package sf.house.aop.define.handlers.base;

import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import sf.house.aop.annotation.PreDefine;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Data
public class PreDefineMeta {

    private JoinPoint point;
    private PreDefine preDefine;
    private Object target;
    private Class targetType;
    private Class returnType;
    private Object[] args;

    public PreDefineMeta(JoinPoint point, PreDefine preDefine) {
        this.point = point;
        this.preDefine = preDefine;
        this.returnType = ((MethodSignature) point.getSignature()).getReturnType();
        this.target = point.getTarget();
        this.targetType = point.getTarget().getClass();
        this.args = point.getArgs();
    }

}
