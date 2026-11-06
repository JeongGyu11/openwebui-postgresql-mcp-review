package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > 일련번호 채번
 * 프로그램명 : 일련번호 채번
 * 설명 :
 *
 * @Class   IdGenerateUtil.class
 * @author  정영탁
 * @since   2017-08-08
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일        수정자                수정내용
 *--------------------------------------------------------------------
 * 2017-08-08   정영탁  최초생성
 * 
 */
@Slf4j
@UtilityClass
public class IdGeneratorUtil  {
    private static final long MAX_FILL_BITS = 0xffffffffffffffffL;
    /**
     * 2011-11-11 00:00:00.000 기준으로 시작.
     */
    private static final int TIMESTAMP_BIT_LENGTH = 41;
    /**
     * 앱 서버별 고유 아이디 : 0~4095
     */
    private static final int NODE_ID_BIT_LENGTH = 12;
    /**
     * MACHINE 별 랜덤 번호를 sequence의 시작값으로 한다. sequence는 0~2047까지의 값으로 cycle value.
     */
    private static final int SEQUENCE_BIT_LENGTH = 11;
    private static final int SUFFIX_BIT_LENGTH = 64 - TIMESTAMP_BIT_LENGTH;
    /**
     * 2021-01-01 00:00:00.000
     */
    private static final long BASE_TIMESTAMP = new Date(1609426800000L).getTime();
    // 111111111111111111111111111111111111111100000000000000000000000
    private static final long TIMESTAMP_BITMAP = MAX_FILL_BITS << (NODE_ID_BIT_LENGTH + SEQUENCE_BIT_LENGTH);
    // 11111111111
    private static final long SEQUENCE_BITMAP = ~(MAX_FILL_BITS << SEQUENCE_BIT_LENGTH);
    // 11111111111100000000000
    private static final long NODE_ID_BITMAP = ~(MAX_FILL_BITS << (NODE_ID_BIT_LENGTH + SEQUENCE_BIT_LENGTH) | SEQUENCE_BITMAP);
    private final AtomicInteger _sequence;
    private final Integer startRange;
    private final Integer endRange;
    private long _nodeId;
    private void setNodeId(int nodeId) {
        final int newNodeId;
        if (endRange != 0) {
            int resolution = endRange - startRange;
            if (resolution < 0)
            {
                //throw new IdentifierGenerationException("invalid IdGeneratorUtil node range");
                throw new IllegalArgumentException("invalid IdGeneratorUtil node range");
            }
            newNodeId = (nodeId % resolution) + startRange;
        } else {
            newNodeId = nodeId;
        }

        _nodeId = (newNodeId << SEQUENCE_BIT_LENGTH) & NODE_ID_BITMAP;
        log.info("generated machineId for IdGenerator: {} ({}:{})", newNodeId & 0xfffL, startRange, endRange);
    }
    public long generate() {
        final long timestamp = System.currentTimeMillis() - BASE_TIMESTAMP;
        final int seq = incrementAndGet(_sequence);
        return ((timestamp << SUFFIX_BIT_LENGTH) & TIMESTAMP_BITMAP) | _nodeId | (seq & SEQUENCE_BITMAP);
    }
    
	// 정수형 오버플로우 방지
    private int incrementAndGet(AtomicInteger seqnoCounter) {
        int incrementValue = 0;
        try {
            incrementValue = seqnoCounter.incrementAndGet();
            if (incrementValue < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            incrementValue = 0;
            seqnoCounter.set(incrementValue);
        }
        return incrementValue;
    }
    

    public static Date getDateFromLogId(long logId) {
        return new Date((logId >> SUFFIX_BIT_LENGTH) + BASE_TIMESTAMP);
    }
    public static long getTimestampFromLogId(long logId) {
        return (logId >> SUFFIX_BIT_LENGTH) + BASE_TIMESTAMP;
    }
    public static long getTimeshiftedLogId(long logId, int delta) {
        return IdGeneratorUtil.getLogIdFromTimeStamp(IdGeneratorUtil.getDateFromLogId(logId).getTime() + delta);
    }
    public static long getLogIdFromTimeStamp(long timeStamp) {
        return ((timeStamp - BASE_TIMESTAMP) << SUFFIX_BIT_LENGTH) & TIMESTAMP_BITMAP;
    }
    public static long getLogIdFromDate(int year, int month, int day, int hour, int minute, int second) {
        return getLogIdFromTimeStamp(
                new GregorianCalendar(year, month - 1, day, hour, minute, second).getTimeInMillis());
    }
    static {
        _sequence = new AtomicInteger();
        startRange = 0;
        endRange = 4096;
        IdGeneratorUtil.initialize();
    }
    private static void initialize() {
        // hostname 을 가져온다. (node 별로 생성하는 Id가 uniq하게 하기 위함.)
        String hostname = "Unknown";
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        }
        // 머신아이디(노드)는 호스트이름에 있는 숫자값을 이용함. 숫자값이 없으면 0을 사용. (대부분 있어야 함. 없으면 망...)
        int machineId = 0;
        if (hostname.matches(".*[0-9].*")) {
            try {
                machineId = Integer.parseInt(hostname.replaceAll("[^\\d]", ""));
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }
        }
        // 머신아이디가 0인 경우는 문제가 있는 것이니 모니터링 하자.
        if (machineId == 0) {
            log.warn("Initialize IdGenerateUtil... Hostname : {}, MachineId: {}", hostname, machineId);
        }
        setNodeId(machineId);
        log.debug("Initialize IdGenerateUtil... Hostname : {}, MachineId: {}, _nodeId: {}", hostname, machineId, _nodeId);
    }
}
