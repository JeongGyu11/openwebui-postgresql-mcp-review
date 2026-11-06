package kr.fasol.obix.common.enums;

import java.util.Arrays;

import lombok.Getter;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 공통코드 필드의 상수값 정의
 * 프로그램명 : 공통코드 필드의 상수값 정의
 * 설명 :
 *
 * 클래스 : CodeConstant.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 28.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 28.         정영탁          최초생성
 */
public class CodeConstant {

	/**
	 * 어드민로그인결과코드
	 */
	@Getter
	public static enum ADM_LGIN_RSLT_CD {
		성공("0000", "성공"),
		사용자아이디오류("1001", "사용자아이디오류"),
		비밀번호오류("1002", "비밀번호오류"),
		접근불가IP("1003", "접근불가IP"),
		OTP인증오류("1004", "OTP인증오류"),
		사용자아이디정지("2010", "사용자아이디정지"),
		비밀번호초기화("2020", "비밀번호초기화"),
		비밀번호오류초과("2030", "비밀번호오류초과"),
		비밀번호만기("2040", "비밀번호만기"),
		사용자아이디만기_휴직("2080", "사용자아이디만기(휴직)"),
		사용자아이디만기_퇴사("2090", "사용자아이디만기(퇴사)");
		private String code;
		private String codeName;
		ADM_LGIN_RSLT_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static ADM_LGIN_RSLT_CD getEnumByCode(String cd) {
			return Arrays.stream(ADM_LGIN_RSLT_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 어드민사용자상태코드
	 */
	@Getter
	public static enum ADM_USER_STUS_CD {
		정상("10", "정상"),
		정지("20", "정지 (장기미접속 등)"),
		초기화("30", "초기화 (비밀번호초기화)"),
		잠김("40", "잠김 (비밀번호오류초과)"),
		휴직("80", "휴직"),
		퇴사("90", "퇴사");
		private String code;
		private String codeName;
		ADM_USER_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static ADM_USER_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(ADM_USER_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 신청상태코드
	 */
	@Getter
	public static enum APLN_STUS_CD {
		신규("00", "신규"),
		진행중("10", "진행중"),
		ADMIN대기중("11", "ADMIN대기중"),
		오류대기중("12", "오류대기중"),
		비즈니스거절("20", "비즈니스거절"),
		CSS거절("21", "CSS거절"),
		정밀심사결과거절("23", "정밀심사결과거절"),
		상담원이관("22", "상담원이관"),
		신청취소("30", "신청취소"),
		자동취소("31", "자동취소"),
		실행대기중("90", "실행대기중"),
		완료("99", "완료");
		private String code;
		private String codeName;
		APLN_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static APLN_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(APLN_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 앱메뉴구분코드
	 */
	@Getter
	public static enum APP_MENU_DV_CD {
		화면VIEW("10", "화면(VIEW)"),
		팝업FRAG("20", "팝업(FRAG)"),
		AJAX거래JSON("30", "AJAX거래(JSON)"),
		다운로드FILE("40", "다운로드(FILE)");
		private String code;
		private String codeName;
		APP_MENU_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static APP_MENU_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(APP_MENU_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 배너팝업구분코드
	 */
	@Getter
	public static enum BANN_PUP_DV_CD {
		  배너("BAN", "배너"),
		  팝업("POP", "팝업"),
		  일반긴급공지("NOR", "일반 긴급공지"),
		  시스템긴급공지("SYS", "시스템 긴급공지"),
		  슬라이드배너팝업("SLD", "슬라이드 배너팝업"),
		  전면팝업("FRT", "전면 팝업");
		private String code;
		private String codeName;
		BANN_PUP_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static BANN_PUP_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(BANN_PUP_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 배치주기코드
	 */
	@Getter
	public static enum BTCH_CYCL_CD {
		연초("10", "연초"),
		연말("11", "연말"),
		반기("20", "반기"),
		분기("21", "분기"),
		월초("30", "월초"),
		월말("31", "월말"),
		월중지정일("32", "월중(지정일)"),
		매주("40", "매주"),
		매영업일("50", "매영업일"),
		매일("51", "매일"),
		CRON주기("90", "CRON주기");
		private String code;
		private String codeName;
		BTCH_CYCL_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static BTCH_CYCL_CD getEnumByCode(String cd) {
			return Arrays.stream(BTCH_CYCL_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 배치실행상태코드
	 */
	@Getter
	public static enum BTCH_EXE_STUS_CD {
		실행중("10", "실행중"),
		오류("20", "오류"),
		대기중("30", "대기중"),
		완료("99", "완료");
		private String code;
		private String codeName;
		BTCH_EXE_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static BTCH_EXE_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(BTCH_EXE_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 게시상태코드
	 */
	@Getter
	public static enum BULTN_STUS_CD {
		대기중("10", "대기중"),
		진행중("20", "진행중"),
		완료("99", "완료");
		private String code;
		private String codeName;
		BULTN_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static BULTN_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(BULTN_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 업무상세코드
	 */
	@Getter
	public static enum BZWK_DTL_CD {
		로그인("010", "로그인"),
		기타("999", "기타");
		private String code;
		private String codeName;
		BZWK_DTL_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static BZWK_DTL_CD getEnumByCode(String cd) {
			return Arrays.stream(BZWK_DTL_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 업무구분코드
	 */
	@Getter
	public static enum BZWK_DV_CD {
		기타("999", "기타");
		private String code;
		private String codeName;
		BZWK_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static BZWK_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(BZWK_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 인증수단코드
	 */
	@Getter
	public static enum CERTN_METHD_CD {
		간편핀("1010", "간편핀"),
		휴대전화인증("2010", "휴대전화인증"),
		간편패턴("1020", "간편패턴"),
		공동인증서인증("2020", "공동인증서인증"),
		간편바이오지문("1030", "간편바이오(지문)"),
		사설인증서인증("2030", "사설인증서인증"),
		간편바이오페이스아이디("1031", "간편바이오(페이스아이디)");
		private String code;
		private String codeName;
		CERTN_METHD_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CERTN_METHD_CD getEnumByCode(String cd) {
			return Arrays.stream(CERTN_METHD_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 변경사유코드
	 */
	@Getter
	public static enum CHNG_RSN_CD {
		악성회원("10", "악성회원"),
		비정상거래시도("20", "비정상거래시도"),
		기타("30", "기타"),
		고객요청("40", "고객요청"),
		탈퇴("50", "탈퇴"),
		휴면("90", "휴면"),
		_1차파기("91", "1차파기"),
		_2차파기("92", "2차파기"),
		복구("93", "복구"),
		완전파기("99", "완전파기");
		private String code;
		private String codeName;
		CHNG_RSN_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CHNG_RSN_CD getEnumByCode(String cd) {
			return Arrays.stream(CHNG_RSN_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 채널경로코드
	 */
	@Getter
	public static enum CHNL_PATH_CD {
		PC("P", "PC"),
		Mobile("M", "Mobile"),
		APP("A", "APP"),
		전문("L", "전문");
		private String code;
		private String codeName;
		CHNL_PATH_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CHNL_PATH_CD getEnumByCode(String cd) {
			return Arrays.stream(CHNL_PATH_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 콜백처리상태코드
	 */
	@Getter
	public static enum CLBC_TRT_STUS_CD {
		신규("10", "신규"),
		처리중("20", "처리중"),
		취소("30", "취소"),
		완료("99", "완료");
		private String code;
		private String codeName;
		CLBC_TRT_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CLBC_TRT_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(CLBC_TRT_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 마감상태코드
	 */
	@Getter
	public static enum CLSNG_STUS_CD {
		진행중("10", "진행중"),
		취소("30", "취소"),
		완료("99", "완료");
		private String code;
		private String codeName;
		CLSNG_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CLSNG_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(CLSNG_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 확인직업코드
	 */
	@Getter
	public static enum CNFRM_OCCU_CD {
		급여소득자("10","급여소득자"),
		개인사업자_자영업("20","개인사업자(자영업)"),
		프리랜서("30","프리랜서"),
		주부("40","주부"),
		학생("50","학생"),
		무직("60","무직");
		private String code;
		private String codeName;
		CNFRM_OCCU_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CNFRM_OCCU_CD getEnumByCode(String cd) {
			return Arrays.stream(CNFRM_OCCU_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 동의항목코드
	 */
	@Getter
	public static enum CNSNT_ITEM_CD {
		고유식별정보수집이용동의("10", "고유식별정보 수집/이용 동의"),
		민감정보수집이용동의("11", "민감정보 수집/이용 동의"),
		개인신용정보수집이용동의("12", "개인(신용)정보 수집/이용 동의"),
		고유식별정보제공동의("20", "고유식별정보 제공 동의"),
		개인신용정보제공동의("22", "개인(신용)정보 제공 동의"),
		고유식별정보조회동의("30", "고유식별정보 조회 동의"),
		개인신용정보조회동의("32", "개인(신용)정보 조회 동의"),
		상품서비스안내동의("41", "상품서비스 안내동의"),
		정보수신매체선택("45", "정보수신 매체선택"),
		청약철회권안내("50", "청약 철회권 안내"),
		위법계약해지권안내("51", "위법계약 해지권 안내"),
		금리인하요구권안내("52", "금리인하 요구권 안내"),
		대출거절사유고지신청안내("53", "대출거절사유 고지 신청안내");
		private String code;
		private String codeName;
		CNSNT_ITEM_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CNSNT_ITEM_CD getEnumByCode(String cd) {
			return Arrays.stream(CNSNT_ITEM_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 고객확인직업코드
	 */
	@Getter
	public static enum CUST_CNFRM_OCCU_CD {
		급여소득자("10", "급여소득자"),
		개인사업자자영업("20", "개인사업자(자영업)"),
		프리랜서("30", "프리랜서"),
		주부("40", "주부"),
		학생("50", "학생"),
		무직("60", "무직");
		private String code;
		private String codeName;
		CUST_CNFRM_OCCU_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CUST_CNFRM_OCCU_CD getEnumByCode(String cd) {
			return Arrays.stream(CUST_CNFRM_OCCU_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 고객의견구분코드
	 */
	@Getter
	public static enum CUST_OPIN_DV_CD {
		개선제안("10", "개선/제안"),
		칭찬불만("20", "칭찬/불만");
		private String code;
		private String codeName;
		CUST_OPIN_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static CUST_OPIN_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(CUST_OPIN_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 데이터처리상태코드
	 */
	@Getter
	public static enum DATA_TRT_STUS_CD {
		시작("10", "시작"),
		처리중("20", "처리중"),
		오류("30", "오류"),
		완료("99", "완료");
		private String code;
		private String codeName;
		DATA_TRT_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static DATA_TRT_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(DATA_TRT_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 이메일유형코드
	 */
	@Getter
	public static enum EMAIL_PTRN_CD {
		보안메일("SMAIL", "보안메일"),
		HTML2PDF("HTMLPDF", "HTML2PDF"),
		일반메일("NONE", "일반메일");
		private String code;
		private String codeName;
		EMAIL_PTRN_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static EMAIL_PTRN_CD getEnumByCode(String cd) {
			return Arrays.stream(EMAIL_PTRN_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 금융권코드
	 */
	@Getter
	public static enum FINAC_SPH_CD {
		  은행("1", "은행"),
		  증권사("2", "증권사"),
		  카드사("3", "카드사"),
		  핀테크("5", "핀테크");
		private String code;
		private String codeName;
		FINAC_SPH_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static FINAC_SPH_CD getEnumByCode(String cd) {
			return Arrays.stream(FINAC_SPH_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 금융기관코드
	 */
	@Getter
	public static enum FIORG_CD {
		당행("000","당행"),
		산업은행("002","산업은행"),
		기업은행("003","기업은행"),
		KB국민은행("004","KB국민은행"),
		수협("007","수협"),
		수출입("008","수출입"),
		농협은행("011","농협은행"),
		우리은행("020","우리은행"),
		SC은행("023","SC은행"),
		한국씨티("027","한국씨티"),
		대구은행("031","대구은행"),
		부산은행("032","부산은행"),
		광주은행("034","광주은행"),
		제주은행("035","제주은행"),
		전북은행("037","전북은행"),
		경남은행("039","경남은행"),
		새마을금고("045","새마을금고"),
		신협("048","신협"),
		저축은행("050","저축은행"),
		HSBC("054","HSBC"),
		도이치("055","도이치"),
		JP모간("057","JP모간"),
		BOA("060","BOA"),
		BNPP("061","BNPP"),
		중국공상("062","중국공상"),
		중국은행("063","중국은행"),
		산림조합중앙회("064","산림조합중앙회"),
		중국건설("067","중국건설"), 
		우체국("071","우체국"),
		KEB하나은행("081","KEB하나은행"),
		신한은행("088","신한은행"),
		케이뱅크("089","케이뱅크"),
		카카오뱅크("090","카카오뱅크"),
		토스뱅크("092","토스뱅크"),
		유안타증권("209","유안타증권"),
		KB증권("218","KB증권"),
		BNK투자증권("224","BNK투자증권"),
		IBK투자증권("225","IBK투자증권"),
		KTB투자증권("227","KTB투자증권"),
		미래에셋대우("238","미래에셋대우"),
		삼성증권("240","삼성증권"),
		한국투자증권("243","한국투자증권"),
		NH투자증권("247","NH투자증권"),
		교보증권("261","교보증권"),
		하이투자증권("262","하이투자증권"),
		현대차증권("263","현대차증권"),
		키움증권("264","키움증권"),
		이베스트증권("265","이베스트증권"),
		SK증권("266","SK증권"),
		대신증권("267","대신증권"),
		한화증권("269","한화증권"),
		하나증권("270","하나증권"),
		토스증권("271","토스증권"),
		신한투자증권("278","신한투자증권"),
		DB증권("279","DB증권"),
		유진투자증권("280","유진투자증권"),
		메리츠증권("287","메리츠증권"),
		카카오페이증권("288","카카오페이증권"),
		부국증권("290","부국증권"),
		신영증권("291","신영증권"),
		케이프투자증권("292","케이프투자증권"),
		한국포스증권("294","한국포스증권"),
		;
		private String code;
		private String codeName;
		FIORG_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static FIORG_CD getEnumByCode(String cd) {
			return Arrays.stream(FIORG_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 그룹구분코드
	 */
	@Getter
	public static enum GROUP_DV_CD {
		메인그룹("10", "메인그룹"),
		서브그룹("20", "서브그룹"),
		여신("110", "여신"),
		수신("120", "수신"),
		고객지원("130", "고객지원"),
		마케팅("140", "마케팅"),
		연계채널("150", "연계채널"),
		설정("160", "설정"),
		보고서("170", "보고서"),
		시스템("180", "시스템"),
		공통("190", "공통"),
		담당자("CHGMAN", "담당자"),
		관리자("ADMIN", "관리자"),
		_1차결재권자("APPRMAN1", "1차결재권자"),
		_2차결재권자("APPRMAN2", "2차결재권자"),
		_3차결재권자("APPRMAN3", "3차결재권자"),
		메인("100", "메인"),
		;
		private String code;
		private String codeName;
		GROUP_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static GROUP_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(GROUP_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 휴일코드
	 */
	@Getter
	public static enum HOLDY_CD {
	  공휴일("10", "공휴일"),
	  임시공휴일("20", "임시공휴일"),
	  기타("90", "기타");
		private String code;
		private String codeName;
		HOLDY_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static HOLDY_CD getEnumByCode(String cd) {
			return Arrays.stream(HOLDY_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 유입경로코드
	 */
	@Getter
	public static enum INFL_PATH_CD {
		  전화상담("10", "전화상담"),
		  일대일상담요청("20", "1:1상담요청"),
		  콜백("30", "콜백"),
		  신청중상담원안내("40", "신청중상담원안내");
		private String code;
		private String codeName;
		INFL_PATH_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static INFL_PATH_CD getEnumByCode(String cd) {
			return Arrays.stream(INFL_PATH_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 가입상태코드
	 */
	@Getter
	public static enum JOIN_STUS_CD {
		 가입("10", "가입"),
		 해지("20", "해지");
		private String code;
		private String codeName;
		JOIN_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static JOIN_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(JOIN_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 최종승인구분코드
	 */
	@Getter
	public static enum LAST_APRV_DV_CD {
		승인("01","승인"),
		조건부승인("02","조건부승인"),
		거절("03","거절");
		private String code;
		private String codeName;
		LAST_APRV_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static LAST_APRV_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(LAST_APRV_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 회원연락수단코드
	 */
	@Getter
	public static enum MB_CNTCT_METHD_CD {
	  우편("01", "우편"),
	  자택전화("02", "자택전화"),
	  직장전화("03", "직장전화"),
	  휴대전화("04", "휴대전화"),
	  SMS("06", "SMS"),
	  연락거부("09", "연락거부"),
	  이메일("05", "이메일");
		private String code;
		private String codeName;
		MB_CNTCT_METHD_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static MB_CNTCT_METHD_CD getEnumByCode(String cd) {
			return Arrays.stream(MB_CNTCT_METHD_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 회원식별유형코드
	 */
	@Getter
	public static enum MB_IDNFI_PTRN_CD {
	  이천년이전생남성("1", "2000년 이전 생 남성"),
	  이천년이전생여성("2", "2000년 이전 생 여성"),
	  이천년이후생남성("3", "2000년 이후 생 남성"),
	  이천년이후생여성("4", "2000년 이후 생 여성");
		private String code;
		private String codeName;
		MB_IDNFI_PTRN_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static MB_IDNFI_PTRN_CD getEnumByCode(String cd) {
			return Arrays.stream(MB_IDNFI_PTRN_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 회원상태코드
	 */
	@Getter
	public static enum MB_STUS_CD {
		정상("10", "정상"),
		정지("20", "정지"),
		탈퇴("30", "탈퇴"),
		휴면("90", "휴면"),
		파기1차("91", "1차파기"),
		파기2차("92", "2차파기"),
		완전파기("99", "완전파기");
		private String code;
		private String codeName;
		MB_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static MB_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(MB_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 매체코드
	 */
	@Getter
	public static enum MDIA_CD {
		APP("A", "APP"),
		WEB("W", "WEB"),
		채널연계("L", "채널연계"),
		기타("E", "기타");
		private String code;
		private String codeName;
		MDIA_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static MDIA_CD getEnumByCode(String cd) {
			return Arrays.stream(MDIA_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 알림속성코드
	 */
	@Getter
	public static enum NOTI_ATRB_CD {
		필수("00", "필수"),
		금융("10", "금융"),
		이벤트혜택("20", "이벤트혜택"),			
		신용관리("30", "신용관리"),
		대출한도("40", "대출한도"),
		마케팅활용동의("50", "마케팅활용동의");
		private String code;
		private String codeName;
		NOTI_ATRB_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static NOTI_ATRB_CD getEnumByCode(String cd) {
			return Arrays.stream(NOTI_ATRB_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 알림수단코드
	 */
	@Getter
	public static enum NOTI_METHD_CD {
		  Push("10", "Push"),
		  알림톡("20", "알림톡"),
		  문자("30", "문자"),
		  이메일("40", "이메일"),
		  전화("50", "전화"),
		  DM우편물("60", "DM우편물");
		private String code;
		private String codeName;
		NOTI_METHD_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static NOTI_METHD_CD getEnumByCode(String cd) {
			return Arrays.stream(NOTI_METHD_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 공지사항유형구분코드
	 */
	@Getter
	public static enum NTC_PTRN_DV_CD {
		 업무공지("10", "업무공지"),
		 서비스공지("20", "서비스공지"),
		 시스템점검("30", "시스템점검"),
		 기타("90", "기타");
		private String code;
		private String codeName;
		NTC_PTRN_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static NTC_PTRN_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(NTC_PTRN_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * OS코드
	 */
	@Getter
	public static enum OS_CD {
		 iOS("IOS", "iOS"),
		 Android("AOS", "Android"),
		 기타("ETC", "기타"),
		 전체("ALL", "전체");
		private String code;
		private String codeName;
		OS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static OS_CD getEnumByCode(String cd) {
			return Arrays.stream(OS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 게재형식코드
	 */
	@Getter
	public static enum PBLC_FMAT_CD {
		전문연계("01","전문연계"),
		배너연계("02","배너연계"),
		브랜드검색("11","브랜드검색"),
		키워드검색("12","키워드검색"),
		배너광고("13","배너광고"),
		제휴광고("14","제휴광고"),
		영상광고("15","영상광고"),
		문자광고("16","문자광고"),
		바이럴광고("17","바이럴광고");
		private String code;
		private String codeName;
		PBLC_FMAT_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static PBLC_FMAT_CD getEnumByCode(String cd) {
			return Arrays.stream(PBLC_FMAT_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 진행상태코드
	 */
	@Getter
	public static enum PRGRS_STUS_CD {
		정상("10","정상"),
		오류("40","오류"),
		완료("99","완료");
		private String code;
		private String codeName;
		PRGRS_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static PRGRS_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(PRGRS_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 등록상태코드
	 */
	@Getter
	public static enum REGI_STUS_CD {
		  정상("10", "정상"),
		  해지("20", "해지");
		private String code;
		private String codeName;
		REGI_STUS_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static REGI_STUS_CD getEnumByCode(String cd) {
			return Arrays.stream(REGI_STUS_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 요청상세구분코드
	 */
	@Getter
	public static enum REQT_DTL_DV_CD {
		  상세정보안내("010", "상세정보안내"),
		  정보변경("020", "정보변경"),
		  오류대응("030", "오류대응"),
		  민원등록("040", "민원등록"),
		  사후처리("050", "사후처리"),
		  자료제공("060", "자료제공"),
		  기타("999", "기타");
		private String code;
		private String codeName;
		REQT_DTL_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static REQT_DTL_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(REQT_DTL_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 화면거래구분코드
	 */
	@Getter
	public static enum SCREN_TRAN_DV_CD {
		메뉴그룹("00", "메뉴 그룹"),
		화면일반("10", "화면 일반"),
		화면서브("11", "화면 서브"),
		화면모달("12", "화면 모달"),
		화면개별("13", "화면 개별"),
		화면링크("14", "화면 링크"),
		거래일반("20", "거래 일반"),
		거래엑셀("21", "거래 엑셀"),
		거래파일UP("22", "거래 파일UP"),
		거래파일DN("23", "거래 파일DN");
		private String code;
		private String codeName;
		SCREN_TRAN_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static SCREN_TRAN_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(SCREN_TRAN_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 성별구분코드
	 */
	@Getter
	public static enum SEX_DV_CD {
		  남성("M", "남성"),
		  여성("F", "여성");
		private String code;
		private String codeName;
		SEX_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static SEX_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(SEX_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 분리보관코드
	 */
	@Getter
	public static enum SST_CD {
		일차파기("10","1차파기"),
		이차파기("20","2차파기"),
		가망인증회원파기("25","가망/인증회원파기"),
		완전파기("30","완전파기"),
		일차복구("90","1차파기복구"),
		이차복구("91","2차파기복구");
		private String code;
		private String codeName;
		SST_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static SST_CD getEnumByCode(String cd) {
			return Arrays.stream(SST_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 시스템구분코드
	 */
	@Getter
	public static enum SYS_DV_CD {
		APP("APP", "APP"),
		WEB("WEB", "WEB"),
		채널연계("LNK", "채널연계"),
		ADMIN("ADM", "ADMIN"),
		솔루션("SOL", "솔루션"),
		기타("ETC", "기타");
		private String code;
		private String codeName;
		SYS_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static SYS_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(SYS_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 통신사코드
	 */
	@Getter
	public static enum TCPNY_CD {
		  SKT("10", "SKT"),
		  KT("20", "KT"),
		  LGT("30", "LGT"),
		  알뜰폰("40", "알뜰폰");
		private String code;
		private String codeName;
		TCPNY_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static TCPNY_CD getEnumByCode(String cd) {
			return Arrays.stream(TCPNY_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

	/**
	 * 사용자접근구분코드
	 */
	@Getter
	public static enum USER_ACCS_DV_CD {
		미인증("10", "미인증"),
		인증필수("20", "인증필수"),
		전자금융필수("30", "전자금융필수");
		private String code;
		private String codeName;
		USER_ACCS_DV_CD(String code, String codeName) {
			this.code = code;
			this.codeName = codeName;
		}
		public static USER_ACCS_DV_CD getEnumByCode(String cd) {
			return Arrays.stream(USER_ACCS_DV_CD.values()).filter(ce -> ce.getCode().equals(cd)).findFirst().orElse(null);
		}
	}

}
