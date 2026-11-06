package kr.fasol.obix.core.webe2e;

import kr.fasol.obix.core.util.CryptoJsUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > 보안 > E2E 암복호화를 위한 유틸
 * 프로그램명 : E2E 암복호화를 위한 유틸
 * 설명 :
 *
 * 클래스 : WebE2eProvider.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 15.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 15.        정영탁			  최초작성
 */
@Slf4j
@UtilityClass
public class WebE2eProvider {

    private static final String CLIENT_SESSION_KEY = WebE2eProvider.class.getName() + ".CLIENT_SESSION_KEY";

    /**
     * 서버 인증서 조회
     */
    public String getServerCertificate() {
        return WebE2eSecurityUtil.getPublicKeyString();
    }

    /**
     * 클라이언트의 암호화된 세션키 전달
     */
    public String getDecryptClientSessionKey(String clientSessionKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return WebE2eSecurityUtil.decryptString(clientSessionKey);
    }
    public void setClientSessionKey(String clientSessionKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 클라이언트로부터 전달받은 암호화된 세션키를 Private Key를 이용해서 복호화하여 세션에 보관
        setSessionValue(getDecryptClientSessionKey(clientSessionKey));
    }

    /**
     * Data 암호화
     */
    public String getEncryptData(String plainText, String clientSessionKey) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 클라이언트로부터 전달받은 세션키를 이용하여 AES 암호화
        return CryptoJsUtil.encryptAES(plainText, clientSessionKey);
    }
    public String getEncryptData(String plainText) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return getEncryptData(plainText, getSessionValue());
    }

    /**
     * Data 복호화
     */
    public String getDecryptData(String cipherText, String clientSessionKey) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 클라이언트로부터 전달받은 세션키를 이용하여 AES 복호화
        return CryptoJsUtil.decryptAES(cipherText, clientSessionKey);
    }
    public String getDecryptData(String cipherText) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return getDecryptData(cipherText, getSessionValue());
    }

    /**
     * 세션에 저장된 객체 조회
     */
    private String getSessionValue() {
        return (String) RequestContextHolder.currentRequestAttributes().getAttribute(WebE2eProvider.CLIENT_SESSION_KEY, RequestAttributes.SCOPE_SESSION);
    }
    /**
     * 세션에 객체 저장
     */
    private void setSessionValue(String value) {
        RequestContextHolder.currentRequestAttributes().setAttribute(WebE2eProvider.CLIENT_SESSION_KEY, value, RequestAttributes.SCOPE_SESSION);
    }

}
