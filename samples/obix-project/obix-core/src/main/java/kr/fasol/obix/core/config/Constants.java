package kr.fasol.obix.core.config;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Set;

/**
 * F/W 필수 상수값 정의
 */
public class Constants {

	/*
	 * 서버 실행 환경
	 */
    public static final String ENV_PRODUCTION = "production";
    public static final String ENV_DR = "dr";
    public static final String ENV_TEST = "test";
    public static final String ENV_DEV = "dev";
    public static final String ENV_LOCAL = "local";

    /*
     * 서버 정보
     */
    public static final String SVR_IPAD;
    public static final String SVR_HOST;
    public static final int SVR_MACHINE_ID;

    /**
     * 서비스 URI 속성
     */
    @Getter
	public static enum CONST_SERVICE_TYPE {
		VIEW("view"), FRAG("frag"), JSON("json"), XML("xml"), CMD("cmd"),
		INDV("indv"), FILE("file"), EXCEL("excl"), BLNK("blnk"),
		EMPTY("");
        String type;
        CONST_SERVICE_TYPE(String type) {
            this.type = type;
        }
        public static CONST_SERVICE_TYPE getTypeByCode(String serviceType) {
            return Arrays.stream(CONST_SERVICE_TYPE.values()).filter(x -> x.getType().equals(serviceType)).findFirst().orElse(CONST_SERVICE_TYPE.EMPTY);
        }
    }
    public static final Set<CONST_SERVICE_TYPE> NOTVIEW_SERVICE_TYPE = ImmutableSet.of(CONST_SERVICE_TYPE.FRAG, CONST_SERVICE_TYPE.JSON, CONST_SERVICE_TYPE.XML, CONST_SERVICE_TYPE.CMD, CONST_SERVICE_TYPE.INDV, CONST_SERVICE_TYPE.FILE, CONST_SERVICE_TYPE.EXCEL);
    public static final Set<CONST_SERVICE_TYPE> VIEW_SERVICE_TYPE = ImmutableSet.of(CONST_SERVICE_TYPE.VIEW, CONST_SERVICE_TYPE.FRAG, CONST_SERVICE_TYPE.INDV);

    static {
        String svrIpad = null;
        String svrHost = null;
        int svrMachineId = 0;
        try {
            svrIpad = InetAddress.getLocalHost().getHostAddress();
            svrHost = InetAddress.getLocalHost().getHostName();

            // 머신아이디(노드)는 호스트이름에 있는 숫자값을 이용함.
            if (svrHost.matches(".*[0-9].*")) {
                try {
                	svrMachineId = Integer.parseInt(svrHost.replaceAll("[^\\d]", ""));
                } catch (NumberFormatException e) {
                	svrMachineId = 0;
                }
            }
        } catch (Exception e) {
            svrIpad = StringUtils.EMPTY;
            svrHost = StringUtils.EMPTY;
            svrMachineId = 0;
        } finally {
            SVR_IPAD = svrIpad;
            SVR_HOST = svrHost;
            SVR_MACHINE_ID = svrMachineId;
        }
    }

}
