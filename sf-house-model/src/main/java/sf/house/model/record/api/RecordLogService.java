package sf.house.model.record.api;

/**
 * Created by nijianfeng on 2018/10/28.
 */
public interface RecordLogService {

    Integer record(RecordLogContext.IRecordType recordType, RecordLogDto recordLogDto);

}
