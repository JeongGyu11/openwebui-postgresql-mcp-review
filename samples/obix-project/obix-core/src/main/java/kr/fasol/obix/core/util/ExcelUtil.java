package kr.fasol.obix.core.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.enums.ExcelConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ContentDisposition;
import org.springframework.web.util.HtmlUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 시스템명 :
 * 업무구분명 : F/W > 공통 유틸 > 엑셀파일 생성 유틸
 * 프로그램명 : 엑셀 파일 생성 유틸
 * 설명 : 엑셀 파일을 생성하는 유틸리티 클래스
 *
 * @Class  ExcelUtil.class
 * @author  정영탁
 * @since   2017-08-16
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-16        정영탁    최초생성
 */
public class ExcelUtil {

    private Workbook workbook;
    private Map<String, Object> model;
    private HttpServletResponse response;
    private HttpServletRequest request;

    private static final String FONT_NAME = "Malgun Gothic";

    /**
     * 엑셀 파일을 생성합니다.
     *
     * @throws Exception 예외 발생 시
     */
    public static void createExcel(Workbook workbook, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExcelUtil excelUtil = new ExcelUtil(workbook, model, request, response);
        excelUtil.createExcel();
    }

    /**
     * ExcelUtil 생성자
     *
     * @param workbook 엑셀 워크북 객체
     * @param model 데이터 모델
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     */
    private ExcelUtil(Workbook workbook, Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) {
        this.workbook = workbook;
        this.model = model;
        this.request = request;
        this.response = response;
    }

    /**
     * 엑셀 파일을 생성합니다.
     *
     * @throws Exception 예외 발생 시
     */
    private void createExcel() throws Exception {
        setFileName(response, getFileName());
        Sheet sheet = workbook.createSheet();

        if(ExcelConstant.EXCEL_DOWNLOAD_TABLE.equals(getDownloadType())) {
            createData(sheet, getDataList());
        } else if(ExcelConstant.EXCEL_DOWNLOAD_CUSTOM.equals(getDownloadType())) {
            createData(sheet, getDataList(), 1);
        } else {
            createData(sheet, getHeader(), getDataList());
        }
    }

    /**
     * 제목 스타일을 반환합니다.
     *
     * @return 제목 스타일
     */
    private CellStyle getTitleStyle() {
        Font headerFont = workbook.createFont();
        headerFont.setFontName(FONT_NAME);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        setBorder(headerStyle, BorderStyle.THIN);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.LEFT);
        headerStyle.setFont(headerFont);

