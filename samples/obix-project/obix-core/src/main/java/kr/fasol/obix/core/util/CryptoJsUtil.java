package kr.fasol.obix.core.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 시스템명 : 
 * 업무구분명 : 공통 > Util > CryptoJS를 이용한 암복호화 유틸
 * 프로그램명 : CryptoJS를 이용한 암복호화 유틸
 * 설명 :
 *
 * 클래스 : CryptoJsUtil.java
 * 작성자 : 정영탁
 * 작성일 : 2022. 10. 13.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2022. 10. 13.         정영탁          최초생성
 */
@UtilityClass
public class CryptoJsUtil {

    /**
     * AES-256을 이용하여 암호화
     * @param plaintext 암호화할 문자열
     * @param passphrase 암복호화 비밀번호
     * @return 암호화된 문자열
     */
    public static String encryptAES(String plaintext, String passphrase) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	if (StringUtils.isBlank(plaintext) || StringUtils.isBlank(passphrase)) {
            return "";
        }

        final int keySize = 256;
        final int ivSize = 128;

        // 텍스트를 Base64 형식으로 디코딩
        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        // Salt를 구한다. (Salted__ 시작되는 문자열)
        byte[] stBytes = "Salted__".concat(DateUtil.getCurrentDate(DateUtil.FORMATTER_HH_MM_SS)).getBytes(StandardCharsets.UTF_8);
        byte[] saltBytes = Arrays.copyOfRange(stBytes, 8, 16);

        // 비밀번호와 Salt에서 Key와 IV값을 가져옴.
        byte[] key = new byte[keySize / 8];
        byte[] iv = new byte[ivSize / 8];
        EvpKDF(passphrase.getBytes(StandardCharsets.UTF_8), keySize, ivSize, saltBytes, key, iv);

        // 암호화
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        byte[] receovedCiphertextBytes = cipher.doFinal(plaintextBytes);

        // 암호화된 텍스트 (Salt값과 암호화된 텍스트를 합한다.)
        byte[] ciphertextBytes = new byte[stBytes.length + receovedCiphertextBytes.length];
        System.arraycopy(stBytes, 0, ciphertextBytes, 0, stBytes.length);
        System.arraycopy(receovedCiphertextBytes, 0, ciphertextBytes, stBytes.length, receovedCiphertextBytes.length);

        // 암호화 문자열을 Base64 형식으로 인코딩
        return Base64.encodeBase64String(ciphertextBytes);
    }

    /**
     * AES-256을 이용하여 복호화
     * @param ciphertext 복호화할 문자열
     * @param passphrase 암복호화 비밀번호
     * @return 복호화된 문자열
     */
    public static String decryptAES(String ciphertext, String passphrase) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    	if (StringUtils.isBlank(ciphertext) || StringUtils.isBlank(passphrase)) {
            return "";
        }

        final int keySize = 256;
        final int ivSize = 128;

        // 텍스트를 Base64 형식으로 디코딩
        byte[] ctBytes = Base64.decodeBase64(ciphertext.getBytes(StandardCharsets.UTF_8));

        // Salt를 구한다. (생략된 8비트는 Salted__ 시작되는 문자열)
        byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);

        // 암호화된 텍스트 (Salt값 이후가 암호화된 텍스트)
        byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);

        // 비밀번호와 Salt에서 Key와 IV값을 가져옴.
        byte[] key = new byte[keySize / 8];
        byte[] iv = new byte[ivSize / 8];
        EvpKDF(passphrase.getBytes(StandardCharsets.UTF_8), keySize, ivSize, saltBytes, key, iv);

        // 복호화
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);

        return new String(recoveredPlaintextBytes, StandardCharsets.UTF_8);
    }

    /**
     * OpenSSL KDF
     */
    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    	return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }
    /**
     * OpenSSL KDF
     */
    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
    	keySize = keySize / 32;
    	ivSize = ivSize / 32;
    	int targetKeySize = keySize + ivSize;
    	byte[] derivedBytes = new byte[targetKeySize * 4];
    	int numberOfDerivedWords = 0;
    	byte[] block = null;
    	MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
    	while (numberOfDerivedWords < targetKeySize) {
    		if (block != null) {
    			hasher.update(block);
    		}
    		hasher.update(password);
    		// Salting
    		block = hasher.digest(salt);
    		hasher.reset();
    		// Iterations : 키 스트레칭(key stretching)
    		for (int i = 1; i < iterations; i++) {
    			block = hasher.digest(block);
    			hasher.reset();
    		}
    		System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
    		numberOfDerivedWords += block.length / 4;
    	}
    	System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
    	System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
    	return derivedBytes; // key + iv
    }

}
