package sf.house.model.record;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import sf.house.aop.util.BeanUtil;
import sf.house.bean.excps.UnifiedException;
import sf.house.model.record.api.RecordLogContext;
import sf.house.model.record.api.RecordLogDto;
import sf.house.model.record.api.RecordLogService;
import sf.house.model.record.dao.RecordLogMapper;

import javax.annotation.Resource;

/**
 * Created by nijianfeng on 2018/10/28.
 */

@Component
public class RecordLogServiceImpl implements RecordLogService {

    @Resource
    private RecordLogMapper recordLogMapper;

    @Override
    public Integer record(RecordLogContext.IRecordType recordType, @NonNull RecordLogDto recordLogDto) {
        recordLogDto.setId(null);
        if (recordType == null || recordType.getBizType() == null) {
            throw UnifiedException.gen("模块类型不能为空");
        }
        RecordLogContext.IBizType bizType = recordType.getBizType();
        if (bizType == null) {
            throw UnifiedException.gen("业务类型不能为空");
        }
        recordLogDto.setBizType(bizType.getBizType());
        recordLogDto.setBizTypeName(bizType.getBizName());
        recordLogDto.setModuleType(recordType.getModuleType());
        recordLogDto.setModuleTypeName(recordType.getModuleName());
        return recordLogMapper.create(BeanUtil.genBean(recordLogDto, RecordLog.class));
    }
}
