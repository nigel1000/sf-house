package sf.house.excel.excps;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ExcelParseException extends Exception {

    private List<ExcelParseExceptionInfo> infoList = new ArrayList<>();

    private boolean collectAllTips;
    private int collectLimit = 25;

    public ExcelParseException(boolean collectAllTips) {
        this.collectAllTips = collectAllTips;
    }

    public void addInfo(ExcelParseExceptionInfo info) {
        if (info == null) {
            return;
        }
        if (collectAllTips) {
            infoList.add(info);
        } else {
            if (infoList.size() > collectLimit) {
                if (!infoList.contains(info)) {
                    infoList.add(info);
                }
            } else {
                infoList.add(info);
            }
        }
    }

    public void addInfo(List<ExcelParseExceptionInfo> infos) {
        for (ExcelParseExceptionInfo info : infos) {
            addInfo(info);
        }
    }

    public boolean isEmptyInfo() {
        return infoList.size() <= 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("please pay attention to ").append(infoList.size()).append(" tips :").append(System.lineSeparator());
        for (ExcelParseExceptionInfo info : infoList) {
            sb.append(info);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}

