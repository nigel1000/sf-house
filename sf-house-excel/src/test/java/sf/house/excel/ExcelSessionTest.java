package sf.house.excel;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import sf.house.excel.base.ErrorEnum;
import sf.house.excel.enums.ExcelBool;
import sf.house.excel.excps.ExcelParseException;
import sf.house.excel.vo.ReaderSheetVO;
import sf.house.excel.vo.WriterSheetVO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hznijianfeng on 2018/8/14.
 */

@Slf4j
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

    public static void main(String[] args) {
        log.info("path:\t" + path);

//        testCopySheet();
        testExport();
//        testExportByTemplate();
//        testExcelParse();

    }

    private static void testExcelParse() {
        ExcelParse excelParse = new ExcelParse(path + "/ExcelSession.xlsx");
        excelParse.setCollectAllTips(true);
        try {
            List<ReaderSheetVO> retList = excelParse.excelParse(1, ReaderSheetVO.class);
            log.info("{}", retList);
        } catch (ExcelParseException e) {
            log.info("{}", e);
            Map<ErrorEnum, CellStyle> maps = new HashMap<>();
            maps.put(ErrorEnum.REGEX, createCellStyle(excelParse, IndexedColors.ROSE.getIndex()));
            maps.put(ErrorEnum.MAX, createCellStyle(excelParse, IndexedColors.RED.getIndex()));
            maps.put(ErrorEnum.OPTION_LIST, createCellStyle(excelParse, IndexedColors.GREEN.getIndex()));
            excelParse.setErrorCellStyle(e, maps);
            excelParse.save(path + "/ExcelSessionImport.xlsx");
        }
    }

    private static CellStyle createCellStyle(ExcelParse excelParse, short color) {
        CellStyle cellStyle = excelParse.createDefaultCellStyle();
        cellStyle.setFillBackgroundColor(color);
        cellStyle.setFillBackgroundColor(color);
        cellStyle.setTopBorderColor(color);
        cellStyle.setBottomBorderColor(color);
        cellStyle.setRightBorderColor(color);
        cellStyle.setLeftBorderColor(color);
        return cellStyle;
    }

    private static void testExportByTemplate() {

        ExcelExport excelExport = new ExcelExport(path + "/ExcelSession.xlsx");
        excelExport.changeSheet("测试");
        excelExport.export(genExportData(), WriterSheetVO.class, 4);

        excelExport.changeSheet("测试2");
        excelExport.export(genExportData(), WriterSheetVO.class);

        // 返回字节 可以通过response返回前端现在
        // excelSession.getBytes();

        excelExport.save(path + "/ExcelSessionExportByTemplate.xlsx");
    }

    private static void testExport() {
        List<WriterSheetVO> vos = Lists.newArrayList();
        for (int i = 0; i < 10000; i++) {
            vos.addAll(genExportData());
        }
        ExcelExport excelExport = new ExcelExport(ExcelSession.ExcelType.XLSX, "测试1");
        excelExport.export(vos, WriterSheetVO.class);

        excelExport.changeSheet("测试2");
        excelExport.export(vos, WriterSheetVO.class, 4);

        // 返回字节 可以通过response返回前端现在
        // excelSession.getBytes();

        excelExport.save(path + "/ExcelSessionExport.xlsx");
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
        o1.setExcelBool(ExcelBool.FALSE);
        WriterSheetVO o2 = new WriterSheetVO();
        o2.setAttr1("我是测试数据2");
        o2.setAttr2(12);
        o2.setAttr3(22L);
        o2.setAttr4(22.22);
        o2.setDate1(new Date());
        o2.setDate2(new Date());
        o2.setExcelBool(ExcelBool.TRUE);

        List<WriterSheetVO> data = Lists.newArrayList(o1, o2);
        return data;
    }

}
