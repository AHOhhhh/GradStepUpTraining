package fun.hercules.order.order.platform.order.utils;


import fun.hercules.order.order.common.constants.DateTimeConstants;
import fun.hercules.order.order.common.errors.ErrorCode;
import fun.hercules.order.order.common.exceptions.BadRequestException;
import fun.hercules.order.order.common.exceptions.InternalServerError;
import fun.hercules.order.order.common.exceptions.PayloadTooLargeException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ExcelUtil {

    private static final int defaultColumnWidth = 20;

    private CellStyle headerStyle;

    public XSSFWorkbook createExcel(String sheetName, List<String> headers, List<List<Object>> bodyList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        initCellStyle(workbook);
        XSSFSheet sheet = createSheet(sheetName, workbook);
        AtomicInteger endRowNum = createHeaderRow(sheet, headers);
        createBodyRows(sheet, headers, bodyList, endRowNum);
        return workbook;
    }

    public static Workbook checkExcelFormat(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            return new XSSFWorkbook();
        }
        if (!isCorrectSuffix(originalFilename)) {
            throw new BadRequestException(ErrorCode.FORMAT_ERROR, "only support xlsx format data");
        }
        try {
            return new XSSFWorkbook(multipartFile.getInputStream());
        } catch (IOException e) {
            throw new InternalServerError(ErrorCode.SERVER_ERROR, "create XSSFWorkbook error");
        }
    }

    public static List<Map<String, Object>> parseExcelBody(Workbook workbook, List<String> headerValues,
                                                           int bodyBeginRow, long maxImportationLines) {
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet.getLastRowNum() > maxImportationLines) {
            throw new PayloadTooLargeException(sheet.getLastRowNum(), maxImportationLines);
        }
        List<Map<String, Object>> excelData = new ArrayList<>();
        Iterator<Row> rows = sheet.rowIterator();
        int rowNum = 0;
        while (rows.hasNext()) {
            if (rowNum < bodyBeginRow) {
                rowNum++;
                rows.next();
                continue;
            }
            Map<String, Object> rowData = getRowData(rows.next(), headerValues);
            if (rowData.size() > 0) {
                excelData.add(rowData);
            }
            rowNum++;
        }

        return excelData;
    }

    public static List<String> parseExcelHeader(Workbook workbook) {
        Row headRow = workbook.getSheetAt(0).getRow(0);
        List<String> headvalues = new ArrayList<>();
        Iterator<Cell> cells = headRow.cellIterator();

        while (cells.hasNext()) {
            Cell cell = cells.next();
            if (StringUtils.isNotEmpty(cell.getStringCellValue())) {
                headvalues.add(cell.getStringCellValue());
            } else {
                throw new BadRequestException(ErrorCode.FORMAT_ERROR, "have empty header data in excel");
            }
        }
        return headvalues;
    }

    private XSSFSheet createSheet(String sheetName, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(defaultColumnWidth);
        return sheet;
    }

    private void initCellStyle(XSSFWorkbook workbook) {
        initHeaderRowStyle(workbook);
    }

    private AtomicInteger createHeaderRow(XSSFSheet sheet, List<String> headers) {
        final AtomicInteger rowNum = new AtomicInteger(0);
        Row row = sheet.createRow(rowNum.getAndAdd(1));
        createHeader(row, headers);
        return rowNum;
    }

    private void createBodyRows(XSSFSheet sheet, List<String> headers, List<List<Object>> bodyList, AtomicInteger endRowNum) {
        final AtomicInteger beginRowNum = new AtomicInteger(endRowNum.get());
        bodyList.forEach(body -> {
            Row row = sheet.createRow(beginRowNum.getAndAdd(1));
            createBody(row, body, headers);
        });
    }

    private void createHeader(Row row, List<String> body) {
        final AtomicInteger colNum = new AtomicInteger(0);
        body.forEach(value -> {
            Cell cell = row.createCell(colNum.getAndAdd(1));
            cell.setCellType(CellType.STRING);
            cell.setCellValue(value);
            cell.setCellStyle(headerStyle);
        });
    }

    private void createBody(Row row, List<?> body, List<String> headers) {
        final AtomicInteger colNum = new AtomicInteger(0);
        Short currentDataFormat = row.getSheet().getWorkbook().createDataFormat().getFormat("¥#,##0.00");
        IntStream.range(0, headers.size())
                .forEach(index -> {
                    Object value = body.get(index);
                    Cell cell = row.createCell(colNum.getAndAdd(1));
                    if (value instanceof BigDecimal) {
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellValue(((BigDecimal) value).doubleValue());
                        updateCurrencyCellFormat(cell, headers.get(index), currentDataFormat);
                    } else if (value instanceof Boolean) {
                        cell.setCellType(CellType.BOOLEAN);
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof Instant) {
                        Date date = Date.from((Instant) value);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeConstants.ISO_DATETIME);
                        dateFormat.setTimeZone(TimeZone.getTimeZone(DateTimeConstants.TIME_ZONE));
                        String dateString = dateFormat.format(date);
                        cell.setCellValue(dateString);
                        cell.setCellType(CellType.STRING);
                    } else {
                        cell.setCellType(CellType.STRING);
                        cell.setCellValue((String) value);
                    }
                });
    }

    private void updateCurrencyCellFormat(Cell cell, String headerName, Short currentDataFormat) {
        if (ExcelHeaderMap.isCurrencyCell(headerName)) {
            cell.getCellStyle().setDataFormat(currentDataFormat);
        }
    }

    private void initHeaderRowStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName("微软雅黑");
        font.setItalic(false);
        font.setBold(true);
        font.setColor(XSSFFont.COLOR_NORMAL);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(Color.pink));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        this.headerStyle = style;
    }

    private static boolean isCorrectSuffix(String fileName) {
        int indexOfSuffix = fileName.lastIndexOf(".");
        String suffix = fileName.substring(indexOfSuffix + 1, fileName.length());

        return suffix.equals("xlsx");
    }

    private static Map<String, Object> getRowData(Row row, List<String> head) {
        return IntStream.rangeClosed(0, head.size() - 1).boxed()
                .collect(Collectors.toMap(head::get, x -> row.getCell(x) != null ? getCellValue(row.getCell(x)) : ""));
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }

    }
}
