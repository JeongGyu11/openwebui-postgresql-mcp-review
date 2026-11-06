package kr.fasol.obix.admin.biz.common.controller;

import org.springframework.stereotype.Controller;

import kr.fasol.obix.core.base.AbstractBaseController;

/**
 * 첨부파일 다운로드 Controller
 * 
 * @author 정영탁
 */
@Controller
public class DownloadController extends AbstractBaseController {
	

//
//	@RequestMapping(value = "/common/download.*")
//	public void download(ModelMap map, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		// parameter를 읽어온다.
//		String fullName = getStringParameter(request, "fullName", "");
//		String fileName = getStringParameter(request, "fileName", "");
//
//		// 파일을 연다.
//		File file = new File(Constants.ATTACH_BASE_DIR + fullName);
//
//		if (file != null && file.isFile() && file.exists()) {
//			DownloadUtil.downloadFile(response, request, fileName);
//
//			// 파일 다운로드
//			BufferedInputStream fin = null;
//		    BufferedOutputStream outs = null;
//
//		    try {
//		    	fin = new BufferedInputStream(new FileInputStream(file));
//		    	outs = new BufferedOutputStream(response.getOutputStream());
//
//		    	byte[] buffer = new byte[1024];
//		    	int read = 0;
//		    	while ((read = fin.read(buffer)) != -1) {
//		    		outs.write(buffer, 0, read);
//		    	}
//		    } catch (IOException ex) {
//		    	ex.printStackTrace();
//		    }
//		    finally {
//		    	try { outs.close(); } catch (Exception ex1) { }
//		        try { fin.close(); } catch (Exception ex2) { }
//		    }
//		}
//
//	}

}
