package kr.fasol.obix.common.enums;

import lombok.Getter;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 파일 처리를 위한 상수값 정의
 * 프로그램명 : 파일 처리를 위한 상수값 정의
 * 설명 :
 *
 * 클래스 : FileConstant.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 9. 8.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 9. 8.          정영탁          최초생성
 */
public class FileConstant {

	/**
	 * 파일 저장공간 위치
	 */
	@Getter
	public static enum STORG_DIRECTORY_ROOT {
		프로필("/prfi_img/", "yyyy/MM/", true, "jpg,jpeg,png", "프로필"),
		공지사항("/ntc/", "yyyy/MM/", false, "pdf,jpg,jpeg,png", "컨텐츠 게시판 공지사항"),
		알림템플릿("/noti/", "yyyy/MM/", false, "", "UMS 알림 템플릿"),
		게시이미지("/pup_bann/", "yyyy/MM/", false, "jpg,jpeg,png", "공지사항/배너/팝업/이벤트 등의 게시 이미지 파일"),
		Admin첨부파일("/adm_blps/", "yyyy/MM/", false, "", "어드민 게시판 첨부파일"),

		EXAMPLE("/example/", "yyyy/MM/dd/", false, "csv,doc,xls,ppt,txt,pdf,bmp,gif,jpg,jpeg,tif,tiff,jtd,docx,xlsx,pptx,hwp,gul,dib,png,rtf,pps,xlsb,xlsm,ppsx,json", "테스트");

		private String rootPath;	// 서버의 Root 경로
		private String postfixPattern;	// 업무소분류 디렉토리 pattern
		private boolean cryptYn;	// 암호화여부
		private String fileExt;	// 저장할 파일 확장자명(,로 구분)
		private String bzwkClsfiNm;	// 업무분류명
		STORG_DIRECTORY_ROOT(String rootPath, String postfixPattern, boolean cryptYn, String fileExt, String bzwkClsfiNm) {
			this.rootPath = rootPath;
			this.postfixPattern = postfixPattern;
			this.cryptYn = cryptYn;
			this.fileExt = fileExt;
			this.bzwkClsfiNm = bzwkClsfiNm;
		}
	}

	/**
	 * 업무분류별 디렉토리 정의
	 */
	@Getter
	public static enum SUB_DIRECTORY {
		/*
		 * 게시판
		 */
		BBS_파일("bbs/file/", "게시판 파일"),
		BBS_이미지("bbs/image/", "게시판 이미지");

		String dirPath;	// 업무소분류 디렉토리
		String bzwkClsfiNm;	// 업무분류명
		SUB_DIRECTORY(String dirPath, String bzwkClsfiNm) {
			this.dirPath = dirPath;
			this.bzwkClsfiNm = bzwkClsfiNm;
		}
	}

}