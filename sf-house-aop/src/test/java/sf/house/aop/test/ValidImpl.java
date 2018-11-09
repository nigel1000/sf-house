package sf.house.aop.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sf.house.aop.annotation.PreDefine;
import sf.house.aop.define.Valid;
import sf.house.aop.define.enums.RunType;

/**
 * Created by hznijianfeng on 2018/9/6.
 */

@Component
@Slf4j
public class ValidImpl implements Valid, ValidTest {

    @Override
    public void validSelf() {
        log.info("ValidImpl validSelf ... ");
    }

    @Override
    @PreDefine(runType = {RunType.VALID})
    public Long valid(ValidTest valid, int num) {
        return 1L;
    }
}
