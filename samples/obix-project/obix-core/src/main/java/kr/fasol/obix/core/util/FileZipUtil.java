package kr.fasol.obix.core.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > 파일 압축(ZIP) 유틸
 * 프로그램명 : 파일 압축(ZIP) 유틸
 * 설명 :
 *
 * 클래스 : FileZipUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 11. 2.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 11. 2.         정영탁          최초생성
 */
@Slf4j
@UtilityClass
public class FileZipUtil {

	/**
	 * 파일을 압축하여 반환
	 * @param fileList 압축할 파일 목록
	 * @return 저장할 파일
	 */
	public static byte[] saveToFileZip(List<File> fileList) throws Exception {
		List<InputStream> inputStreamList = new ArrayList<>();
		List<String> filenameList = new ArrayList<>();

		for (File file : fileList) {
			if (file.isFile()) {
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
					inputStreamList.add(inputStream);
					filenameList.add(file.getName());
				} catch (IOException e) {
					log.error(e.getMessage());
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}

		return saveToZip(inputStreamList, filenameList);
	}

	/**
	 * 파일을 압축하여 반환
	 * @param fileList 압축할 파일 목록
	 * @return 저장할 파일
	 */
	public static byte[] saveToMultipartFileZip(List<MultipartFile> fileList) throws Exception {
		
		List<InputStream> inputStreamList = new ArrayList<>();
		List<String> filenameList = new ArrayList<>();

		for (MultipartFile file : fileList) {
			inputStreamList.add(file.getInputStream());
			filenameList.add(file.getOriginalFilename());
		}

		return saveToZip(inputStreamList, filenameList);
	}

	/**
	 * 파일을 압축하여 반환
	 * @param inputStream 압축할 파일
	 * @return 저장할 파일
	 */
	public static byte[] saveToZip(InputStream inputStream, String filename) throws Exception {
		List<InputStream> inputStreamList = new ArrayList<>();
		List<String> filenameList = new ArrayList<>();
		inputStreamList.add(inputStream);
		filenameList.add(filename);
		return saveToZip(inputStreamList, filenameList);
	}

	/**
	 * 파일을 압축하여 반환
	 * @param inputStreamList 압축할 파일 목록
	 * @return 저장할 파일
	 */
	public static byte[] saveToZip(List<InputStream> inputStreamList, List<String> filenameList) throws Exception {
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		if (inputStreamList == null || filenameList == null || inputStreamList.size() != filenameList.size()) {
			log.error("## [saveToZip] 입력값이 상이합니다.");
			return byteArrayOutputStream.toByteArray();
		}

		ZipOutputStream zos = new ZipOutputStream(byteArrayOutputStream);

		int length;
		ZipEntry ze;
		byte[] buf = new byte[8 * 1024];

		try {
			for (int i = 0; i < inputStreamList.size(); i++) {
				InputStream inputStream = inputStreamList.get(i);
				String filename = filenameList.get(i);

				ze = new ZipEntry(filename);
				zos.putNextEntry(ze);

				while ((length = inputStream.read(buf, 0, buf.length)) >= 0) {
					zos.write(buf, 0, length);
				}
				inputStream.close();
				zos.closeEntry();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (zos != null)
				zos.close();
		}

		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * 압축 해제
	 */
	public static void unzip(File zippedFile) throws IOException {
		unzip(zippedFile, Charset.defaultCharset().name());
	}

	/**
	 * 압축 해제
	 */
	public static void unzip(File zippedFile, String charsetName) throws IOException {
		unzip(zippedFile, zippedFile.getParentFile(), charsetName);
	}

	/**
	 * 압축 해제
	 */
	public static void unzip(File zippedFile, File destDir) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(zippedFile);
			unzip(inputStream, destDir, Charset.defaultCharset().name());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * 압축 해제
	 */
	public static void unzip(File zippedFile, File destDir, String charsetName) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(zippedFile);
			unzip(inputStream, destDir, charsetName);
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * 압축 해제
	 */
	public static void unzip(InputStream is, File destDir) throws IOException {
		unzip(is, destDir, Charset.defaultCharset().name());
	}

	/**
	 * 압축 해제
	 */
	public static void unzip(InputStream is, File destDir, String charsetName) throws IOException {
		ZipInputStream zis = null;
		ZipEntry entry;
		String name;
		File target;
		int nWritten = 0;
		BufferedOutputStream bos = null;
		byte[] buf = new byte[1024 * 8];

		try {
			zis = new ZipInputStream(is, Charset.forName(charsetName));
			while ((entry = zis.getNextEntry()) != null) {
				name = entry.getName();
				target = new File(destDir, name);
				if (entry.isDirectory()) {
					target.mkdirs();
				} else {
					target.createNewFile();
					try {
						bos = new BufferedOutputStream(new FileOutputStream(target));
						while ((nWritten = zis.read(buf)) >= 0) {
							bos.write(buf, 0, nWritten);
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					} catch (Exception e) {
						log.error(e.getMessage());
					} finally {
						if (bos != null)
							bos.close();
					}
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (zis != null)
				zis.close();
		}
	}

	/**
	 * 압축
	 */
	public static void zip(File src) throws IOException {
		zip(src, Charset.defaultCharset().name(), true);
	}

	/**
	 * 압축
	 */
	public static void zip(File src, boolean includeSrc) throws IOException {
		zip(src, Charset.defaultCharset().name(), includeSrc);
	}

	/**
	 * 압축
	 */
	public static void zip(File src, String charSetName, boolean includeSrc) throws IOException {
		zip(src, src.getParentFile(), charSetName, includeSrc);
	}

	/**
	 * 압축
	 */
	public static void zip(File src, OutputStream os) throws IOException {
		zip(src, os, Charset.defaultCharset().name(), true);
	}

	/**
	 * 압축
	 */
	public static void zip(File src, File destDir, String charSetName, boolean includeSrc) throws IOException {
		String fileName = src.getName();
		if (!src.isDirectory()) {
			int pos = fileName.lastIndexOf(".");
			if (pos > 0) {
				fileName = fileName.substring(0, pos);
			}
		}
		fileName += ".zip";

		File zippedFile = new File(destDir, fileName);
		if (!zippedFile.exists())
			zippedFile.createNewFile();
		
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(zippedFile);
			zip(src, outputStream, charSetName, includeSrc);
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	/**
	 * 압축
	 */
	public static void zip(File src, OutputStream os, String charsetName, boolean includeSrc) throws IOException {
		ZipOutputStream zos = new ZipOutputStream(os, Charset.forName(charsetName));
		FileInputStream fis = null;

		int length;
		ZipEntry ze;
		byte[] buf = new byte[8 * 1024];
		String name;

		try {
			Stack<File> stack = new Stack<File>();
			File root;
			if (src.isDirectory()) {
				if (includeSrc) {
					stack.push(src);
					root = src.getParentFile();
				} else {
					File[] fs = src.listFiles();
					for (int i = 0; i < fs.length; i++) {
						stack.push(fs[i]);
					}
					root = src;
				}
			} else {
				stack.push(src);
				root = src.getParentFile();
			}

			while (!stack.isEmpty()) {
				File f = stack.pop();
				name = toPath(root, f);
				if (f.isDirectory()) {
					File[] fs = f.listFiles();
					for (int i = 0; i < fs.length; i++) {
						if (fs[i].isDirectory())
							stack.push(fs[i]);
						else
							stack.add(0, fs[i]);
					}
				} else {
					ze = new ZipEntry(name);
					zos.putNextEntry(ze);
					try {
						fis = new FileInputStream(f);
						while ((length = fis.read(buf, 0, buf.length)) >= 0) {
							zos.write(buf, 0, length);
						}
					} catch (IOException e) {
						log.error(e.getMessage());
					} catch (Exception e) {
						log.error(e.getMessage());
					}
					finally {
						if (fis != null)
							fis.close();
					}
					zos.closeEntry();
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			zos.close();
		}
	}

	/**
	 * GZip 압축
	 */
	public static void compressGzip(File src, OutputStream os) throws IOException {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		FileInputStream fis = new FileInputStream(src);

		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = fis.read(buf)) != -1) {
				gos.write(buf, 0, len);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			gos.close();
			fis.close();
		}
	}

	/**
	 * GZip 압축 해제
	 */
	public static void decompressGzip(String srcFilename, String destFilename) throws IOException {
		InputStream inputStream = null;
		File destDir = null;
		try {
			inputStream = new FileInputStream(srcFilename);
			destDir = new File(destFilename);
			decompressGzip(inputStream, destDir);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	/**
	 * GZip 압축 해제
	 */
	public static void decompressGzip(InputStream is, File destDir) throws IOException {
		GZIPInputStream gis = new GZIPInputStream(is);
		FileOutputStream fos = new FileOutputStream(destDir);

		try {
			byte[] buf = new byte[1024];
			int len;
			while ((len = gis.read(buf)) != -1) {
				fos.write(buf, 0, len);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			fos.close();
			gis.close();
		}
	}

	private static String toPath(File root, File dir) {
		String path = dir.getAbsolutePath();
		path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
		if (path.startsWith("/"))
			path = path.substring(1);
		if (dir.isDirectory() && !path.endsWith("/"))
			path += "/";
		return path;
	}
}
