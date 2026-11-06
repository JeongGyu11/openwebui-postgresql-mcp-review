package kr.fasol.obix.core.webe2e;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > 보안 > E2E 암복호화를 위한 유틸
 * 프로그램명 : E2E 암복호화를 위한 유틸
 * 설명 : JSEncrypt를 이용한 암복호화
 *
 * 클래스 : WebE2eSecurityUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2023. 7. 7.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2023. 7. 7.        정영탁			  최초작성
 */
@Slf4j
public class WebE2eSecurityUtil {

	private static final String KEY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "RSA/ECB/PKCS1Padding";

	// 서버 인증서 객체
	public static final ServerCertificateVO serverCertificateVO;

	static {
		// 인증서 초기화
		serverCertificateVO = initializeServerCertificate();
	}

    /**
	 * 전달된 Public Key를 이용하여 암호화
     */
    public static byte[] encrypt(String plainText, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(SIGNATURE_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(plainText.getBytes());
    }

    /**
	 * 전달된 Public Key를 이용하여 암호화
     */
    public static String encryptString(String plainText, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		byte[] encryptedData = encrypt(plainText, publicKey);
		return Base64.getEncoder().encodeToString(encryptedData);
    }

	/**
	 * Public Key를 이용하여 암호화
	 */
	public static String encryptString(String plainText) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		byte[] encryptedData = encrypt(plainText, serverCertificateVO.getPublicKeyString());
		return Base64.getEncoder().encodeToString(encryptedData);
	}

    /**
     * E2E 복호화
     */
    public static byte[] decrypt(byte[] cipherData, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(SIGNATURE_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherData);
    }

    /**
     * E2E 복호화
     */
    public static String decryptString(String cipherText) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = decrypt(cipherBytes, serverCertificateVO.getPrivateKey());
		return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

	/**
	 * E2E 암호화를 위한 Public Key
	 */
	public static String getPublicKeyString() {
		return serverCertificateVO.getPublicKeyString();
	}

	/**
	 * E2E 암호화를 위한 Public Key 생성
	 */
	private static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			byte[] encodeKey = Base64.getDecoder().decode(base64PublicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodeKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			publicKey = keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error("[WebE2eSecurityUtil][getPublicKey]", e);
		}
		return publicKey;
	}

	/**
	 * E2E 복호화를 위한 Private Key 생성
	 */
	private static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		byte[] encodeKey = Base64.getDecoder().decode(base64PrivateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodeKey);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			log.error("[WebE2eSecurityUtil][getPrivateKey]", e);
		}
		return privateKey;
	}

	/**
	 * webe2e 프로퍼티 파일 로딩
	 */
	private static ServerCertificateVO initializeServerCertificate() {
		ServerCertificateVO serverCertificateVO = new ServerCertificateVO();
		InputStream propInputStream = null;

		try {
			// webe2e.properties 파일의 경로 설정
			String webe2ePropPath = System.getProperty("WEBE2E_CONF_PATH");
			if (StringUtils.isBlank(webe2ePropPath)) {
				webe2ePropPath = "webe2e.properties";
			}

			if (webe2ePropPath.startsWith("/")) {
				propInputStream = new FileInputStream(webe2ePropPath);
			} else {
				Resource resource = new DefaultResourceLoader().getResource(webe2ePropPath);
				propInputStream = resource.getInputStream();
			}

			// properties에서 인증키 조회
			Properties properties = new Properties();
			properties.load(propInputStream);
			String publicKey = properties.getProperty("webe2e.public.key");
			String privateKey = properties.getProperty("webe2e.private.key");

			// 인증키를 객체로 변환하여 static에 보관
			serverCertificateVO = ServerCertificateVO.builder()
					.privateKeyString(privateKey)
					.publicKeyString(publicKey)
					.privateKey(getPrivateKey(privateKey))
					.publicKey(getPublicKey(publicKey))
					.build();

		} catch (Exception e) {
			log.error("[WebE2eSecurityUtil][initializeServerCertificate]", e);
		} finally {
			if (propInputStream != null) {
				try {
					propInputStream.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}

		return serverCertificateVO;
	}

}
