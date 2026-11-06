package kr.fasol.obix.admin.common.tag;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.FormatUtil;
import org.apache.commons.lang3.StringUtils;

import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 업무구분명 :
 * 세부업무구분명 : 공통 > Function Tag
 * 프로그램명 : Function Tag
 * 설명 :
 *
 * @Class  FunctionUtil.class
 * @author  정영탁
 * @since   2017-08-10
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-08-10        정영탁    최초생성
 */
@Slf4j
public class FunctionUtil {
    /** 생성자 */
    public FunctionUtil() {}


    /**
     * 메뉴ID를 기준으로 선택된 메뉴의 상위 목록을 반환
     * @param menuList 전체 메뉴 목록
     * @param screnTranId 메뉴ID
     * @return
     */
    public static List<AdmScrenTranBasQryDTO> getMenuList(List<AdmScrenTranBasQryDTO> menuList, String screnTranId) {
        List<AdmScrenTranBasQryDTO> locationList = new ArrayList<>();

        AdmScrenTranBasQryDTO screnTranDTO = menuList.stream().filter(x -> StringUtils.trimToEmpty(x.getUrlAddr()).contains(screnTranId)).findFirst().orElse(null);
        if (screnTranDTO != null) {
            locationList.add((AdmScrenTranBasQryDTO) screnTranDTO.clone());
            while (screnTranDTO != null && StringUtils.isNotBlank(screnTranDTO.getUpScrenTranId())) {
                final String upScrenTranId = screnTranDTO.getUpScrenTranId();
                screnTranDTO = menuList.stream().filter(x -> x.getScrenTranId().equals(upScrenTranId)).findFirst().orElse(null);
                if (screnTranDTO != null) {
                    locationList.add((AdmScrenTranBasQryDTO) screnTranDTO.clone());
                }
            }
        }

        List<AdmScrenTranBasQryDTO> outputList = new ArrayList<>();
        if (!locationList.isEmpty()) {
            for (int i = locationList.size() - 1; i >= 0; i--) {
                outputList.add(locationList.get(i));
            }
        }
        return outputList;
    }

    /**
     * 메뉴ID를 기준으로 메뉴정보가 존재하는 지 여부
     * @param menuList 전체 메뉴 목록
     * @param screnTranId : 메뉴ID
     */
    public static boolean hasMenu(List<AdmScrenTranBasQryDTO> menuList, String screnTranId) {
		boolean isAuthUri = menuList.stream()
				.filter(x -> x.getScrenTranId().equals(screnTranId))
				.findFirst().isPresent();
		return isAuthUri;
    }

    /**
     * 메뉴ID를 기준으로 하위의 메뉴가 존재하는 지 여부
     * @param menuList 전체 메뉴 목록
     * @param screnTranId : 메뉴ID
     */
    public static boolean hasSubMenu(List<AdmScrenTranBasQryDTO> menuList, String screnTranId) {
        if (menuList == null || menuList.isEmpty() || StringUtils.isEmpty(screnTranId)) {
            return false;
        }

		boolean isAuthUri = menuList.stream()
				.filter(x -> StringUtils.trimToEmpty(x.getScrenTranId()).equals(screnTranId))
				.filter(x -> "Y".equals(x.getMenuRepstYn()))
				.findFirst().isPresent();
		return isAuthUri;
    }

    /**
     * 메뉴ID를 기준으로 메뉴정보 DTO를 반환
     * @param menuList 전체 메뉴 목록
     * @param screnTranId : 메뉴ID
     */
    public static AdmScrenTranBasQryDTO getMenuData(List<AdmScrenTranBasQryDTO> menuList, String screnTranId) {
        if (menuList == null || menuList.isEmpty() || StringUtils.isEmpty(screnTranId)) {
            return null;
        }

		return menuList.stream().filter(x -> x.getScrenTranId().equals(screnTranId)).findFirst().orElse(null);
    }

    /**
     * 메뉴ID를 기준으로 메뉴명을 반환
     * @param menuList 전체 메뉴 목록
     * @param screnTranId : 메뉴ID
     */
    public static String getMenuName(List<AdmScrenTranBasQryDTO> menuList, String screnTranId) {
        if (menuList == null || menuList.isEmpty() || StringUtils.isEmpty(screnTranId)) {
            return null;
        }

		return menuList.stream().filter(x -> x.getScrenTranId().equals(screnTranId)).findFirst().orElseGet(AdmScrenTranBasQryDTO::new).getScrenTranNm();
    }

	/**
	 * 현재시간 출력
	 */
	public static String currentDateTime(String pattern) {
		return DateUtil.getCurrentDateTime(pattern);
	}

	public static String currentDate(String pattern) {
		return DateUtil.getCurrentDate(pattern);
	}

	/**
	 * 날짜 포멧변경 출력
	 */
	public static String formatDate(Object value, String pattern) {
		String output = "";
		if (value == null) {
			output = "";
		} else if (value instanceof Date) {
			output = DateUtil.getString((Date) value, pattern);
		} else {
			output = (String) value;
			if (StringUtils.isNotBlank(output)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Date d = null;
				try {
					d = sdf.parse(output);
					sdf.applyPattern(pattern);
					output = sdf.format(d);
				} catch (ParseException e) {
					output = "";
				}
			}
		}
		return output;
	}

	/**
	 * 숫자 포멧변경 출력
	 */
	public static String formatNumber(String value, String pattern) {
		String output = value;

		if (output != null && !output.isEmpty()) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			double lAmt = Double.parseDouble(output.trim());
			output = decimalFormat.format(lAmt);
		}

		return output;
	}

	/**
	 * 계좌 번호 포맷팅 출력
	 */
	public static String formatAcno(String value, boolean isMask) {
		String output = value;

		if (StringUtils.isNotBlank(output))
			output = FormatUtil.formatAcno(output, isMask);

		return output;
	}

	/**
	 * 사업자 등록 번호 포맷팅 출력
	 */
	public static String formatBisno(String value) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatBisno(output);
		}
		return output;
	}

	/**
	 * 주민등록번호 포맷팅 출력
	 */
	public static String formatRnno(String value, boolean isMask) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatRnno(output, isMask);
		}
		return output;
	}

	/**
	 * 전화번호 포맷팅 출력
	 */
	public static String formatPhone(String value, boolean isMask) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatPhone(output, isMask);
		}
		return output;
	}

	/**
	 * 이메일 포맷팅 출력
	 */
	public static String formatEmail(String value, boolean isMask) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatEmail(output, isMask);
		}
		return output;
	}

	/**
	 * 패스워드 포맷팅 출력
	 */
	public static String formatPassword(String value) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatPassword(output);
		}
		return output;
	}

	/**
	 * 금액 포맷팅 출력
	 */
	public static String formatMoney(String value) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatMoney(output, true, 0);
		}
		return output;
	}

	/**
	 * 이자 포맷팅 출력
	 */
	public static String formatInterest(String value) {
		String output = value;
		if (StringUtils.isNotBlank(output)) {
			output = FormatUtil.formatInterest(output);
		}
		return output;
	}

}
