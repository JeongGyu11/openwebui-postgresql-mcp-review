package kr.fasol.obix.admin.biz.common.controller;

import org.springframework.stereotype.Controller;

import kr.fasol.obix.core.base.AbstractBaseController;

/**
 * Editor의 Uploader를 구현하기 위한 컨트롤러
 * 
 * @author 정영탁
 */
@Controller
public class UploaderController extends AbstractBaseController {
	

//	// 첨부파일 업로드 경로
//	private static final String BASE_ATTACH_DIR = PropertiesHelper.getString("attach.base_dir");
//	private static final String BASE_ATTACH_URL = PropertiesHelper.getString("attach.base_url");
//	private static final String ATTACH_PATH = PropertiesHelper.getString("attach.upload.editor.baseDir");
//
//
//	/**
//	 * Naver SmartEditor의 이미지 업로드
//	 */
//	@RequestMapping(value = "/common/uploader/smarteditor.*")
//	public ModelAndView smarteditor(ModelMap map, MultipartHttpServletRequest request, @RequestParam("uploadInputBox") MultipartFile file) throws Exception {
//
//		FileType type = FileType.IMAGE;	// 업로드 방식(Image, File, Flash)
//
//		String callback = getStringParameter(request, "callback");
//		String callback_func = getStringParameter(request, "callback_func");
//		String url = callback + "?callback_func=" + callback_func;
//
//		// 첨부파일 업로드
//		if (file != null && !file.isEmpty() && FileUtil.getAllowExtensionCheck(file, type)) {
//			FileInfo fileInfo = FileUtil.saveAttachFiles(file
//					, BASE_ATTACH_DIR + ATTACH_PATH + "/" + type.getString() + "/"
//					, BASE_ATTACH_URL + ATTACH_PATH + "/" + type.getString() + "/");
//			if (fileInfo != null) {
//				// return attributes
//				url += "&size=" + fileInfo.fileSize;
//				url += "&url=" + java.net.URLEncoder.encode(fileInfo.fileUrl, WebConstants.CHAR_ENCODING);
//				url += "&sFileName=" + java.net.URLEncoder.encode(fileInfo.fileName, WebConstants.CHAR_ENCODING);
//				url += "&sFileURL=" + java.net.URLEncoder.encode(fileInfo.fileUrl, WebConstants.CHAR_ENCODING);
//				url += "&bNewLine=" + true;
//			}
//			else {
//				url += "&errstr=error";
//			}
//		}
//		else {
//			url += "&errstr=error";
//		}
//
//		return getRedirectView(url);
//	}
//
//	/**
//	 * Naver SmartEditor의 이미지 업로드
//	 */
//	@RequestMapping(value = "/common/uploader/smarteditor.json")
//	public List<Map<String, Object>> smarteditor(@RequestParam("uploadInputBox") MultipartFile file, MultipartHttpServletRequest request, Model model) throws Exception {
//
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		FileType type = FileType.IMAGE;	// 업로드 방식(Image, File, Flash)
//
//		// 첨부파일 업로드
//		if (file != null && !file.isEmpty() && FileUtil.getAllowExtensionCheck(file, type)) {
//			FileInfo fileInfo = FileUtil.saveAttachFiles(file
//					, BASE_ATTACH_DIR + ATTACH_PATH + "/" + type.getString() + "/"
//					, BASE_ATTACH_URL + ATTACH_PATH + "/" + type.getString() + "/");
//			if (fileInfo != null) {
//				// return attributes
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("sFileName", fileInfo.fileName);
//				map.put("sFileURL", fileInfo.fileUrl);
//				map.put("bNewLine", true);
//				list.add(map);
//			}
//		}
//
//		return list;
//	}
//
//	/**
//	 * CKEditor의 simpleuploader
//	 */
//	@RequestMapping(value = "/common/uploader/simpleuploader.*")
//	public void simpleuploader(ModelMap map, MultipartHttpServletRequest request, HttpServletResponse response, @RequestParam("upload") MultipartFile file) throws Exception {
//
//		FileType type = FileType.valueOf(getStringParameter(request, "Type"));	// 업로드 방식(Image, File, Flash)
//		String CKEditorFuncNum = getStringParameter(request, "CKEditorFuncNum");
//
//		// 출력값
////		int errorNumber = 0;
//		String fileUrl = "";
////		String fileName = "";
//		String customMsg = "";
//
//		/*
//		 * Return Code
//		 * 0 = No errors
//		 * 1 = Custom errors
//		 * 101 = Custom warning
//		 * 201 = file with the same name is already available
//		 * 202 = Invalid file type
//		 * 203 = Security error
//		 * 500 = The connector is disabled 
//		 */
//		if (FileType.IMAGE.equals(type) || FileType.FILE.equals(type) || FileType.FLASH.equals(type)) {
//			if (!FileUtil.getAllowExtensionCheck(file, type)) {
////				errorNumber = 1;
//				customMsg = "업로드 가능한 허용 확장자가 아닙니다.";
//			} else {
//				FileInfo fileInfo = FileUtil.saveAttachFiles(file
//						, BASE_ATTACH_DIR + ATTACH_PATH + "/" + type.getString() + "/"
//						, BASE_ATTACH_URL + ATTACH_PATH + "/" + type.getString() + "/");
//				if (fileInfo != null) {
////					errorNumber = 0;
//				} else {
////					errorNumber = 1;
//					customMsg = "파일 업로드에 실패했습니다.";
//				}
//			}
//		}
//		else {
////			errorNumber = 202;
//		}
//
//		// return attributes
//		String html = "";
//		html += "<script type=\"text/javascript\">\n";
//		html += "window.parent.CKEDITOR.tools.callFunction(" + CKEditorFuncNum + ", '" + fileUrl + "', '" + customMsg + "');\n";	// CKEditor
//		//html += "window.parent.OnUploadCompleted(" + errorNumber + ",'" + fileUrl + "','" + fileName + "','" + customMsg + "');\n";	// FCKEditor
//		html += "</script>\n";
//
//		response.setCharacterEncoding(WebConstants.CHAR_ENCODING);
//		writeResponse(response, html);
//	}

}
