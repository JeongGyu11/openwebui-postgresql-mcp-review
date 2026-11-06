package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.xpath.XPathAPI;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > XML 데이터 유틸
 * 프로그램명 : XML 데이터 유틸
 * 설명 :
 * 예제 :
 * <pre>
 * 1. 조회
 *   Document xmlDoc = XmlDocumentUtil.getDocumentFromString("XML 문자열");
 *   
 *   String value = XmlDocumentUtil.getNodeValue(xmlDoc, "//Envelope/Body/response/result/text()");
 *   String value = XmlDocumentUtil.getTextContent(xmlDoc, "//Envelope/Body/response/result");
 *   
 *   NodeList dataList = XmlDocumentUtil.getNodeList(xmlDoc, "//Envelope/Body/response/data");
 *   if (dataList != null) {
 *     for (int i = 0; i < dataList.getLength(); i++) {
 *       String bundleNm = XmlDocumentUtil.getTextContent(dataList.item(i), "bundleNm");
 *     }
 *   }
 * 
 * 2. XML 생성
 *   Document xmlDoc = XmlDocumentUtil.createDocument();
 *
 *   Element root = XmlDocumentUtil.createElement(xmlDoc, "Envelope");
 *     Element header = XmlDocumentUtil.createElement(xmlDoc, root, "Header");
 *       Element commonHeader = XmlDocumentUtil.createElement(xmlDoc, header, "commonHeader");
 *         Element apiKeyHashCode = XmlDocumentUtil.createElement(xmlDoc, commonHeader, "apiKeyHashCode", "내용");
 *     Element body = XmlDocumentUtil.createElement(xmlDoc, root, "Body");
 *       Element field1 = XmlDocumentUtil.createElement(xmlDoc, body, "field1", "내용");
 *
 *   String xmlStr = XmlDocumentUtil.toXMLString(xmlDoc);
 *   log.debug("XML 내용 = {}", xmlStr);
 * </pre>
 * 
 * @apiNote
 * <pre>
 * 	xalan-2.7.2.jar, serializer-2.7.2.jar 추가
 * </pre>
 *
 * 클래스 : XmlDocumentUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2022. 11. 15.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2022. 11. 15.         정영탁          최초생성
 */
@Slf4j
@UtilityClass
public class XmlDocumentUtil {

