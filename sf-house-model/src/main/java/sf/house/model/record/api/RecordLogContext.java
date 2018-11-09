package sf.house.model.record.api;

import lombok.Getter;

public class RecordLogContext {


    public interface IRecordType {

        Integer getModuleType();

        String getModuleName();

        IBizType getBizType();

    }

    public interface IBizType {

        Integer getBizType();

        String getBizName();
    }

    public enum CommonBizType implements IBizType {

        NULL(-1, "异常状态"),

        ADD(10001, "添加"),
        EDIT(10002, "编辑"),
        DEL(10003, "删除"),;

        @Getter
        private Integer code;
        @Getter
        private String desc;

        CommonBizType(Integer code, String desc) {
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