package kr.fasol.obix.core.util;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.List;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > DTO 간 데이터 전달을 위한
 * 설명 :
 *
 * @Class   DTOUtil.class
 * @author  정영탁
 * @since   2017-07-31
 * @version 1.0
 *
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일        수정자                수정내용
 *--------------------------------------------------------------------
 * 2017-07-31   정영탁  최초생성
 */
@Slf4j
@UtilityClass
public class DTOUtil {
	private static final ModelMapper modelMapper;

	static {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration()
				.setSkipNullEnabled(true)
				.setMatchingStrategy(MatchingStrategies.STRICT);
	}

	/**
	 * srcObj의 각 필드의 값을 복사한 tgtClass의 새로운 인스턴스를 생성한다.
	 * tgtClass의 필드 명을 기준으로 srcObj에서 값을 복사한다.
	 */
	public static <I, O> O convertDTO(I srcObj, Class<O> tgtClass) {
		if (srcObj == null ) {
			return null;
		}
		return modelMapper.map(srcObj, tgtClass);
	}

	/**
	 * srcObj 리스트의 값을 복사한 tgtClass 타입의 List를 생성한다.
	 * tgtClass의 필드 명을 기준으로 srcObj에서 값을 복사한다.
	 */
	public static <I, O> List<O> convertDTOList(List<I> srcObjList, Class<O> tgtClass) {
		if (srcObjList == null ) {
			return null;
		}
		
		List<O> list = new ArrayList<>();
		for (I srcObj : srcObjList) {
			list.add(convertDTO(srcObj, tgtClass));
		}

		return list;
	}

	/**
	 * srcObj의 각 필드의 값을 tgtObj의 각 필드의 값으로 overwrite복사한 인스턴스를 생성한다.
	 * srcObj, tgtObj는 각 DTO의 생성된 인스턴스이다.
	 */
	public static <I, O> O copyDTO(I srcObj, O tgtObj) {
		if (srcObj == null ) {
			return null;
		}

		modelMapper.map(srcObj, tgtObj);

		return tgtObj;
	}

	/**
	 * 여러개의 srcObj의 각 필드의 값을 복사한 tgtClass의 새로운 인스턴스를 생성한다.
	 * tgtClass의 필드 명을 기준으로 srcObj에서 값을 복사한다.
	 */
	public static <I, O> O mergeDTO(List<I> srcObjList, Class<O> tgtClass) {
		if (srcObjList == null ) {
			return null;
		}

		O out = null;
		try {
			out = tgtClass.getDeclaredConstructor().newInstance();
			for (I srcObj : srcObjList) {
				modelMapper.map(srcObj, out);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return out;
	}

	/**
	 * srcObj의 각 필드의 값을 복사한 tgtClass의 새로운 인스턴스를 생성한다.
	 * tgtClass의 필드 명을 기준으로 srcObj에서 값을 복사한다.
	 */
	public static <I, O> O convertMap(I srcObj, Class<O> tgtClass) {
		if (srcObj == null ) {
			return null;
		}

        //init gson
        Gson gson = new GsonBuilder()
                .setFieldNamingStrategy(f -> CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, f.getName()))
                .create();

        O out = null;
		try {
			out = gson.fromJson(gson.toJson(srcObj), tgtClass);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return out;
	}

}
