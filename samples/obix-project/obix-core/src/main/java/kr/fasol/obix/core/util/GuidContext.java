package kr.fasol.obix.core.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * GUID 관리
 */
@Component
@Slf4j
@Getter
public class GuidContext {
    private String guid;
    private String childGuid;

    /**
     * GUID 설정
     */
    public void setGuid(String guid) {
        // GUID가 비어있는 경우에만 GUID를 저장해야 함.(기존값을 변경하면 안됨.)
        if (!StringUtils.hasLength(this.guid)) this.guid = guid;
    }

    /**
     * GUID 강제 재설정
     */
    public void setEnforceGuid(String guid) {
    	// GUID 강제 재설정
        this.guid = guid;
    }

    /**
     * ChildGuild 생성
     */
    public void createChildGuid() {
        // GUID를 사용하기 전에 처리. childGuid가 없으면 현재 guid가 childGuid. 있으면 guid의 hop을 붙여 childGuid를 생성.
        if (!StringUtils.hasLength(childGuid)) {
            childGuid = guid;
        } else if (StringUtils.hasLength(guid)) {
        	childGuid = GuidUtil.getGuidKey(guid) + GuidUtil.getGuidHop(childGuid);
        } else {
            childGuid = GuidUtil.getGuidKey(GuidUtil.createGuid()) + GuidUtil.getGuidHop(childGuid);
        }
    }

    /**
     * ChildGuild 생성
     */
    public synchronized void createNewChildGuid() {
        childGuid = GuidUtil.getGuidKey(GuidUtil.createGuid()) + GuidUtil.getGuidHop(guid);
    }

    /**
     * 시스템경로번호(NextHop) 재설정
     */
    public synchronized void setNextHop(String guid) {
        log.debug("prev guid info this.guid: {}, this.childGuid: {}, guid: {}", this.guid, this.childGuid, guid);

        // 보낸 GUID와 되돌아온 GUID의 Unique Key가 같아야 함. 그렇지 않으면 안됨.
        if (GuidUtil.getGuidKey(childGuid).equals(GuidUtil.getGuidKey(guid))) {
            this.guid = GuidUtil.nextGuid(GuidUtil.getGuidKey(this.guid) + GuidUtil.getGuidHop(guid));
            this.childGuid = GuidUtil.nextGuid(guid);
        } else {
            log.debug("Guid Process Error... childGuid: {}, guid: {}", childGuid, guid);
            this.guid = GuidUtil.nextGuid(this.guid);
            this.childGuid = GuidUtil.nextGuid(this.childGuid);
        }

        log.debug("next guid info this.guid: {}, this.childGuid: {}, guid: {}", this.guid, this.childGuid, guid);
    }
}
