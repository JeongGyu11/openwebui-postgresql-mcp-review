package kr.fasol.obix.core.util;

import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GUID 생성 규칙
 * GUID(36자리) = 일자(8) + 전문생성시간(9) + 채널코드(3) + 채널ID(7) + 일련번호(6) + 시스템경로번호(3)
 * <p>
 * 전문작성일자(8): 거래일자 (YYYYMMDD)
 * 전문작성시각(6): 거래시각 (hhmmssSSS)
 * 채널코드(3): MCI에서 구분하는 채널구분코드
 * 채널ID(7): 채널에서 서버 및 인스턴스 구분할 수 있는 값. 서버번호(5) + WAS Instance번호(2)
 * 일련번호(6)
 * 시스템경로번호(3): 증분
 */
@Slf4j
@UtilityClass
public class GuidUtil {

    private static final String DEFAULT_CHNL_CD_S3 = "OBX";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmssSSS");
    private static final AtomicInteger MESG_SEQ_NO_COUNTER = new AtomicInteger(1);
    private static final String SYS_PATH_NO_S3 = "001";

    private static final String CHANNEL_SERVER_ID = StringUtils.leftPad(StringUtils.right(System.getProperty("application.name", getHostname()), 7), 7, "0").toUpperCase();

    public String createGuid() {
        final LocalDateTime now = LocalDateTime.now();

        final StringBuilder guidBuilder = new StringBuilder();
        guidBuilder.append(now.format(DATE_FORMATTER));
        guidBuilder.append(now.format(TIME_FORMATTER));
        guidBuilder.append(DEFAULT_CHNL_CD_S3);
        guidBuilder.append(CHANNEL_SERVER_ID);
        guidBuilder.append(String.format("%06d", getAndIncrement(MESG_SEQ_NO_COUNTER) % 1000_000));
        guidBuilder.append(SYS_PATH_NO_S3); // 생성시에는 001부터
        return guidBuilder.toString();
    }

    public String nextGuid(String guid) {
        final String nextGuid = getGuidKey(guid) + StringUtils.leftPad(String.valueOf(Integer.parseInt(getGuidHop(guid)) + 1), 3, "0");
        log.debug("guid: {}, nextGuid: {}", guid, nextGuid);
        return nextGuid;
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }
        return "Unknown";
    }

	// 정수형 오버플로우 방지
    private int getAndIncrement(AtomicInteger seqnoCounter) {
        int incrementValue = 0;
        int curInt = seqnoCounter.get();
        try {
            incrementValue = seqnoCounter.getAndIncrement();
            if (incrementValue < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            incrementValue = 0;
            seqnoCounter.set(incrementValue);
        }
        return curInt;
    }

    /**
     * GUID 에서 앞의 33자리 Unique Key 추출
     *
     * @param guid
     * @return
     */
    public String getGuidKey(@Nonnull String guid) {
        return (guid.length() > 33) ? guid.substring(0, 33) : guid;
    }

    /**
     * GUID 에서 뒤의 3자리 Hop 추출
     *
     * @param guid
     * @return
     */
    public String getGuidHop(@Nonnull String guid) {
        return (guid.length() == 36) ? guid.substring(33, 36) : SYS_PATH_NO_S3;
    }
}
