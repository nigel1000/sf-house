package sf.house.aop.order.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.bean.excps.UnifiedException;

/**
 * Created by hznijianfeng on 2019/2/22.
 */

@Slf4j
@Component
public class OrderTest {

    public void ret() {
        log.info(this.getClass().getName() + " ret doing");
    }

    public void excp() {
        log.info(this.getClass().getName() + " excp doing");
        throw UnifiedException.gen("抛出异常");
    }

//[main] INFO sf.house.aop.App - --------------------- order ---------------------------
//[main] INFO sf.house.aop.App - --------------------- excp ---------------------------
//[main] INFO sf.house.aop.order.Aop1 - sf.house.aop.order.Aop1  begin
//[main] INFO sf.house.aop.order.Aop2 - sf.house.aop.order.Aop2  begin
//[main] INFO sf.house.aop.order.Aop3 - sf.house.aop.order.Aop3  begin
//[main] INFO sf.house.aop.order.demo.OrderTest - sf.house.aop.order.demo.OrderTest excp begin
//[main] INFO sf.house.aop.order.Aop3 - sf.house.aop.order.Aop3  exception
//[main] INFO sf.house.aop.order.Aop3 - sf.house.aop.order.Aop3  finally   只要这里有正常返回，下面的切面就不会再接到exception了，哪怕你catch住再抛出来。
//[main] INFO sf.house.aop.order.Aop2 - sf.house.aop.order.Aop2  end
//[main] INFO sf.house.aop.order.Aop2 - sf.house.aop.order.Aop2  finally
//[main] INFO sf.house.aop.order.Aop1 - sf.house.aop.order.Aop1  end
//[main] INFO sf.house.aop.order.Aop1 - sf.house.aop.order.Aop1  finally
//[main] INFO sf.house.aop.App - --------------------- ret ---------------------------
//[main] INFO sf.house.aop.order.Aop1 - sf.house.aop.order.Aop1  begin
//[main] INFO sf.house.aop.order.Aop2 - sf.house.aop.order.Aop2  begin
//[main] INFO sf.house.aop.order.Aop3 - sf.house.aop.order.Aop3  begin
//[main] INFO sf.house.aop.order.demo.OrderTest - sf.house.aop.order.demo.OrderTest ret begin
//[main] INFO sf.house.aop.order.Aop3 - sf.house.aop.order.Aop3  end
//[main] INFO sf.house.aop.order.Aop3 - sf.house.aop.order.Aop3  finally
//[main] INFO sf.house.aop.order.Aop2 - sf.house.aop.order.Aop2  end
//[main] INFO sf.house.aop.order.Aop2 - sf.house.aop.order.Aop2  finally
//[main] INFO sf.house.aop.order.Aop1 - sf.house.aop.order.Aop1  end
//[main] INFO sf.house.aop.order.Aop1 - sf.house.aop.order.Aop1  finally


}
