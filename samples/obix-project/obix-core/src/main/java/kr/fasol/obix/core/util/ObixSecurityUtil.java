package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > Util > 암복호화 유틸
 * 프로그램명 :
 * 설명 : AES, SHA-256을 이용한 암/복호화
 *
 * <pre>
 * 1. AES 방식의 암복호화
 *    <code>encryptAES("abcd");</code>
 *    <code>decryptAES(encryptAES("abcd"));</code>
 * 2. SHA256 해쉬 암호화
 *    <code>SHA256("abcd");</code>
 * </pre>
 *
 * @Class ObixSecurityUtil.class
 * @since 2021/04/12
 * @author 정영탁
 * @version 1.0
 *
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2021/04/12        정영탁          최초생성
 */
@UtilityClass
public class ObixSecurityUtil {

	public static String getEnKey() {
		return EnvironmentUtil.getProperty("OBIX_SECURITY_KEY");
	}

    /**
     * AES-256을 이용하여 암호화
     * @param data 암호화할 문자열
     * @return 암호화된 문자열
     */
    public static String encryptAES(String data) throws Exception {
        if (data == null || data.length() == 0) {
            return "";
        }
        byte[] textBytes = data.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = encryptAES128CBC(textBytes);

        return Base64.encodeBase64String(encrypted);
    }

    /**
     * AES128CBC+HEXENC를 이용하여 암호화
     * @param data 암호화할 문자열
     * @return 암호화된 문자열
     */
    public static String encryptAES128CBC(String data) throws Exception {
        byte[] textBytes = data.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = encryptAES128CBC(textBytes);

        return Hex.encodeHexString(encrypted, false);
    }