        return headerStyle;
    }

    /**
     * 헤더 스타일을 반환합니다.
     *
     * @return 헤더 스타일
     */
    private CellStyle getHeaderStyle() {
        Font headerFont = workbook.createFont();
        headerFont.setFontName(FONT_NAME);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        setBorder(headerStyle, BorderStyle.THIN);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);

        return headerStyle;
    }

    /**
     * 데이터 스타일을 반환합니다.
     *
     * @return 데이터 스타일
     */
    private CellStyle getDataStyle() {
        Font font = workbook.createFont();
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints((short) 10);

        CellStyle style = workbook.createCellStyle();
        setBorder(style, BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);

        return style;
    }

    /**
     * 다운로드 타입을 반환합니다.
     *
     * @return 다운로드 타입
     */
    private String getDownloadType() {
        return (String)model.get(ExcelConstant.EXCEL_DOWNLOAD_TYPE);
    }

    /**
     * 파일 이름을 반환합니다.
     *
     * @return 파일 이름
     */
    private String getFileName() {
        return (String)model.get(ExcelConstant.EXCEL_FILE_NAME);
    }

    /**
     * 헤더 정보를 반환합니다.
     *
     * @return 헤더 정보
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getHeader() {
        return (Map<String, String>)model.get(ExcelConstant.EXCEL_HEAD);
    }

    /**
     * 데이터 리스트를 반환합니다.
     *
     * @return 데이터 리스트
     */
    private List<?> getDataList() {
        return (List<?>) model.get(ExcelConstant.EXCEL_DATA);
    }

    /**
     * 포맷 정보를 반환합니다.
     *
     * @return 포맷 정보
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getFormat() {
        return (Map<String, String>)model.get(ExcelConstant.EXCEL_FORMAT);
    }

    /**
     * 파일 이름을 설정합니다.
     *
     * @param response HTTP 응답 객체
     * @param fileName 파일 이름
     */
    private void setFileName(HttpServletResponse response, String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            fileName = ExcelConstant.EXCEL_DEFAULT_FILE_NAME + DateUtil.getToday(DateUtil.FORMATTER_YYYYMMDDHHMMSS);
        }

        String newFileName = setFileExtension(fileName);

        response.setHeader("Content-Disposition", ContentDisposition.builder("attachment").filename(newFileName, StandardCharsets.UTF_8).build().toString());
        response.setHeader("Content-Transfer-Encoding", "binary");
    }

    /**
     * 파일 확장자를 설정합니다.
     *
     * @param fileName 파일 이름
     * @return 확장자가 추가된 파일 이름
     */
    private String setFileExtension(String fileName) {
        String newFileName = fileName;
        if (workbook instanceof XSSFWorkbook) {
            newFileName += ".xlsx";
        } else if (workbook instanceof SXSSFWorkbook) {
            newFileName += ".xlsx";
        } else if (workbook instanceof HSSFWorkbook) {
            newFileName += ".xls";
        }
        return newFileName;
    }

    /**
     * 헤더를 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param header 헤더 정보
     * @param cellStyle 셀 스타일
     */
    @SuppressWarnings("deprecation")
    private void createHeader(Sheet sheet, Map<String, String> header, CellStyle cellStyle) {
        Row row = sheet.createRow(0);
        int cellIdx = 0;

        for (String key : header.keySet()) {
            Cell cell = row.createCell(cellIdx++);
            cell.setCellStyle(cellStyle);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(header.get(key));
        }
    }

    /**
     * 데이터를 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param object 데이터 객체
     * @param row 행 객체
     * @param header 헤더 정보
     * @param format 포맷 정보
     * @param cellStyle 셀 스타일
     * @throws IllegalAccessException 예외 발생 시
     * @throws InvocationTargetException 예외 발생 시
     */
    @SuppressWarnings("deprecation")
    private void createData(Sheet sheet, Object object, Row row, Map<String, String> header, Map<String, String> format, CellStyle cellStyle) throws IllegalAccessException, InvocationTargetException {
        String methodName = null;
        int cellIdx = 0;
        Object value = null;
        String cellValue = null;
        Cell cell;
        CellType cellType = CellType.STRING;

        for (String key : header.keySet()) {
            methodName = "get" + key.substring(0,1).toUpperCase() + key.substring(1);

            try {
                if (object instanceof Map) {
                    value = ((Map<?, ?>) object).get(key);
                } else {
                    value = MethodUtils.invokeMethod(object, true, methodName);
                }

                if(value == null) {
                    cellValue = "";
                } else {
                    if (ExcelConstant.EXCEL_FORMAT_MONEY.equals(format.get(key))) {
                        if (value instanceof BigDecimal t) {
                            t = t.setScale(0, RoundingMode.HALF_UP);
                            cellValue = t.toPlainString();
                        } else if (value instanceof BigInteger t) {
                            cellValue = new String(t.toByteArray());
                        } else {
                            cellValue = String.valueOf(value);
                        }
                        cellType = CellType.NUMERIC;
                    } else if(ExcelConstant.EXCEL_FORMAT_DATETIME.equals(format.get(key))) {
                        if (value instanceof Date) {
                            cellValue = DateUtil.getString((Date)value, DateUtil.FORMATTER_YYYY_MM_DD_HH_MM);
                        } else {
                            cellValue = DateUtil.getFormatDate(String.valueOf(value), DateUtil.FORMATTER_YYYYMMDDHHMMSS, DateUtil.FORMATTER_YYYY_MM_DD_HH_MM);
                        }
                    } else if(ExcelConstant.EXCEL_FORMAT_DATE.equals(format.get(key))) {
                        cellValue = FormatUtil.formatDate(String.valueOf(value), "-");
                    } else if(ExcelConstant.EXCEL_FORMAT_TIME.equals(format.get(key))) {
                        cellValue = FormatUtil.formatTime(String.valueOf(value), ":");
                    } else if(ExcelConstant.EXCEL_FORMAT_TIME_NO_SECOND.equals(format.get(key))) {
                        String v = String.valueOf(value);
                        if (v != null && v.length() < 6) v = v + "00";
                        cellValue = DateUtil.getFormatDate(v, DateUtil.FORMATTER_HHMMSS, DateUtil.FORMATTER_HH_MM);
                    } else if(ExcelConstant.EXCEL_FORMAT_INTEREST.equals(format.get(key))) {
                        cellValue = FormatUtil.formatInterest(String.valueOf(value));
                    } else if(ExcelConstant.EXCEL_FORMAT_ACNO.equals(format.get(key))) {
                        cellValue = FormatUtil.formatAcno(String.valueOf(value), false);
                    } else {
                        cellValue = HtmlUtils.htmlUnescape(String.valueOf(value));
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                cellValue = "";
            }

            cell = row.createCell(cellIdx);
            cell.setCellStyle(cellStyle);
            cell.setCellType(cellType);

            if (ExcelConstant.EXCEL_FORMAT_MONEY.equals(format.get(key)) && StringUtils.isNotBlank(cellValue)) {
                DataFormat df = workbook.createDataFormat();
                cellStyle.setDataFormat(df.getFormat("#,##0"));
                cell.setCellValue(new BigDecimal(cellValue).doubleValue());
            } else {
                cell.setCellValue(cellValue);
            }

            cellIdx++;
        }
    }

    /**
     * 데이터를 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param dataList 데이터 리스트
     * @throws Exception 예외 발생 시
     */
    private void createData(Sheet sheet, List<?> dataList) throws Exception {
        CellStyle headStyle = getHeaderStyle();
        CellStyle style = getDataStyle();
        int columnCnt = 0;

        int rowSize = dataList.size();
        for (int i = 0; i < rowSize; i++) {
            List<?> list = (List<?>) dataList.get(i);
            if (i == 0) {
                columnCnt = list.size();
                createRow(sheet, list, i, headStyle);
            } else {
                createRow(sheet, list, i, style);
            }
        }

        if (!(workbook instanceof SXSSFWorkbook)) {
            for (int i = 0; i < columnCnt; i++) {
                sheet.autoSizeColumn(i, true);
            }
        }
    }

    /**
     * 데이터를 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param dataList 데이터 리스트
     * @param titleNum 제목 번호
     * @throws Exception 예외 발생 시
     */
    private void createData(Sheet sheet, List<?> dataList, int titleNum) throws Exception {
        CellStyle titleStyle = getTitleStyle();
        CellStyle headStyle = getHeaderStyle();
        CellStyle style = getDataStyle();
        int columnCnt = 0;

        int rowSize = dataList.size();
        int idx = 0;
        for (int i = 0; i < rowSize; i++) {
            List<?> list = (List<?>) dataList.get(i);
            if (list != null) {
                if (list.size() == 1) {
                    idx = i + titleNum;
                    createRow(sheet, list, i, titleStyle);
                } else if (idx == i) {
                    columnCnt = list.size();
                    createRow(sheet, list, i, headStyle);
                } else {
                    createRow(sheet, list, i, style);
                }
            }
        }

        if (!(workbook instanceof SXSSFWorkbook)) {
            for (int i = 0; i < columnCnt; i++) {
                sheet.autoSizeColumn(i, true);
            }
        }
    }

    /**
     * 데이터를 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param header 헤더 정보
     * @param dataList 데이터 리스트
     * @throws Exception 예외 발생 시
     */
    private void createData(Sheet sheet, Map<String, String> header, List<?> dataList) throws Exception {
        CellStyle headStyle = getHeaderStyle();
        CellStyle style = getDataStyle();

        createHeader(sheet, header, headStyle);

        List<?> list = getDataList();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                createData(sheet, list.get(i), sheet.createRow(i + 1), header, getFormat(), style);
            }
        }

        if (!(workbook instanceof SXSSFWorkbook)) {
            for (int i = 0; i < header.size(); i++) {
                sheet.autoSizeColumn(i, true);
            }
        }
    }

    /**
     * 행을 생성합니다.
     *
     * @param sheet 엑셀 시트 객체
     * @param cellList 셀 리스트
     * @param rowNum 행 번호
     * @param cellStyle 셀 스타일
     * @throws Exception 예외 발생 시
     */
    @SuppressWarnings("deprecation")
    private void createRow(Sheet sheet, List<?> cellList, int rowNum, CellStyle cellStyle) throws Exception {
        int size = cellList.size();
        Row row = sheet.createRow(rowNum);
        Cell cell;

        for (int i = 0; i < size; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(HtmlUtils.htmlUnescape((String) cellList.get(i)));
        }
    }

    /**
     * 셀 스타일에 테두리를 설정합니다.
     *
     * @param style 셀 스타일
     * @param borderStyle 테두리 스타일
     */
    private void setBorder(CellStyle style, BorderStyle borderStyle) {
        style.setBorderBottom(borderStyle);
        style.setBorderTop(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
    }
}