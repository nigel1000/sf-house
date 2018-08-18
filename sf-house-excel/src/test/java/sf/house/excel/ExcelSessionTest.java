package sf.house.excel;

import com.google.common.collect.Lists;
import sf.house.excel.excps.ExcelParseException;
import sf.house.excel.excps.ExcelParseExceptionInfo;
import sf.house.excel.vo.ReaderSheetVO;
import sf.house.excel.vo.WriterSheetVO;

import java.util.Date;
import java.util.List;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

public class ExcelSessionTest {

    private static String path;

    static {
        path = ExcelSessionTest.class.getResource("/").getPath();
        if (path.contains("C:")) {
            path = path.substring(1, path.indexOf("target")) + "src/test/resources";
        } else {
            path = path.substring(0, path.indexOf("target")) + "src/test/resources";
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("path:\t" + path);

        testCopySheet();
        testExport();
        testExportByTemplate();
        testExcelParse();

    }

    private static void testExcelParse() {

        ExcelSession excelSession = new ExcelSession(path + "/ExcelSession.xlsx");
        try {
            List<ReaderSheetVO> retList = excelSession.excelParse(1, ReaderSheetVO.class);
            System.out.println(retList);
        } catch (ExcelParseException e) {
            StringBuilder sb = new StringBuilder();
            List<ExcelParseExceptionInfo> errInfos = e.getInfoList();
            if (errInfos != null && errInfos.size() > 0) {
                for (ExcelParseExceptionInfo errInfo : errInfos) {
                    sb.append(errInfo.getSheetName()).append("第" + errInfo.getRowNum()).append("行").append("，")
                            .append("字段“").append(errInfo.getColumnName()).append("”").append("，")
                            .append(errInfo.getErrMsg()).append(";");
                }
            }
            System.out.println(sb.toString());
        }


    }

    private static void testExportByTemplate() {

        ExcelSession excelSession = new ExcelSession(path + "/ExcelSession.xlsx");
        excelSession.changeSheet("测试");
        excelSession.export(genExportData(), WriterSheetVO.class, 4);

        excelSession.changeSheet("测试2");
        excelSession.export(genExportData(), WriterSheetVO.class);

        // 返回字节 可以通过response返回前端现在
        // excelSession.getBytes();

        excelSession.save(path + "/ExcelSessionExportByTemplate.xlsx");
    }

    private static void testExport() {
        ExcelSession excelSession = new ExcelSession(ExcelSession.ExcelType.XLSX, "测试1");
        excelSession.export(genExportData(), WriterSheetVO.class);

        excelSession.changeSheet("测试2");
        excelSession.export(genExportData(), WriterSheetVO.class, 4);

        // 返回字节 可以通过response返回前端现在
        // excelSession.getBytes();

        excelSession.save(path + "/ExcelSessionExport.xlsx");
    }

    private static void testCopySheet() {
        ExcelSession excelSession = new ExcelSession(path + "/ExcelSession.xlsx");
        excelSession.copySheet("拷贝的sheet1");
        excelSession.copySheet("拷贝的sheet2");

        excelSession.save(path + "/ExcelSessionCopy.xlsx");
    }

    private static List<WriterSheetVO> genExportData() {
        WriterSheetVO o1 = new WriterSheetVO();
        o1.setAttr1("我是测试数据1");
        o1.setAttr2(12);
        o1.setAttr3(22L);
        o1.setAttr4(22.22);
        o1.setDate1(new Date());
        o1.setDate2(new Date());
        WriterSheetVO o2 = new WriterSheetVO();
        o2.setAttr1("我是测试数据2");
        o2.setAttr2(12);
        o2.setAttr3(22L);
        o2.setAttr4(22.22);
        o2.setDate1(new Date());
        o2.setDate2(new Date());

        List<WriterSheetVO> data = Lists.newArrayList(o1, o2);
        return data;
    }

}
