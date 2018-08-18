package sf.house.aop.util;

import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@FunctionalInterface
public interface Execute<P> {

    void execute(List<P> params);
}
