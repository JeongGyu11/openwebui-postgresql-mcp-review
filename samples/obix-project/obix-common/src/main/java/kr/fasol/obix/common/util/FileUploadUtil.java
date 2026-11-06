package kr.fasol.obix.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import kr.fasol.obix.core.util.DateUtil;
import kr.fasol.obix.core.util.EnvironmentUtil;
import kr.fasol.obix.core.util.ObixSecurityUtil;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import kr.fasol.obix.common.enums.FileConstant;
import kr.fasol.obix.core.exception.UserException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > 파일 업로드 유틸
 * 프로그램명 : 파일 업로드 유틸
 * 설명 :
 *
 * 클래스 : FileUploadUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 8. 26.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 8. 26.         정영탁          최초생성
 */
@Slf4j
@UtilityClass
public class FileUploadUtil {

	private static final char[] ALPHAS = new char[] { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private static Random GENERATOR = new Random(System.currentTimeMillis());

	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 */
	public static String upload(MultipartFile file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return upload(file, storgDirRoot, "", null);
	}
	/**
	 * 업무분류별 디렉토리에 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param subDirectory 파일저장공간위치 뒤에 추가로 생성할 디렉토리(FileConstant.SUB_DIRECTORY)
	 */
	public static String upload(MultipartFile file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY subDirectory) throws Exception {
		return upload(file, storgDirRoot, subDirectory.getDirPath(), null);
	}
	/**
	 * 업무분류별 디렉토리에 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param subDirectory 파일저장공간위치 뒤에 추가로 생성할 디렉토리(FileConstant.SUB_DIRECTORY)
	 * @param savedFileName 저장할 파일명(강제지정)
	 */
	public static String upload(MultipartFile file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY subDirectory, String savedFileName) throws Exception {
		return upload(file, storgDirRoot, subDirectory.getDirPath(), savedFileName);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성할 디렉토리
	 * @param savedFileName 저장할 파일명(강제지정)
	 */
	public static String upload(MultipartFile file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir, String savedFileName) throws Exception {
		return upload(file, storgDirRoot, postfixDir, savedFileName, false);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성할 디렉토리
	 * @param savedFileName 저장할 파일명(강제지정)
	 * @param overwriteYn 덮어쓰기 여부
	 */
	public static String upload(MultipartFile file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir, String savedFileName, boolean overwriteYn) throws Exception {
		if (file == null || file.isEmpty()) {
			throw new UserException("BIZ.FILE.01").setDebuggingMessage("전달할 파일이 존재하지 않습니다.");
		}

		// 유효한 확장자인지 체크
		checkFileExt(storgDirRoot, file.getOriginalFilename());

		String newFilePath = getNewFilePath(storgDirRoot, postfixDir);

		String newFileName;
		if (StringUtils.isNotBlank(savedFileName)) {
			newFileName = checkFileName(newFilePath + savedFileName, overwriteYn);
		} else {
			newFileName = generateFileName(newFilePath, file.getOriginalFilename());
		}

		return upload(file.getInputStream(), newFileName, storgDirRoot.isCryptYn(), overwriteYn);
	}

	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 */
	public static String upload(File file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return upload(file, storgDirRoot, "", null);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param savedFileName 저장할 파일명(강제지정)
	 */
	public static String upload(File file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String savedFileName) throws Exception {
		return upload(file, storgDirRoot, "", savedFileName);
	}
	/**
	 * 업무분류별 디렉토리에 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param subDirectory 파일저장공간위치 뒤에 추가로 생성할 디렉토리(FileConstant.SUB_DIRECTORY)
	 */
	public static String upload(File file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY subDirectory) throws Exception {
		return upload(file, storgDirRoot, subDirectory.getDirPath(), null);
	}
	/**
	 * 업무분류별 디렉토리에 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param subDirectory 파일저장공간위치 뒤에 추가로 생성할 디렉토리(FileConstant.SUB_DIRECTORY)
	 * @param savedFileName 저장할 파일명(강제지정)
	 */
	public static String upload(File file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY subDirectory, String savedFileName) throws Exception {
		return upload(file, storgDirRoot, subDirectory.getDirPath(), savedFileName);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성할 디렉토리
	 * @param savedFileName 저장할 파일명(강제지정)
	 */
	public static String upload(File file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir, String savedFileName) throws Exception {
		return upload(file, storgDirRoot, postfixDir, savedFileName, false);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성할 디렉토리
	 * @param savedFileName 저장할 파일명(강제지정)
	 * @param overwriteYn 덮어쓰기 여부
	 */
	public static String upload(File file, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir, String savedFileName, boolean overwriteYn) throws Exception {
		if (file == null || !file.exists()) {
			throw new UserException("BIZ.FILE.01").setDebuggingMessage("전달할 파일이 존재하지 않습니다.");
		}

		// 유효한 확장자인지 체크
		checkFileExt(storgDirRoot, file.getName());

		String newFilePath = getNewFilePath(storgDirRoot, postfixDir);

		String newFileName;
		if (StringUtils.isNotBlank(savedFileName)) {
			newFileName = checkFileName(newFilePath + savedFileName, overwriteYn);
		} else {
			newFileName = generateFileName(newFilePath, file.getName());
		}

		InputStream inputStream = new FileInputStream(file);
		try {
			return upload(inputStream, newFileName, storgDirRoot.isCryptYn(), overwriteYn);
		} catch (Exception e) {
			throw e;
		} finally {
			inputStream.close();
		}
	}

	/**
	 * 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot) throws Exception {
		return upload(inputStream, storgDirRoot, "", null, null);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param savedFileName 저장할 파일명
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String savedFileName) throws Exception {
		return upload(inputStream, storgDirRoot, "", savedFileName, null);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param savedFileName 저장할 파일명
	 * @param extension 파일확장자
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String savedFileName, String extension) throws Exception {
		return upload(inputStream, storgDirRoot, "", savedFileName, extension);
	}
	/**
	 * 업무분류별 디렉토리에 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param subDirectory 파일저장공간위치 뒤에 추가로 생성할 디렉토리(FileConstant.SUB_DIRECTORY)
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY subDirectory) throws Exception {
		return upload(inputStream, storgDirRoot, subDirectory.getDirPath(), null, null);
	}
	/**
	 * 업무분류별 디렉토리에 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param subDirectory 파일저장공간위치 뒤에 추가로 생성할 디렉토리(FileConstant.SUB_DIRECTORY)
	 * @param savedFileName 저장할 파일명(강제지정)
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, FileConstant.SUB_DIRECTORY subDirectory, String savedFileName) throws Exception {
		return upload(inputStream, storgDirRoot, subDirectory.getDirPath(), savedFileName, null);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성할 디렉토리
	 * @param savedFileName 저장할 파일명
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir, String savedFileName, String extension) throws Exception {
		return upload(inputStream, storgDirRoot, postfixDir, savedFileName, extension, false);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param inputStream 저장할 파일
	 * @param storgDirRoot 파일 저장공간 위치 FileConstant.STORG_DIRECTORY_ROOT
	 * @param postfixDir 파일저장공간위치 뒤에 추가로 생성할 디렉토리
	 * @param savedFileName 저장할 파일명
	 * @param overwriteYn 덮어쓰기 여부
	 */
	public static String upload(InputStream inputStream, FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String postfixDir, String savedFileName, String extension, boolean overwriteYn) throws Exception {
		if (inputStream == null) {
			throw new UserException("BIZ.FILE.01").setDebuggingMessage("전달할 파일이 존재하지 않습니다.");
		}

		String newFilePath = getNewFilePath(storgDirRoot, postfixDir);

		String newFileName;
		if (StringUtils.isNotBlank(savedFileName)) {
			newFileName = checkFileName(newFilePath + savedFileName, overwriteYn);
		} else {
			newFileName = generateFileName(newFilePath, (StringUtils.isNotBlank(extension) ? "filename.".concat(extension) : savedFileName) );
		}

		return upload(inputStream, newFileName, storgDirRoot.isCryptYn(), overwriteYn);
	}
	/**
	 * 파일 업로드(서버에 저장)
	 * @param file 저장할 파일
	 * @param filePath 저장할 파일경로
	 * @param cryptYn 암호화여부
	 * @param overwriteYn 덮어쓰기 여부
	 */
	private static String upload(InputStream file, String filePath, boolean cryptYn, boolean overwriteYn) throws Exception {
		if (file == null) {
			throw new UserException("BIZ.FILE.01").setDebuggingMessage("전달할 파일이 존재하지 않습니다.");
		}

		// 저장경로 존재여부 검사, 폴더가 존재하지 않으면 생성
		File savePath = new File(filePath);
		if (savePath.getParentFile() != null && !savePath.getParentFile().exists()) {
			createDirectoryAndPermissions(savePath.getParentFile());
		}

		InputStream inputStream = null;

		// 파일 암호화 처리
		if (cryptYn) {
			byte[] srcBytes = IOUtils.toByteArray(file);
			try {
				byte[] encBytes = ObixSecurityUtil.encryptAES(srcBytes);
				inputStream = IOUtils.toBufferedInputStream(new ByteArrayInputStream(encBytes));
			} catch (EncoderException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				log.error("### 파일 암호화 실패 = " + filePath);
				log.error(e.getMessage());
				inputStream = IOUtils.toBufferedInputStream(new ByteArrayInputStream(srcBytes));
			} catch (Exception e) {
				log.error("### 파일 암호화 실패 = " + filePath);
				log.error(e.getMessage());
				inputStream = IOUtils.toBufferedInputStream(new ByteArrayInputStream(srcBytes));
			}
		} else {
			inputStream = IOUtils.toBufferedInputStream(file);
		}

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(new FileOutputStream(savePath));
			
			int i;
			byte readByte[] = new byte[4096];
			while ((i = bis.read(readByte, 0, 4096)) != -1) {
				bos.write(readByte, 0, i);
			}
			bos.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (bis != null) {
				bis.close();
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("[파일업로드] 서버에 저장된 파일경로 = {}", filePath);
			log.debug("[파일업로드] 서버에 저장된 파일크기 = {}", savePath.length());
		}

		// 디렉토리 퍼미션을 775으로 변경
		setFilePermission(savePath.toPath());

		return filePath;
	}

	/**
	 * 디렉토리 생성 및 퍼미션 조정(mkdirs)
	 * @param file 경로
	 */
	public static boolean createDirectoryAndPermissions(File file) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("### 디렉토리 생성 = {}", file.toPath());
		}
		if (file.exists()) {
			return false;
		}
		if (createDirectoryAndPermission(file)) {
			return true;
		}
        File canonFile = null;
        try {
            canonFile = file.getCanonicalFile();
        } catch (IOException e) {
            return false;
        }
        File parent = canonFile.getParentFile();
        return (parent != null && (createDirectoryAndPermissions(parent) || parent.exists()) && createDirectoryAndPermission(canonFile));
	}

	/**
	 * 디렉토리 생성 및 퍼미션 조정(mkdir)
	 * @param file 경로
	 */
	private static boolean createDirectoryAndPermission(File file) throws Exception {
		if (file.mkdir()) {
			setFilePermission(file.toPath());
			return true;
		}
		return false;
	}

	private static void setFilePermission(Path path) throws Exception {
		try {
			// 파일 퍼미션을 775로 변경
			Set<PosixFilePermission> perms = new HashSet<>(Arrays.asList(
					PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE,
					PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE));
			Files.setPosixFilePermissions(path, perms);
		} catch (UnsupportedOperationException e) {
			log.error("지원하지 않는 OS = {}", path);
		}
	}

	/**
	 * 파일을 생성하고, 성공여부를 반환한다.
	 * @param data 파일에 쓰기 위한 데이터
	 * @param filePath 파일 경로
	 * @return true : 성공, false : 실패
	 * @throws Exception
	 */
	public static boolean createFile(String data, String filePath) throws Exception {

		// 디렉토리 생성
		File savePath = new File(filePath);
		if (!savePath.exists()) {
			createDirectoryAndPermissions(savePath);
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(data);
			bw.flush();
		} catch (IOException e) {
			return false;
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
		
		return true;
	}

	/**
	 * List를 JSON 형식으로 변환하여 파일을 생성한다.
	 * @param list 파일에 쓰기 위한 데이터
	 * @param filePath 파일 경로
	 * @return true : 성공, false : 실패
	 * @throws Exception
	 */
	public static boolean createFile(List<?> list, String filePath) throws Exception {
		if (list == null || list.isEmpty()) {
			throw new UserException("BIZ.FILE.01").setDebuggingMessage("전달할 내용이 존재하지 않습니다.");
		}

		// 디렉토리 생성
		File savePath = new File(filePath);
		if (!savePath.exists()) {
			createDirectoryAndPermissions(savePath);
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath));

			for (int i = 0; i < list.size(); i++) {
				String data = new Gson().toJson(list.get(i));
				bw.write(data);
				bw.write('\n');
			}

			bw.flush();
		} catch (IOException e) {
			return false;
		} finally {
			if (bw != null) {
				bw.close();
			}
		}

		// 파일 퍼미션을 770으로 변경
		setFilePermission(savePath.toPath());

		return true;
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

	/**
	 * 전달된 폴더와 파일명으로 전체 파일경로를 생성하여 리턴한다. (지정경로에 일자별로 폴더를 생성하여 저장)
	 * @param rootDir 상위 파일 경로
	 * @param fileName 파일명
	 * @return
	 */
	public static String generateFileName(String rootDir, String fileName) throws Exception {
		String newFileName = rootDir + uniqueFileName(fileName);
		return checkFileName(newFileName, false);
	}

	/**
	 * 파일에 유니크한 이름을 구한다.
	 */
	private static String uniqueFileName(String fileName) throws Exception {
		// 서버에 저장할 새로운 파일명을 만든다.
		StringBuffer newFileName = new StringBuffer();
		newFileName.append( getRandomString(16,32) );
		newFileName.append( "_" );
		newFileName.append( DateUtil.getCurrentDateTime("HHmmss") );
		if (StringUtils.isNotBlank(fileName)) {
			newFileName.append( "." );
			newFileName.append( FilenameUtils.getExtension(fileName) );
		}
		return newFileName.toString();
	}

	/**
	 * 해당 파일명으로 저장된 데이터가 존재하면 새로운 파일명 리턴
	 * @param fileName 파일명
	 * @param overwriteYn 덮어쓰기 여부
	 * @return
	 */
	private static String checkFileName(String fileName, boolean overwriteYn) throws Exception {
		String newFileName = StringUtils.trimToEmpty(fileName);
		if (StringUtils.isEmpty(newFileName)) return null;

		// 저장경로 존재여부 검사를 위한 경로 취득
		String filePath = new File(newFileName).getParentFile().toString();

		// 저장경로 존재여부 검사, 폴더가 존재하지 않으면 생성
		File tmpFile = new File(filePath);
		if (!tmpFile.exists()) {
			createDirectoryAndPermissions(tmpFile);
		}

		if (!overwriteYn) {
			// 해당 경로에 같은 파일명이 존재하는지 검사
			// 같은 파일명이 존재할 경우 파일명에 "_i" 추가
			int i = 0;
			String tmpName = newFileName;
			while (true) {
				i++;
				tmpFile = new File(tmpName);
				if (!tmpFile.exists()) {
					break;
				} else {
					tmpName = FilenameUtils.getFullPath(newFileName) + FilenameUtils.getBaseName(newFileName) + "_" + i + "." + FilenameUtils.getExtension(newFileName);
					continue;
				}
			}
			newFileName = tmpName;
		}

		return newFileName;
	}

	/**
	 * 유효한 파일 확장자인지 체크
	 * @param storgDirRoot
	 * @param filename 업로드할 파일명
	 */
	private static void checkFileExt(FileConstant.STORG_DIRECTORY_ROOT storgDirRoot, String filename) throws Exception {
		// 확장자 유효성이 있으면, 검사
		if (StringUtils.isNotBlank(storgDirRoot.getFileExt())) {
			if (!FilenameUtils.isExtension(filename, storgDirRoot.getFileExt().toLowerCase().split(","))) {
				throw new UserException("BIZ.FILE.03").setDebuggingMessage("유효한 파일 확장자가 아님(업로드된 파일확장자 = " + FilenameUtils.getExtension(filename) + ")");
			}
		}
	}

	/**
	 * Return a specific length of random string.
	 * 
	 * @param size
	 *            the length of random string to be developed
	 * @return String random string
	 */
	public static String getRandomString(int size) {
		return randomAlphabetic(size);
	}

	/**
	 * Return random string between minimum and maximum digits.
	 * 
	 * @param minSize
	 *            minimum digits
	 * @param maxSize
	 *            maximum digits
	 * @return String random string
	 */
	public static String getRandomString(int minSize, int maxSize) {
		Random generator = new Random(System.currentTimeMillis());
		int randomLength = generator.nextInt(maxSize - minSize) + minSize;

		return randomAlphabetic(randomLength);
	}

	private static String randomAlphabetic(int size) {
		StringBuilder buf = new StringBuilder();
		GENERATOR.setSeed(System.currentTimeMillis());
		for (int i = 0; i < size; i++) {
			buf.append(ALPHAS[GENERATOR.nextInt(52)]);
		}
		return buf.toString();
	}

}
