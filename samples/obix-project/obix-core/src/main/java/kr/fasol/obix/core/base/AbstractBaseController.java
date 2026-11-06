package kr.fasol.obix.core.base;

import jakarta.servlet.http.HttpServletRequest;
import kr.fasol.obix.core.util.ControllerUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 설명 : Controller에서 상속받는 BaseController
 *
 * 클래스  AbstractBaseController.class
 * 작성자  정영탁
 * 작성일   2017. 7. 15.
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017. 7. 15.        정영탁           최초생성
 */
public abstract class AbstractBaseController {

    // ---------------------------------------------------------- Helper Methods : View
    /**
     * RequestMapping을 파싱하여 기본 ViewName을 반환
     */
    public String getDefaultViewName(HttpServletRequest request) {
        return ControllerUtils.getDefaultViewName(request);
    }

	/**
	 * 설명 : view로 redirection
	 * @param viewResource 이동할 URL (예: main.do)
	 */
	public ModelAndView getRedirectView(String viewResource) {
        return ControllerUtils.getRedirectView(viewResource);
	}

	/**
	 * 설명 : view로 redirection
	 * @param viewName 이동할 URL (예: main.do)
	 * @param model request parameter
	 */
	public ModelAndView getRedirectView(String viewName, Map<String, ?> model) {
        return ControllerUtils.getRedirectView(viewName, model);
	}

}
