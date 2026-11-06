package kr.fasol.obix.admin.common.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.fasol.obix.admin.dto.biz.cm.AdmScrenTranBasQryDTO;
import org.apache.commons.lang3.StringUtils;

import kr.fasol.obix.common.enums.SessionEnum;
import kr.fasol.obix.common.util.SessionUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 시스템명 : 관리자
 * 업무구분명 : 공통 > 세션 관리
 * 프로그램명 : 로그인 세션 관리
 * 설명 :
 *
 * @Class  AdminSessionHelper.class
 * @author  정영탁
 * @since   2017-07-28
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2017-07-28        정영탁    최초생성
 */
@UtilityClass
public class AdminSessionHelper {

    /**
     * 로그인 여부 판별
     */
    public static boolean isLogin() {
        AdminSessionCommon adminSessionCommon = get();
        return (adminSessionCommon != null
        		&& adminSessionCommon.getAdmUserBasDTO() != null
                && adminSessionCommon.getAdmUserBasDTO().getUserId() != null)
                ;
    }

    /**
     * 세션에 저장된 객체 조회
     */
    public static AdminSessionCommon get() {
    	return SessionUtil.get(SessionEnum.ADMIN_SESSION, AdminSessionCommon.class);
    }

    /**
     * 세션에 객체 저장
     */
    public static void put(AdminSessionCommon adminSessionCommon) {
    	SessionUtil.put(SessionEnum.ADMIN_SESSION, adminSessionCommon);
    }

    /**
     * 세션에서 객체 삭제
     */
    public static void destroy() {
    	SessionUtil.remove(SessionEnum.ADMIN_SESSION);
    }

    /**
     * 어드민 사용자ID 조회
     */
    public static String getAdminUserId() {
        AdminSessionCommon adminSessionCommon = get();
        if (adminSessionCommon != null && adminSessionCommon.getAdmUserBasDTO() != null) {
        	return StringUtils.trimToEmpty(adminSessionCommon.getAdmUserBasDTO().getUserId());
        }
    	return StringUtils.EMPTY;
    }

    /**
     * 어드민 사용자명 조회
     */
    public static String getAdminUserNm() {
        AdminSessionCommon adminSessionCommon = get();
        if (adminSessionCommon != null && adminSessionCommon.getAdmUserBasDTO() != null) {
            return StringUtils.trimToEmpty(adminSessionCommon.getAdmUserBasDTO().getUserNm());
        }
        return StringUtils.EMPTY;
    }

    /**
     * 사용자 메뉴 리스트 (권한)
     */
    public static List<AdmScrenTranBasQryDTO> getAdmScrenMenuList() {
        AdminSessionCommon adminSessionCommon = get();
        if (adminSessionCommon != null && adminSessionCommon.getAdmScrenMenuList() != null) {
            return adminSessionCommon.getAdmScrenMenuList();
        }
        return null;
    }

    /**
     * 사용자 메뉴 이력
     */
    public static List<AdmScrenTranBasQryDTO> getUsrMenuHisList() throws Exception {
        AdminSessionCommon adminSessionCommon = get();
        if (adminSessionCommon != null && adminSessionCommon.getUsrMenuHisList() != null) {
            String jsonStr = adminSessionCommon.getUsrMenuHisList();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            List<AdmScrenTranBasQryDTO> list = objectMapper.readValue(jsonStr, new TypeReference<List<AdmScrenTranBasQryDTO>>() {});
            return list;
        }
        return null;
    }

    /**
     * 사용자 메뉴 (화면표시)
     */
    public static List<AdmScrenTranBasQryDTO> getUserMenu() throws Exception {
        AdminSessionCommon adminSessionCommon = get();
        if (adminSessionCommon != null && adminSessionCommon.getAdmScrenMenuList() != null) {
            String jsonStr = adminSessionCommon.getUserMenuList();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            List<AdmScrenTranBasQryDTO> list = objectMapper.readValue(jsonStr, new TypeReference<List<AdmScrenTranBasQryDTO>>() {});
            return list;
        }
        return null;
    }

}
