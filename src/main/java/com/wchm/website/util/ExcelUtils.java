package com.wchm.website.util;

import com.wchm.website.entity.Currency;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 读取Excel文件
 */
public class ExcelUtils {

    /**
     * 读取Excel方法
     */
    public static List<Currency> readFromExcel(HttpServletRequest request) {
        MultipartFile excelFile; // Excel文件对象
        InputStream is = null; // 输入流对象
        String cellStr; // 单元格，最终按字符串处理
        List<Currency> dataList = new ArrayList<>(); // 返回封装数据的List
        Currency currency; // 每一个雇员信息对象

        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator item = multipartRequest.getFileNames();
            while (item.hasNext()) {
                String fileName = (String) item.next();
                excelFile = multipartRequest.getFile(fileName);
                is = excelFile.getInputStream();
            }
            if (is == null) {
                return null;
            }

            Workbook workbook2007 = WorkbookFactory.create(is);
            Sheet sheet = workbook2007.getSheetAt(0);

            // 开始循环遍历行，表头不处理，从1开始
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                currency = new Currency(); // 实例化Student对象
                Row row = sheet.getRow(i); // 获取行对象
                if (row == null) { // 如果为空，不处理
                    continue;
                }

                // 循环遍历单元格
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j); // 获取单元格对象
                    if (cell == null) { // 单元格为空设置cellStr为空串
                        cellStr = "";
                    } else {
                        cellStr = cleeFormatString(cell);
                    }

                    // 下面按照数据出现位置封装到bean中
                    if (j == 0) {
                        BigDecimal b = null;
                        try {
                            b = new BigDecimal(cellStr);
                        } catch (Exception e) {
                        }
                        currency.setCurrency(b);
                    } else if (j == 1) {
                        currency.setUser_name(cellStr);
                    } else if (j == 2) {
                        Double d = null;
                        try {
                            d = cell.getNumericCellValue();
                        } catch (Exception e) {
                        }
                        if (d != null) {
                            // 手机号单独处理
                            DecimalFormat df = new DecimalFormat("0");
                            cellStr = df.format(d);
                            currency.setMobile(cellStr);
                        }
                    } else if (j == 3) {
                        currency.setAddress(cellStr);
                    } else if (j == 4) {
                        currency.setRemarks(cellStr);
                    } else if (j == 5) {
                        currency.setLock_describe(cellStr);
                    }
                    currency.setSurplus(currency.getCurrency());
                }
                if (!StringUtils.isEmpty(currency.getAddress()) && currency.getCurrency() != null)
                    dataList.add(currency); // 数据装入List
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally { // 关闭文件流
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataList;
    }

    private static String cleeFormatString(Cell cell) {
        String cellStr;
        if (cell.getCellType() == CellType.NUMERIC) { // 对数字值的处理
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                //如果是date类型则 ，获取该cell的date值
                Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cellStr = format.format(date);
            } else {// 纯数字
                BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                cellStr = big.doubleValue() + "";
            }
        } else if (cell.getCellType() == CellType.STRING) { // 对String的处理
            cellStr = cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.FORMULA) { // 对公式的处理
            cellStr = String.valueOf(cell.getNumericCellValue());
            if (cellStr.equals("NaN")) { // 如果获取的数据值为非法值,则转换为获取字符串
                cellStr = cell.getStringCellValue();
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            cellStr = " " + cell.getBooleanCellValue();
        } else {
            cellStr = cell.getStringCellValue();
        }
        return cellStr;
    }


}
