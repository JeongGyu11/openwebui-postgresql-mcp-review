package kr.fasol.obix.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.EnvironmentUtil;
import kr.fasol.obix.core.util.ImageUtil;
import kr.fasol.obix.core.util.ObixSecurityUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import kr.fasol.obix.common.enums.FileConstant;
import kr.fasol.obix.core.exception.UserException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > 파일 다운로드 유틸
 * 프로그램명 : 파일 다운로드 유틸
 * 설명 :
 *
 * 클래스 : FileDownloadUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 11.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 11.         정영탁          최초생성
 */
@Slf4j
@UtilityClass
public class FileDownloadUtil {

	/**
	 * 파일 다운로드
	 * @param filePath 서버의 파일 경로
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Resource> download(@NonNull String filePath, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		Assert.notNull(filePath, "`filePath` is required");
		String filename = filePath.substring(filePath.lastIndexOf('/')+1);
		return download(filePath, filename, storgDirRoot.isCryptYn());
	}

	/**
	 * 파일 다운로드
	 * @param filePath 서버의 파일 경로
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Resource> download(@NonNull String filePath) throws Exception {
		Assert.notNull(filePath, "`filePath` is required");
		String filename = filePath.substring(filePath.lastIndexOf('/')+1);
		return download(filePath, filename);
	}

	/**
	 * 파일 다운로드
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Resource> download(@NonNull String filePath, @NonNull String filename, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return download(filePath, filename, storgDirRoot.isCryptYn());
	}

	/**
	 * 파일 다운로드
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Resource> download(@NonNull String filePath, @NonNull String filename) throws Exception {
		return download(filePath, filename, false);
	}

	/**
	 * 파일 다운로드
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @param cryptYn 암호화여부
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Resource> download(@NonNull String filePath, @NonNull String filename, boolean cryptYn) throws Exception {
		byte[] fileBytes = readFile(filePath, filename, cryptYn);

		Resource resource = new ByteArrayResource(fileBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename, StandardCharsets.UTF_8).build());
		headers.setContentLength(resource.contentLength());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}

	/**
	 * 이미지파일 썸네일 다운로드
	 * @param filePath 서버의 파일 경로
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param width 생성할 이미지의 Width
	 * @param height 생성할 이미지의 Height
	 * @param quality 생성할 이미지의 Quality(최대 100)
	 * @return ResponseEntity
	 */
	public static ResponseEntity<Resource> downloadThumbnail(@NonNull String filePath, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, int width, int height, int quality) throws Exception {
		Assert.notNull(filePath, "`filePath` is required");
		String filename = filePath.substring(filePath.lastIndexOf('/')+1);

		byte[] fileBytes = readFileThumbnail(filePath, width, height, quality, storgDirRoot.isCryptYn());

		Resource resource = new ByteArrayResource(fileBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename, StandardCharsets.UTF_8).build());
		headers.setContentLength(resource.contentLength());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}

	/**
	 * 파일 다운로드 가능여부 확인
	 * @param filePath 서버의 파일 경로
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @return ResponseEntity
	 */
	public static boolean isDownload(@NonNull String filePath, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		Assert.notNull(filePath, "`filePath` is required");
		String filename = filePath.substring(filePath.lastIndexOf('/')+1);
		return isDownload(filePath, filename, storgDirRoot.isCryptYn());
	}

	/**
	 * 파일 다운로드  가능여부 확인
	 * @param filePath 서버의 파일 경로
	 * @return ResponseEntity
	 */
	public static boolean isDownload(@NonNull String filePath) throws Exception {
		Assert.notNull(filePath, "`filePath` is required");
		String filename = filePath.substring(filePath.lastIndexOf('/')+1);
		return isDownload(filePath, filename);
	}

	/**
	 * 파일 다운로드  가능여부 확인
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @return ResponseEntity
	 */
	public static boolean isDownload(@NonNull String filePath, @NonNull String filename, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return isDownload(filePath, filename, storgDirRoot.isCryptYn());
	}

	/**
	 * 파일 다운로드  가능여부 확인
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @return ResponseEntity
	 */
	public static boolean isDownload(@NonNull String filePath, @NonNull String filename) throws Exception {
		return isDownload(filePath, filename, false);
	}

	/**
	 * 파일 다운로드  가능여부 확인
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @param cryptYn 암호화여부
	 * @return ResponseEntity
	 */
	public static boolean isDownload(@NonNull String filePath, @NonNull String filename, boolean cryptYn) throws Exception {
		File file = new File(filePath);
		return (file.exists() && file.isFile());
	}

	/**
	 * 파일 읽기
	 * @param filePath 서버의 파일 경로
	 */
	public static byte[] readFile(@NonNull String filePath) throws Exception {
		return readFile(filePath, "", false);
	}
	/**
	 * 파일 읽기
	 * @param filePath 서버의 파일 경로
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 */
	public static byte[] readFile(@NonNull String filePath, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return readFile(filePath, "", storgDirRoot.isCryptYn());
	}
	/**
	 * 파일 읽기
	 * @param filePath 서버의 파일 경로
	 * @param filename 브라우저로 다운로드 될 파일명
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 */
	public static byte[] readFile(@NonNull String filePath, @NonNull String filename, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return readFile(filePath, filename, storgDirRoot.isCryptYn());
	}
	/**
	 * 파일 읽기
	 * @param filePath 서버의 파일 경로
	 * @param cryptYn 암호화여부
	 */
	public static byte[] readFile(@NonNull String filePath, @NonNull String filename, boolean cryptYn) throws Exception {
		InputStream inputStream = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException(filePath);
			}

			Resource resource = new UrlResource(file.toURI());

			// 파일 복호화 처리
			if (cryptYn) {
				try {
					inputStream = resource.getInputStream();
					byte[] srcBytes = IOUtils.toByteArray(inputStream);
					byte[] decBytes = ObixSecurityUtil.decryptAES(srcBytes);
					resource = new ByteArrayResource(decBytes);
				} catch (IOException | DecoderException | NoSuchAlgorithmException | NoSuchPaddingException |
                         InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
                         BadPaddingException e) {
					log.error("### 파일 복호화 실패 = " + filePath);
					log.error(e.getMessage());
				} catch (Exception e) {
					log.error("### 파일 복호화 실패 = " + filePath);
					log.error(e.getMessage());
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
			inputStream = resource.getInputStream();

			if (log.isDebugEnabled()) {
				log.debug("filePath = {}", resource.getFilename());
				log.debug("fileSize = {}", resource.contentLength());
			}

			return IOUtils.toByteArray(resource.getInputStream());
		}
		catch (FileNotFoundException e) {
			throw new UserException("CB1007").setDebuggingMessage("[FileNotFoundException][" + filePath + "]");
		}
		catch (Exception e) {
			throw new UserException("CB1007", e).setDebuggingMessage("[File Download Exception][" + filePath + "]");
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * 파일 읽기
	 * @param filePath 서버의 파일 경로
	 * @param cryptYn 암호화여부
	 */
	public static byte[] readFileThumbnail(@NonNull String filePath, int width, int height, int quality, boolean cryptYn) throws Exception {
		InputStream inputStream = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException(filePath);
			}

			Resource resource = new UrlResource(file.toURI());

			// 파일 복호화 처리
			if (cryptYn) {
				try {
					inputStream = resource.getInputStream();
					byte[] srcBytes = IOUtils.toByteArray(resource.getInputStream());
					byte[] decBytes = ObixSecurityUtil.decryptAES(srcBytes);
					resource = new ByteArrayResource(decBytes);
				} catch (IOException | DecoderException | NoSuchAlgorithmException | NoSuchPaddingException |
                         InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException |
                         BadPaddingException e) {
					log.error("### 파일 복호화 실패 = " + filePath);
					log.error(e.getMessage());
				} catch (Exception e) {
					log.error("### 파일 복호화 실패 = " + filePath);
					log.error(e.getMessage());
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
			inputStream = resource.getInputStream();

			if (log.isDebugEnabled()) {
				log.debug("filePath = {}", resource.getFilename());
				log.debug("fileSize = {}", resource.contentLength());
			}

			// 파일을 썸네일 이미지로 변환
			return ImageUtil.saveToThumbnail(resource.getInputStream(), width, height, quality);
		}
		catch (FileNotFoundException e) {
			throw new UserException("CB1007").setDebuggingMessage("[FileNotFoundException][" + filePath + "]");
		}
		catch (Exception e) {
			throw new UserException("CB1007", e).setDebuggingMessage("[File Download Exception][" + filePath + "]");
		}
		finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * 서버에 저장된 파일 목록 조회
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 */
	public static List<File> listFile(FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return listFile(storgDirRoot, "");
	}

	/**
	 * 서버에 저장된 파일 목록 조회
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param umsSubDirectory 파일저장공간위치 뒤에 추가로 생성된 디렉토리(FileConstant.UMS_SUB_DIRECTORY)
	 */
	public static List<File> listFile(FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY umsSubDirectory) throws Exception {
		return listFile(storgDirRoot, umsSubDirectory.getDirPath());
	}
	/**
	 * 서버에 저장된 파일 목록 조회
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성된 디렉토리
	 */
	public static List<File> listFile(FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir) throws Exception {
		List<File> fileList = new ArrayList<>();

		String newFilePath = getNewFilePath(storgDirRoot, postfixDir);

		fileList.addAll(getListFile(new File(newFilePath)));

		return fileList;
	}

	private static List<File> getListFile(File file) throws Exception {
		List<File> list = new ArrayList<>();

		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						list.addAll(getListFile(files[i]));
					} else {
						list.add(files[i]);
					}
				}
			}
		}

		return list;
	}

	/**
	 * 파일을 저장할 전체 경로를 생성하여 리턴한다.
	 */
	private static String getNewFilePath(FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir) {
		String newFilePath = EnvironmentUtil.getProperty("STORG_ROOT_PATH", "") + storgDirRoot.getRootPath();
		if (StringUtils.isNotBlank(storgDirRoot.getPostfixPattern())) {
			newFilePath += DateUtil.getCurrentDate(storgDirRoot.getPostfixPattern());
		}

		if (StringUtils.isNotBlank(postfixDir)) {
			newFilePath += postfixDir;
		}

		if (!newFilePath.endsWith("/")) {
			newFilePath += "/";
		}

		return newFilePath;
	}

}
