package kr.fasol.obix.core.webe2e;

import lombok.*;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 시스템명 :
 * 업무구분명 : 공통 > 보안 > E2E 암복호화를 위한 서버 인증서 VO
 * 프로그램명 : E2E 암복호화를 위한 서버 인증서 VO
 * 설명 :
 *
 * 클래스 : ServerCertificateVO.java
 * 작성자 : 정영탁
 * 작성일 : 2024. 11. 15.
 * --------------------------------------------------------------------
 * Modification Information
 * --------------------------------------------------------------------
 * 수정일               수정자            수정내용
 * --------------------------------------------------------------------
 * 2024. 11. 15.        정영탁			  최초작성
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerCertificateVO {

    /**
     * 마스터 키
     */
    private String masterKey;

    /**
     * 개인키 문자열
     */
    private String privateKeyString;
    /**
     * 공개키 문자열
     */
    private String publicKeyString;

    /**
     * 개인키 객체
     */
    private PrivateKey privateKey;

    /**
     * 공개키 객체
     */
    private PublicKey publicKey;

}
