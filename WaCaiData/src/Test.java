import bean.OutlayBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {

    private static String TAG = Test.class.getSimpleName();

    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println(TAG);
        printExcel();
        System.out.println("--------------------------------");

    }

    private static void printExcel() {
        File file = new File("xls/wacai_2013-01-2013-12.xls");
        List<OutlayBean> list = new ArrayList<>(); // 用来存放表中数据

        Workbook workbook = readExcel(file);
        if (workbook != null) {
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 获取最大行数
            int rowCount = sheet.getPhysicalNumberOfRows();
            // 获取第一行
            Row row = sheet.getRow(0);
            // 获取最大列数
            int columnsCount = row.getPhysicalNumberOfCells();
            for (int i = 0; i < rowCount; i++) {
                OutlayBean outlayBean = new OutlayBean();
                row = sheet.getRow(i);
                if (row != null) {
                    outlayBean.setCategoryParent(getDataFromCell(row.getCell(0)));
                    outlayBean.setCategoryChild(getDataFromCell(row.getCell(1)));
                    outlayBean.setAccount(getDataFromCell(row.getCell(2)));
                    outlayBean.setCurrency(getDataFromCell(row.getCell(3)));
                    outlayBean.setProject(getDataFromCell(row.getCell(4)));
                    outlayBean.setSeller(getDataFromCell(row.getCell(5)));
                    outlayBean.setRepay(getDataFromCell(row.getCell(6)));
                    outlayBean.setDate(getDataFromCell(row.getCell(7)));
                    outlayBean.setPrice(getDataFromCell(row.getCell(8)));
                    outlayBean.setPriceMember(getDataFromCell(row.getCell(9)));
                    outlayBean.setRemark(getDataFromCell(row.getCell(10)));
                    outlayBean.setAccountBook(getDataFromCell(row.getCell(11)));
                } else {
                    break;
                }
                list.add(outlayBean);
            }
        }
        // 遍历解析出来的list
        for (OutlayBean outlayBean : list) {
            System.out.print(outlayBean.toString());
            System.out.println();
        }
    }

    /**
     * 读取Excel文件
     */
    private static Workbook readExcel(File file) {
        if (file == null) {
            return null;
        }

        Workbook workbook = null;
        String filePath = file.getAbsolutePath();
        String extString = filePath.substring(filePath.lastIndexOf("."));

        InputStream inputStream;
        try {
            inputStream = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (".xlsx".equals(extString)) {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 读取单元格
     */
    private static String getDataFromCell(Cell cell) {
        String data = "";
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    data = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    data = cell.getRichStringCellValue().getString();
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 转换为日期格式YYYY-mm-dd
                        data = DateFormat.getDateInstance().format(cell.getDateCellValue());
                    } else {
                        // 数字
                        data = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
            }
        }
        return data;
    }
}