	/**
	 * XML 문자열을 Document 형식으로 파싱
	 */
	public static Document getDocumentFromString(String xmlDoc) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xmlDoc));
		return builder.parse(is);
	}

	/**
	 * XML Document에서 XPath로 검색된 Single 문자열 반환
	 */
	public static String getNodeValue(Document doc, String xpath) {
		Assert.notNull(doc, "Document is null");
		return getNodeValue(doc.getFirstChild(), xpath);
	}

	/**
	 * XML Node에서 XPath로 검색된 Single 문자열 반환
	 */
	public static String getNodeValue(Node contextNode, String xpath) {
		Assert.notNull(contextNode, "Node is null");

		try {
			Node node = XPathAPI.selectSingleNode(contextNode, xpath);
			if (node != null) {
				return node.getNodeValue();
			}
		} catch (TransformerException e) {
			log.error("### [XmlDocumentUtil.getValue] [xpath=" + xpath + "]");
		}
		return null;
	}

	/**
	 * XML Document에서 XPath로 검색된 목록 반환
	 */
	public static List<String> getValues(Document doc, String xpath) {
		Assert.notNull(doc, "Document is null");
		return getValues(doc.getFirstChild(), xpath);
	}

	/**
	 * XML Node에서 XPath로 검색된 목록 반환
	 */
	public static List<String> getValues(Node contextNode, String xpath) {
		Assert.notNull(contextNode, "Node is null");

		List<String> list = new ArrayList<>();
		try {
			NodeList nodeList = XPathAPI.selectNodeList(contextNode, xpath);
			for (int i = 0; i < nodeList.getLength(); i++) {
				list.add( nodeList.item(i).getNodeValue() );
			}
			return list;
		} catch (TransformerException e) {
			log.error("### [XmlDocumentUtil.getValues] [xpath=" + xpath + "]");
		}
		return null;
	}

	/**
	 * XML Document에서 XPath로 검색된 Single 문자열 반환
	 */
	public static String getTextContent(Document doc, String xpath) {
		Assert.notNull(doc, "Document is null");
		return getTextContent(doc.getFirstChild(), xpath);
	}

	/**
	 * XML Node에서 XPath로 검색된 Single 문자열 반환
	 */
	public static String getTextContent(Node contextNode, String xpath) {
		Assert.notNull(contextNode, "Node is null");

		try {
			Node node = XPathAPI.selectSingleNode(contextNode, xpath);
			if (node != null) {
				return node.getTextContent();
			}
		} catch (TransformerException e) {
			log.error("### [XmlDocumentUtil.getValue] [xpath=" + xpath + "]");
		}
		return null;
	}

	/**
	 * XML Node에서 XPath로 검색된 Single Node 반환
	 */
	public static Node getNode(Node contextNode, String xpath) {
		Assert.notNull(contextNode, "Node is null");

		try {
			return XPathAPI.selectSingleNode(contextNode, xpath);
		} catch (TransformerException e) {
			log.error("### [XmlDocumentUtil.getValue] [xpath=" + xpath + "]");
		}
		return null;
	}

	/**
	 * XML Document에서 XPath로 검색된 NodeList 반환
	 */
	public static NodeList getNodeList(Document doc, String xpath) {
		Assert.notNull(doc, "Document is null");
		return getNodeList(doc.getFirstChild(), xpath);
	}

	/**
	 * XML Node에서 XPath로 검색된 NodeList 반환
	 */
	public static NodeList getNodeList(Node contextNode, String xpath) {
		Assert.notNull(contextNode, "Node is null");

		try {
			return XPathAPI.selectNodeList(contextNode, xpath);
		} catch (TransformerException e) {
			log.error("### [XmlDocumentUtil.getNodeList] [xpath=" + xpath + "]");
		}
		return null;
	}

	/**
	 * Document 신규 생성
	 */
	public static Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		doc.setXmlStandalone(true);	// standalone="no" 제거
		doc.setXmlVersion("1.0");
		return doc;
	}

	/**
	 * 최상위 Element 생성
	 * @param doc XML Document
	 * @param tagName 최상위 Element를 생성할 태그 명
	 */
	public static Element createElement(Document doc, String tagName) {
		return createElement(doc, null, tagName, null);
	}
	/**
	 * 하위의 Element 생성 (데이터 없이 Element 태그만 생성)
	 * @param doc XML Document
	 * @param parentElement Element를 생성할 부모 Element
	 * @param tagName Element를 생성할 태그 명
	 */
	public static Element createElement(Document doc, Element parentElement, String tagName) {
		return createElement(doc, parentElement, tagName, null);
	}
	/**
	 * 하위의 Element 생성 (데이터를 포함하여 생성)
	 * @param doc XML Document
	 * @param parentElement Element를 생성할 부모 Element
	 * @param tagName Element를 생성할 태그 명
	 * @param textContent Element의 내용에 추가할 데이터
	 */
	public static Element createElement(Document doc, Element parentElement, String tagName, String textContent) {
		Assert.notNull(doc, "Document is null");
    	Element elem = doc.createElement(tagName);
    	if (textContent != null) {
    		elem.setTextContent(textContent);
    	}
    	if (parentElement != null) {
    		parentElement.appendChild(elem);
    	} else {
    		doc.appendChild(elem);
    	}
		return elem;
	}

	/**
	 * XML 문자열로 변환
	 * @param doc XML Document
	 */
	public static String toXMLString(Document doc) throws TransformerConfigurationException, TransformerException {
		Assert.notNull(doc, "Document is null");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(out);

		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();

		// 출력 시 문자코드 UTF-8
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// 들여쓰기 있음
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);

		return new String(out.toByteArray(), StandardCharsets.UTF_8);
	}

}
