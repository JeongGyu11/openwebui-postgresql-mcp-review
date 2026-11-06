package kr.fasol.obix.core.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.fasol.obix.core.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import java.util.Map;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Excel Download > XSSFWorkbook 형태로 엑셀 다운로드
 * 설명 : AbstractXlsxView를 상속받은 기본적인 엑셀파일 생성
 *
 * @Class  ExcelXlsxView.class
 * @author  정영탁
 * @since   2017-08-17
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-17        정영탁    최초생성
 */
public class ExcelXlsxView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExcelUtil.createExcel(workbook, model, request, response);
    }
}