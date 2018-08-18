package sf.house.aop.helper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

/**
 * Created by hznijianfeng on 2018/8/15. 事务细粒度控制，控制到某一业务段
 */

@Transactional
@Service
public class TransactionHelper {

    public void aroundBiz(Runnable biz) {
        biz.run();
    }

    /**
     * 提供返回
     * 
     * @param biz
     * @param <T>
     * @return
     */
    public <T> T aroundBiz(Supplier<T> biz) {
        return biz.get();
    }

}
