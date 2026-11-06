package kr.fasol.obix.core.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 페이징 정보 구현체
 */
@Data
public class PageInfoImpl implements IPageInfo, Serializable {
    @Serial
    private static final long serialVersionUID = 6626382060530239164L;

    /*
     * 페이징 처리
     */
    private int pageNo = 1;		    // 페이지 번호
    private int totalCount;			// 전체 데이터 수
    private int pageSize = 10;		// 페이지 당 출력할 항목 수
    private int pageBlock = 10;		// 페이지바에 출력할 페이지 수
    private int rowNum = 0;         // 게시물의 순번

    /**
     * 쿼리 페이징을 위한 페이번호 설정
     */
    @JsonIgnore
    public int getPageNoIndex() {
        return (pageSize * (pageNo - 1));
    }

    /**
     * 총 페이지 수
     */
    @JsonIgnore
    public int getTotalPage() {
        return ((totalCount - 1) / pageSize) + 1;
    }

    /**
     * 페이징 출력 시작번호
     */
    @JsonIgnore
    public int getStartPage() {
        return ((pageNo - 1) / pageBlock) * pageBlock + 1;
    }

    /**
     * 페이징 출력 끝번호
     */
    @JsonIgnore
    public int getEndPage() {
        return ((getStartPage() + (pageBlock - 1)) < getTotalPage() ? getStartPage() + (pageBlock - 1) : getTotalPage());
    }

    /** 페이징에 출력할 페이지 번호 */
    @JsonIgnore
    public List<Integer> getPages() {
        List<Integer> list = new ArrayList<>();
        for (int i = getStartPage(); i <= getEndPage(); i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * 처음 링크의 페이지 번호
     */
    @JsonIgnore
    public int getFirstPage() {
        return 1;
    }
    /**
     * 이전 링크의 페이지 번호
     */
    @JsonIgnore
    public int getPrevPage() {
        if (getStartPage() > 1) {
            return getStartPage() - 1;
        } else {
            return 1;
        }
    }
    /**
     * 다음 링크의 페이지 번호
     */
    @JsonIgnore
    public int getNextPage() {
        if (getEndPage() < getTotalPage()) {
            return getEndPage() + 1;
        } else {
            return getTotalPage();
        }
    }
    /**
     * 마지막 링크의 페이지 번호
     */
    @JsonIgnore
    public int getLastPage() {
        return getTotalPage();
    }

}

