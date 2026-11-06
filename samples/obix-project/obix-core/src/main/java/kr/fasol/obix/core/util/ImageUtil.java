package kr.fasol.obix.core.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriter;
import com.github.jaiimageio.impl.plugins.tiff.TIFFT6Compressor;
import com.github.jaiimageio.plugins.tiff.TIFFImageWriteParam;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Util > 이미지 변환 유틸
 * 프로그램명 : 이미지 변환 유틸
 * 설명 :
 *
 * 클래스 : ImageUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2021. 9. 25.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021. 9. 25.         정영탁          최초생성
 */
@Slf4j
@UtilityClass
public class ImageUtil {

	@Getter
	public static enum PICTURE_TYPE {
		JPEG("jpg"),
		PNG("png"),
		TIFF("tiff");
		private String type;
		PICTURE_TYPE(String type) {
			this.type = type;
		}
	}

	/**
	 * TIFF 이미지로 변환
	 * @param inputStreamList 변환할 이미지 버퍼 목록
	 * @return 저장할 파일
	 */
	public static byte[] saveToTiffImage(List<InputStream> inputStreamList) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);

		TIFFImageWriter writer = (TIFFImageWriter) ImageIO.getImageWritersByFormatName(PICTURE_TYPE.TIFF.getType()).next();
		TIFFImageWriteParam writeParam = (TIFFImageWriteParam) writer.getDefaultWriteParam();
		TIFFT6Compressor compressor = new TIFFT6Compressor();
		writeParam.setCompressionMode(TIFFImageWriteParam.MODE_EXPLICIT);
		writeParam.setCompressionType("LZW");
		writeParam.setTIFFCompressor(compressor);

		writer.setOutput(imageOutputStream);

		writer.prepareWriteSequence(null);
		for (InputStream inputStream : inputStreamList) {
			writer.writeToSequence(new IIOImage(ImageIO.read(inputStream), null, null), writeParam);
		}
		writer.endWriteSequence();

		writer.dispose();
		imageOutputStream.close();

		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * TIFF 이미지를 JPEG 이미지로 변환
	 * @param tiffImageBytes 변환할 TIFF 이미지
	 * @return 변환된 이미지 목록
	 */
	public static List<byte[]> readFromTiffImage(byte[] tiffImageBytes) throws Exception {
		List<byte[]> imageList = new ArrayList<>();

		TiffImageParser tiffImageParser = new TiffImageParser();
		List<BufferedImage> tiffBufferedImage = tiffImageParser.getAllBufferedImages(tiffImageBytes);

		tiffBufferedImage.forEach(tiffImage -> {
			try {
				BufferedImage jpegImage = new BufferedImage(tiffImage.getWidth(), tiffImage.getHeight(), BufferedImage.TYPE_INT_RGB);
				// Draw image from original TIFF to the new JPEG image
				jpegImage.createGraphics().drawImage(tiffImage, 0, 0, Color.WHITE, null);
				// Write the image as JPEG to disk
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(jpegImage, PICTURE_TYPE.JPEG.getType(), ImageIO.createImageOutputStream(byteArrayOutputStream));
				imageList.add( byteArrayOutputStream.toByteArray() );
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});

		return imageList;
	}

	/**
	 * 썸네일 이미지로 변환
	 * @param inputStream 변환할 이미지 버퍼
	 * @return 저장할 파일
	 */
	public static byte[] saveToThumbnail(InputStream inputStream) throws Exception {
		return saveToThumbnail(inputStream, 100);
	}
	/**
	 * 썸네일 이미지로 변환
	 * @param inputStream 변환할 이미지 버퍼
	 * @param quality 생성할 이미지의 Quality(최대 100)
	 * @return 저장할 파일
	 */
	public static byte[] saveToThumbnail(InputStream inputStream, int quality) throws Exception {
		return saveToThumbnail(inputStream, 0, 0, quality);
	}
	/**
	 * 썸네일 이미지로 변환
	 * @param inputStream 변환할 이미지 버퍼
	 * @param width 생성할 이미지의 Width
	 * @param height 생성할 이미지의 Height
	 * @param quality 생성할 이미지의 Quality(최대 100)
	 * @return 저장할 파일
	 */
	public static byte[] saveToThumbnail(InputStream inputStream, int width, int height, int quality) throws Exception {
		// Exif가 제거된(정위치) 이미지객체 반환
		BufferedImage imageBuffer = getRotateImage(IOUtils.toByteArray(inputStream));
		// 썸네일로 변환
		return saveToThumbnail(imageBuffer, width, height, quality);
	}

	/**
	 * 썸네일 이미지로 변환
	 * @param imageBuffer 변환할 이미지 버퍼
	 * @param width 생성할 이미지의 Width
	 * @param height 생성할 이미지의 Height
	 * @param quality 생성할 이미지의 Quality(최대 100)
	 * @return 저장할 파일
	 */
	public static byte[] saveToThumbnail(BufferedImage imageBuffer, int width, int height, int quality) throws Exception {
		Assert.notNull(imageBuffer, "저장할 이미지 객체는 필수 항목입니다.");
		Assert.isTrue(quality >= 0 && quality <= 100, "Quality는 0~100 사이의 숫자입니다.");

		// 원본 이미지 크기
		int imageWidth = imageBuffer.getWidth();
		int imageHeight = imageBuffer.getHeight();

		int targetWidth = width > 0 ? width : (height > 0 ? imageWidth * height / imageHeight : imageWidth);
		int targetHeight = height > 0 ? height : (width > 0 ? imageHeight * width / imageWidth : imageHeight);

		// 썸네일 이미지 생성 (3바이트 RGB)
		BufferedImage thumbnailImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = thumbnailImage.createGraphics();

		try {
			// 렌더링 품질 향상 옵션 추가
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			// 이미지 리사이징 후 그리기
			g2d.drawImage(imageBuffer, 0, 0, targetWidth, targetHeight, null);
		} finally {
			g2d.dispose(); // 리소스 정리
		}

		// JPEG 품질 조정 및 이미지 저장
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
			if (!writers.hasNext()) {
				throw new IOException("No available JPEG writer");
			}

			ImageWriter writer = writers.next();
			writer.setOutput(ios);

			// JPEG 품질 조정
			JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
			jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpegParams.setCompressionQuality(quality / 100.0f); // 0.0 ~ 1.0

			writer.write(null, new javax.imageio.IIOImage(thumbnailImage, null, null), jpegParams);
			writer.dispose();

			return baos.toByteArray();
		}
	}

	/**
	 * 이미지 Exif가 제거된(정위치) 이미지객체 반환
	 */
	public static BufferedImage getRotateImage(byte[] imgBytes) throws Exception {
		InputStream inputStream = new ByteArrayInputStream(imgBytes);
		BufferedImage image = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(imgBytes));
			if (image == null) {
				throw new IOException("이미지 읽기 실패");
			}

			// 원본 이미지의 Orientation 정보
			int orientation = 1;	// 회전정보, 1. 0도, 3. 180도, 6. 270도, 8. 90도 회전한 정보 (기본값: 1 회전없음)
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();

			// 메타데이터 읽기
			Metadata metadata = ImageMetadataReader.readMetadata(inputStream);		// 이미지 메타데이터 객체
			Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);	// 이미지의 Exif 데이터를 읽기 위한 객체

			if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
				orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
			}

			// AffineTransform을 사용하여 회전
			AffineTransform atf = new AffineTransform();
			switch (orientation) {
				case 1: // 회전 없음
					break;
				case 2: // 좌우 반전
					atf.scale(-1.0, 1.0);
					atf.translate(-imageWidth, 0);
					break;
				case 3: // 180도 회전
					atf.translate(imageWidth, imageHeight);
					atf.rotate(Math.PI);
					break;
				case 4: // 상하 반전
					atf.scale(1.0, -1.0);
					atf.translate(0, -imageHeight);
					break;
				case 5: // -90도 회전 후 좌우 반전
					atf.rotate(-Math.PI / 2);
					atf.scale(-1.0, 1.0);
					break;
				case 6: // 90도 회전
					atf.translate(imageHeight, 0);
					atf.rotate(Math.PI / 2);
					break;
				case 7: // 90도 회전 후 좌우 반전
					atf.scale(-1.0, 1.0);
					atf.translate(-imageHeight, 0);
					atf.translate(0, imageWidth);
					break;
				case 8: // -90도 회전
					atf.translate(0, imageWidth);
					atf.rotate(3 * Math.PI / 2);
					break;
				default:
					log.warn("Unknown orientation: {}", orientation);
					break;
			}

			// 이미지 회전 적용
			AffineTransformOp rotateOp = new AffineTransformOp(atf, AffineTransformOp.TYPE_BICUBIC);
			BufferedImage destinationImage = rotateOp.createCompatibleDestImage(image, (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null);
			Graphics2D g = destinationImage.createGraphics();
			g.setBackground(Color.WHITE);
			g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
			destinationImage = rotateOp.filter(image, destinationImage);

			return destinationImage;
		} catch (IOException | ImageProcessingException | MetadataException e) {
			log.error("이미지 회전 처리 중 오류 발생: {}", e.getMessage(), e);
		} catch (Exception e) {
			log.error("알 수 없는 오류 발생: {}", e.getMessage(), e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.warn("스트림 닫기 실패: {}", e.getMessage());
			}
		}
		return image;
	}

}
