package kr.fasol.obix.core.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > DTO > 페이지정보 DTO 인터페이스
 * 프로그램명 :
 * 설명 :
 *
 * 클래스 : IPageInfo.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 7. 9.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 7. 9.         정영탁          최초생성
 */
public interface IPageInfo {

	/** 페이지 번호 */
	int getPageNo();

	/** 페이지 번호 */
	void setPageNo(int pageNo);

	/** 전체 데이터 건수 */
	int getTotalCount();

	/** 전체 데이터 건수 */
	void setTotalCount(int totalCount);

	/** 페이지당 출력할 항목 수 */
	int getPageSize();

	/** 페이지당 출력할 항목 수 */
	void setPageSize(int pageSize);

	/** 페이지 네비게이션에 출력할 페이지 수 */
	int getPageBlock();

	/** 페이지 네비게이션에 출력할 페이지 수 */
	void setPageBlock(int pageBlock);

	/** ROW_NUM */
	int getRowNum();

	/** ROW_NUM */
	void setRowNum(int rowNum);

	@JsonIgnore
	int getPageNoIndex();

	/**
	 * 총 페이지 수
	 */
	@JsonIgnore
	int getTotalPage();

	/**
	 * 페이징 출력 시작번호
	 */
	@JsonIgnore
	int getStartPage();

	/**
	 * 페이징 출력 끝번호
	 */
	@JsonIgnore
	int getEndPage();

	/** 페이징에 출력할 페이지 번호 */
	@JsonIgnore
	List<Integer> getPages();

	/**
	 * 처음 링크의 페이지 번호
	 */
	@JsonIgnore
	int getFirstPage();

	/**
	 * 이전 링크의 페이지 번호
	 */
	@JsonIgnore
	int getPrevPage();

	/**
	 * 다음 링크의 페이지 번호
	 */
	@JsonIgnore
	int getNextPage();

	/**
	 * 마지막 링크의 페이지 번호
	 */
	@JsonIgnore
	int getLastPage();

}
