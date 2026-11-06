package kr.fasol.obix.core.util;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UrlPathHelper;

import java.util.Map;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Util > Controller의 공통 유틸
 * 프로그램명 : Controller의 공통 유틸
 * 설명 :
 * <p>
 * 클래스 : ControllerUtils.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 22.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 22.         정영탁          최초생성
 */
@UtilityClass
public class ControllerUtils {

	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

	// ---------------------------------------------------------- Helper Methods : View
	/**
	 * RequestMapping을 파싱하여 기본 ViewName을 반환
	 */
	public String getDefaultViewName(HttpServletRequest request) {
		String requestUri = urlPathHelper.getRequestUri(request);
		if (requestUri.indexOf('.', requestUri.lastIndexOf('/')) > -1) {
			requestUri = requestUri.substring(0, requestUri.lastIndexOf('.'));
		}

		return requestUri;
	}

	/**
	 * 설명 : view로 redirection
	 * @param viewResource 이동할 URL (예: main.do)
	 */
	public ModelAndView getRedirectView(String viewResource) {
		RedirectView view = new RedirectView(viewResource);
		view.setEncodingScheme("UTF-8");
		return new ModelAndView(view);
	}

	/**
	 * 설명 : view로 redirection
	 * @param viewName 이동할 URL (예: main.do)
	 * @param model request parameter
	 */
	public ModelAndView getRedirectView(String viewName, Map<String, ?> model) {
		RedirectView view = new RedirectView(viewName);
		view.setEncodingScheme("UTF-8");
		return new ModelAndView(view, model);
	}

	// ---------------------------------------------------------- ServletRequestUtils Delegator
	// Boolean
	public Boolean getBooleanParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getBooleanParameter(request, name);
	}

	public boolean getBooleanParameter(ServletRequest request, String name, boolean defaultVal) {
		return ServletRequestUtils.getBooleanParameter(request, name, defaultVal);
	}

	public boolean[] getBooleanParameters(ServletRequest request, String name) {
		return ServletRequestUtils.getBooleanParameters(request, name);
	}

	// Double
	public Double getDoubleParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getDoubleParameter(request, name);
	}

	public double getDoubleParameter(ServletRequest request, String name, double defaultVal) {
		return ServletRequestUtils.getDoubleParameter(request, name, defaultVal);
	}

	public double[] getDoubleParameters(ServletRequest request, String name) {
		return ServletRequestUtils.getDoubleParameters(request, name);
	}

	// Float
	public Float getFloatParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getFloatParameter(request, name);
	}

	public float getFloatParameter(ServletRequest request, String name, float defaultVal) {
		return ServletRequestUtils.getFloatParameter(request, name, defaultVal);
	}

	public float[] getFloatParameters(ServletRequest request, String name) {
		return ServletRequestUtils.getFloatParameters(request, name);
	}

	// Integer
	public Integer getIntParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getIntParameter(request, name);
	}

	public int getIntParameter(ServletRequest request, String name, int defaultVal) {
		return ServletRequestUtils.getIntParameter(request, name, defaultVal);
	}

	public int[] getIntParameters(ServletRequest request, String name) {
		return ServletRequestUtils.getIntParameters(request, name);
	}

	// Long
	public Long getLongParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getLongParameter(request, name);
	}

	public long getLongParameter(ServletRequest request, String name, long defaultVal) {
		return ServletRequestUtils.getLongParameter(request, name, defaultVal);
	}

	public long[] getLongParameters(ServletRequest request, String name) {
		return ServletRequestUtils.getLongParameters(request, name);
	}

	// String
	public String getStringParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getStringParameter(request, name);
	}

	public String getStringParameter(ServletRequest request, String name, String defaultVal) {
		String value = ServletRequestUtils.getStringParameter(request, name, defaultVal);
		if (StringUtils.isEmpty(value)) {
			return defaultVal;
		}
		return value;
	}

	public String[] getStringParameters(ServletRequest request, String name) {
		return ServletRequestUtils.getStringParameters(request, name);
	}

	// Required Boolean
	public boolean getRequiredBooleanParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredBooleanParameter(request, name);
	}

	public boolean[] getRequiredBooleanParameters(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredBooleanParameters(request, name);
	}

	// Required Double
	public double getRequiredDoubleParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredDoubleParameter(request, name);
	}

	public double[] getRequiredDoubleParameters(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredDoubleParameters(request, name);
	}

	// Required Float
	public float getRequiredFloatParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredFloatParameter(request, name);
	}

	public float[] getRequiredFloatParameters(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredFloatParameters(request, name);
	}

	// Required Integer
	public int getRequiredIntParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredIntParameter(request, name);
	}

	public int[] getRequiredIntParameters(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredIntParameters(request, name);
	}

	// Required String
	public String getRequiredStringParameter(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredStringParameter(request, name);
	}

	public String[] getRequiredStringParameters(ServletRequest request, String name) throws ServletRequestBindingException {
		return ServletRequestUtils.getRequiredStringParameters(request, name);
	}

}