    /**
     * AES-256을 이용하여 복호화
     * @param data 복호화할 문자열
     * @return 복호화된 문자열
     */
    public static String decryptAES(String data) throws Exception {
        if (data == null || data.length() == 0) {
            return "";
        }
        byte[] textBytes = Base64.decodeBase64(data);

        byte[] decrypted = decryptAES128CBC(textBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * AES128CBC를 이용하여 암호화
     * @param data 암호화할 문자열
     * @return 암호화된 문자열
     */
    public static byte[] encryptAES(byte[] data) throws Exception {
    	return encryptAES128CBC(data);
    }

    /**
     * AES128CBC를 이용하여 암호화
     * @param data 암호화할 문자열
     * @return 암호화된 문자열
     */
    public static byte[] encryptAES128CBC(byte[] data) throws Exception {
        if (data == null || data.length == 0) {
            return new byte[] {};
        }

        byte[] keyBytes = Hex.decodeHex(getEnKey());
        byte[] ivBytes = Hex.decodeHex("00000000000000000000000000000000");

        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(data);
    }

    /**
     * DECHEX+AES128CBC를 이용하여 복호화
     * @param data 복호화할 문자열
     * @return 복호화된 문자열
     */
    public static String decryptAES128CBC(String data) throws Exception {
        byte[] textBytes = Hex.decodeHex(data);

        byte[] decrypted = decryptAES128CBC(textBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * AES128CBC를 이용하여 복호화
     * @param data 복호화할 문자열
     * @return 복호화된 문자열
     */
    public static byte[] decryptAES(byte[] data) throws Exception {
    	return decryptAES128CBC(data);
    }

    /**
     * AES128CBC를 이용하여 복호화
     * @param data 복호화할 문자열
     * @return 복호화된 문자열
     */
    public static byte[] decryptAES128CBC(byte[] data) throws Exception {
        if (data == null || data.length == 0) {
            return new byte[] {};
        }

        byte[] keyBytes = Hex.decodeHex(getEnKey());
        byte[] ivBytes = Hex.decodeHex("00000000000000000000000000000000");

        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(data);
    }

    /**
     * AES-256을 이용하여 암호화
     * @param data 암호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @return 암호화된 문자열
     */
    public static String encryptAES(String data, byte[] key) throws Exception {
        if (data == null || data.length() == 0) {
            return "";
        }
        byte[] textBytes = data.getBytes("UTF-8");

        byte[] encrypted = encryptAES128CBC(textBytes, key);

        return Base64.encodeBase64String(encrypted);
    }

    /**
     * AES128CBC+HEXENC를 이용하여 암호화
     * @param data 암호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @return 암호화된 문자열
     */
    public static String encryptAES128CBC(String data, byte[] key) throws Exception {
        byte[] textBytes = data.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = encryptAES128CBC(textBytes, key);

        return Hex.encodeHexString(encrypted, false);
    }

    /**
     * AES-256을 이용하여 복호화
     * @param data 복호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @return 복호화된 문자열
     */
    public static String decryptAES(String data, byte[] key) throws Exception {
        if (data == null || data.length() == 0) {
            return "";
        }
        byte[] textBytes = Base64.decodeBase64(data);

        byte[] decrypted = decryptAES128CBC(textBytes, key);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * AES128CBC를 이용하여 암호화
     * @param data 암호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @return 암호화된 문자열
     */
    public static byte[] encryptAES128CBC(byte[] data, byte[] key) throws Exception {
        if (data == null || data.length == 0) {
            return new byte[] {};
        }

        byte[] ivBytes = Hex.decodeHex("00000000000000000000000000000000");

        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(data);
    }

    /**
     * DECHEX+AES128CBC를 이용하여 복호화
     * @param data 복호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @return 복호화된 문자열
     */
    public static String decryptAES128CBC(String data, byte[] key) throws Exception {
        byte[] textBytes = Hex.decodeHex(data);

        byte[] decrypted = decryptAES128CBC(textBytes);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * AES128CBC를 이용하여 복호화
     * @param data 복호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @return 복호화된 문자열
     */
    public static byte[] decryptAES128CBC(byte[] data, byte[] key) throws Exception {
        if (data == null || data.length == 0) {
            return new byte[] {};
        }

        byte[] ivBytes = Hex.decodeHex("00000000000000000000000000000000");

        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(data);
    }

    /**
     * AES-256을 이용하여 암호화
     * @param data 암호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @param iv 알고리즘 파라미터(16 Byte이상의 키값)
     * @return 암호화된 문자열
     */
    public static String encryptAES(String data, byte[] key, byte[] iv) throws Exception {
        if (data == null || data.length() == 0) {
            return "";
        }
        byte[] textBytes = data.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = encryptAES128CBC(textBytes, key, iv);

        return Base64.encodeBase64String(encrypted);
    }

    /**
     * AES128CBC를 이용하여 암호화
     * @param data 암호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @param iv 알고리즘 파라미터(16 Byte이상의 키값)
     * @return 암호화된 문자열
     */
    public static byte[] encryptAES128CBC(byte[] data, byte[] key, byte[] iv) throws Exception {
        if (data == null || data.length == 0) {
            return new byte[] {};
        }

        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(data);
    }

    /**
     * AES-256을 이용하여 복호화
     * @param data 복호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @param iv 알고리즘 파라미터(16 Byte이상의 키값)
     * @return 복호화된 문자열
     */
    public static String decryptAES(String data, byte[] key, byte[] iv) throws Exception {
        if (data == null || data.length() == 0) {
            return "";
        }
        byte[] textBytes = Base64.decodeBase64(data);

        byte[] decrypted = decryptAES128CBC(textBytes, key, iv);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * AES128CBC를 이용하여 복호화
     * @param data 복호화할 문자열
     * @param key 암호화 키(16 Byte이상의 키값)
     * @param iv 알고리즘 파라미터(16 Byte이상의 키값)
     * @return 복호화된 문자열
     */
    public static byte[] decryptAES128CBC(byte[] data, byte[] key, byte[] iv) throws Exception {
        if (data == null || data.length == 0) {
            return new byte[] {};
        }

        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(data);
    }


    /**
     * AES-128 암호화(EUC-KR)
     * @param inData 암호화할 문자열
     * @param inKey 암호화 키(16 Byte이상의 키값)
     * @return
     * @throws Exception
     */
    public static byte[] encryptAES128ECB(String inData, byte[] inKey) throws Exception {
		byte[] encryptedByteText = null;

		// inKey : 16진수암호키문자열을 byte[]로 변환
		SecretKey secretKey = new SecretKeySpec(inKey, "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// Cipher.ENCRYPT_MODE : 1
		cipher.init(Cipher.ENCRYPT_MODE, secretKey); 

		// inData : 평문
		encryptedByteText = cipher.doFinal(inData.getBytes("euc-kr"));

		// Base64 인코딩
		return Base64.encodeBase64(encryptedByteText);
	}

	/**
	 * AES-128 복호화(EUC-KR)
	 * @param inData 복호화할 문자열
	 * @param inKey 암호화 키(16 Byte이상의 키값)
	 * @return
	 * @throws Exception
	 */
    public static String decryptAES128ECB(String inData, byte[] inKey) throws Exception {
		String decryptedText = null;
		byte[] decryptedByteText = null;

		// inKey : 16진수암호키문자열을 byte[]로 변환
		SecretKey secretKey = new SecretKeySpec(inKey, "AES");

		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		// Cipher.DECRYPT_MODE : 2
		cipher.init(Cipher.DECRYPT_MODE, secretKey); 

		// inData : 암호화문 -> Base64 디코딩
		decryptedByteText = cipher.doFinal(Base64.decodeBase64(inData));
		decryptedText = new String(decryptedByteText, "euc-kr");

		return decryptedText;
	}

    /**
     * SHA-256 해쉬 암호화
     */
    public static String SHA256(String value) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(value.getBytes());
        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String getEncryptValue(String value) throws Exception {
    	if (StringUtils.isBlank(value)) {
    		return "";
    	}
    	return Base64.encodeBase64String(value.getBytes("UTF-32LE"));
    }
    public static String getDecryptValue(String value) throws Exception {
    	if (StringUtils.isBlank(value)) {
    		return "";
    	}
    	return new String(Base64.decodeBase64(value), "UTF-32LE");
    }

}
