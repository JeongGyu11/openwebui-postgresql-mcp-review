package kr.fasol.obix.core.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO에서 상속받는 페이징정보 DTO
 */
@Getter
@Setter
public abstract class AbstractPageInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -397971770922058869L;

    protected PageInfoImpl pageInfo;	// 페이징정보

    /**
     * 게시물의 순번을 설정한다.
     */
    public void setRowNum(int rowNum) {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        this.pageInfo.setRowNum(rowNum);
    }
    /**
     * 페이지번호를 설정한다.
     */
    public void setPageNo(int pageNo) {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        this.pageInfo.setPageNo(pageNo);
    }
    /**
     * 페이지조회건수를 설정한다.
     */
    public void setPageSize(int pageSize) {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        this.pageInfo.setPageSize(pageSize);
    }
    /**
     * TOTAL_COUNT를 설정한다.
     */
    public void setTotalCount(int totalCount) {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        this.pageInfo.setTotalCount(totalCount);
    }

    /**
     * 게시물의 순번을 반환한다.
     */
    @JsonIgnore
    public int getRowNum() {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        return this.pageInfo.getRowNum();
    }
    /**
     * 페이지번호를 반환한다.
     */
    @JsonIgnore
    public int getPageNo() {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        return this.pageInfo.getPageNo();
    }
    /**
     * 페이지조회건수를 반환한다.
     */
    @JsonIgnore
    public int getPageSize() {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        return this.pageInfo.getPageSize();
    }
    /**
     * TOTAL_COUNT를 반환한다.
     */
    @JsonIgnore
    public int getTotalCount() {
        if (this.pageInfo == null) {
            this.pageInfo = new PageInfoImpl();
        }
        return this.pageInfo.getTotalCount();
    }

}
