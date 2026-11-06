package kr.fasol.obix.core.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import kr.fasol.obix.core.exception.UserException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > PDF 파일 관리
 * 프로그램명 : PDF 파일 관리
 * 설명 :
 *
 * 클래스 : PDFUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 10. 14.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 10. 14.         정영탁          최초생성
 */
@Slf4j
@UtilityClass
public class PDFUtil {

	/**
	 * PDF 파일 병합
	 * @param streamOfPDFFiles : List형 PDF목록 
	 */
	public static byte[] mergePDF(List<InputStream> streamOfPDFFiles) throws Exception {
		return mergePDF(streamOfPDFFiles, false);
	}

	/**
	 * PDF 파일 병합
	 * @param streamOfPDFFiles : List형 PDF목록 
	 * @param paginate : 페이지번호 표시여부
	 */
	public static byte[] mergePDF(List<InputStream> streamOfPDFFiles, boolean paginate) throws Exception {
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);

		Document document = new Document();
		PdfWriter writer = null;
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			writer = PdfWriter.getInstance(document, bufferedOutputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
		} catch (Exception e) {
			throw new UserException("CB1003", e); // 잘못된 요청입니다.
		} finally {
			if (document.isOpen()) {
				document.close();
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * HTML 페이지를 PDF로 변환
	 * @param html : 변환할 HTML 내용
	 * @param css : HTML에 적용할 CSS 내용
	 * @param fontPath : CSS에 적용할 폰트 파일 경로
	 */
	public static byte[] createPDF(String html, String css, String fontPath) throws Exception {
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);

		Document document = new Document();
		PdfWriter writer = null;
		try {
			// PDF 파일 생성
			writer = PdfWriter.getInstance(document, bufferedOutputStream);
			// PDF 파일에 사용할 폰트 크기 설정
			writer.setInitialLeading(12.5f);
			// PDF 파일 열기
			document.open();

			// CSS 설정 변수 세팅
			CSSResolver cssResolver = new StyleAttrCSSResolver();
			if (css != null) {
				CssFile cssFile = null;
				try {
					cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes(StandardCharsets.UTF_8)));
				} catch (Exception ee) {
					throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
				}
				if (cssFile == null) {
					throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
				}

				// CSS 파일 적용
				cssResolver.addCss(cssFile);
			}

			// PDF 파일에 HTML 내용 삽입
			XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

			// 폰트 설정
			if (fontPath != null) {
				try {
					fontProvider.register(fontPath);
				} catch (Exception ee) {
					throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
				}
				if (fontProvider.getRegisteredFonts() == null) {
					throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
				}
			}

			// 사용할 폰트를 담아두었던 내용을 CssAppliers에 담아 적용
			CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

			// HTML Pipeline 생성
			HtmlPipelineContext htmlPipelineContext = new HtmlPipelineContext(cssAppliers);
			htmlPipelineContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

			// Pipelines
			PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, writer);
			HtmlPipeline htmlPipeline = new HtmlPipeline(htmlPipelineContext, pdfWriterPipeline);
			CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

			// XMLWorker
			XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
			XMLParser xmlParser = new XMLParser(true, xmlWorker, StandardCharsets.UTF_8);

			// HTML 내용을 PDF 파일에 삽입
			StringReader stringReader = new StringReader(html);
			// XML 파싱
			xmlParser.parse(stringReader);

		} catch (Exception e) {
			throw new UserException("CB1003", e); // 잘못된 요청입니다.
		} finally {
			if (document.isOpen()) {
				document.close();
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

}
