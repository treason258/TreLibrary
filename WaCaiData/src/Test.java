import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {

    private static String TAG = Test.class.getSimpleName();

    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println(TAG);
        printExcel();
        System.out.println("--------------------------------");

    }

    private static void printExcel() {
        Workbook workbook;
        Sheet sheet;
        Row row;
        List<Map<String, String>> list = null;
        String cellData;
        File file = new File("xls/test.xls");
        String columns[] = {"name", "age", "score"};

        workbook = readExcel(file);
        if (workbook != null) {
            //用来存放表中数据
            list = new ArrayList<>();
            //获取第一个sheet
            sheet = workbook.getSheetAt(0);
            //获取最大行数
            int rowCount = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int columnsCount = row.getPhysicalNumberOfCells();
            for (int i = 0; i < rowCount; i++) {
                Map<String, String> map = new LinkedHashMap<>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < columnsCount; j++) {
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }
        // 遍历解析出来的list
        for (Map<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
            }
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
    private static Object getCellFormatValue(Cell cell) {
        Object cellValue;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}
