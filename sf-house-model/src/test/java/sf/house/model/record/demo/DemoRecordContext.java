package sf.house.model.record.demo;

import lombok.Getter;
import sf.house.model.record.api.RecordLogContext;

/**
 * Created by nijianfeng on 2018/10/28.
 */
public class DemoRecordContext {


    public enum ModuleType implements RecordLogContext.IRecordType {

        NULL(-1, "异常状态"),

        DEMO_MODULE(1, "示例模块"),;

        @Getter
        private Integer code;
        @Getter
        private String desc;
        @Getter
        private RecordLogContext.IBizType bizType;

        ModuleType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        ModuleType(Integer code, String desc, RecordLogContext.IBizType bizType) {
            this.code = code;
            this.desc = desc;
            this.bizType = bizType;
        }

        public ModuleType setBizType(RecordLogContext.IBizType bizType) {
            this.bizType = bizType;
            return this;
        }

        @Override
        public Integer getModuleType() {
            return this.code;
        }

        @Override
        public String getModuleName() {
            return this.desc;
        }
    }


    public enum BizType implements RecordLogContext.IBizType {

        NULL(-1, "异常状态"),

        JOB_ADD(2, "系统 job 自动生成"),;

        @Getter
        private Integer code;
        @Getter
        private String desc;

        BizType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public Integer getBizType() {
            return this.code;
        }

        @Override
        public String getBizName() {
            return this.desc;
        }
    }


}
