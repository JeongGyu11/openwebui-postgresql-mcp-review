package kr.fasol.obix.core.util;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 이메일 발송 Util
 *
 * @Class  MailSenderUtil.class
 * @author  정영탁
 * @since   2018. 10. 24.
 * @version 1.0
 *
 * @Copyright (c) Finance All Solutions.
 *--------------------------------------------------------------------
 * Modification Information
 *--------------------------------------------------------------------
 * 수정일               수정자            수정내용
 *--------------------------------------------------------------------
 * 2018. 10. 24.        정영탁          최초생성
 */
@Slf4j
@Component
public class MailSenderUtil {

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * 메일 발송
	 * @param from 발신자
	 * @param to 수신자
	 * @param subject 제목
	 * @param text 내용
	 */
	public void sendSimpleMessage(String from, String to, String subject, String text) throws Exception {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(text);
		mailMessage.setSentDate(new Date());
		sendTextMessage(mailMessage, from);
	}

	/**
	 * TEXT 형식의 메일 발송
	 * @param message 메일 메시지
	 * @param fromEmail 발신자 이메일
	 */
	public void sendTextMessage(SimpleMailMessage message, String fromEmail) throws Exception {
		sendTextMessage(message, fromEmail, null);
	}
	/**
	 * TEXT 형식의 메일 발송
	 * @param message 메일 메시지
	 * @param fromEmail 발신자 이메일
	 * @param fromName 발신자 이름
	 */
	public void sendTextMessage(SimpleMailMessage message, String fromEmail, String fromName) throws Exception {
		sendMessage(message, false, fromEmail, fromName);
	}

	/**
	 * HTML 형식의 메일 발송
	 * @param message 메일 메시지
	 * @param fromEmail 발신자 이메일
	 */
	public void sendHtmlMessage(SimpleMailMessage message, String fromEmail) throws Exception {
		sendHtmlMessage(message, fromEmail, null);
	}
	/**
	 * HTML 형식의 메일 발송
	 * @param message 메일 메시지
	 * @param fromEmail 발신자 이메일
	 * @param fromName 발신자 이름
	 */
	public void sendHtmlMessage(SimpleMailMessage message, String fromEmail, String fromName) throws Exception {
		sendMessage(message, true, fromEmail, fromName);
	}

	/**
	 * 메일 발송
	 * @param message 메일 메시지
	 * @param isHtmlTemplate HTML 템플릿 여부
	 * @param fromEmail 발신자 이메일
	 * @param fromName 발신자 이름
	 */
	public void sendMessage(SimpleMailMessage message, boolean isHtmlTemplate, String fromEmail, String fromName) throws Exception {
		InternetAddress fromAddress = createInternetAddress(fromEmail, fromName);
		MimeMessage mimeMessage = createMimeMessage(fromAddress, message, isHtmlTemplate);
		mailSender.send(mimeMessage);
	}

	/**
	 * 템플릿을 이용하여 메일 발송
	 * @param mailMessageDto 메일 메시지 DTO
	 */
	public void sendMessage(MailMessageDto mailMessageDto) throws Exception {
		InternetAddress fromAddress = createInternetAddress(mailMessageDto.getFromEmail(), mailMessageDto.getFromName());
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(mailMessageDto.getToEmail());
		message.setSubject(mailMessageDto.getSubject());
		message.setText(mailMessageDto.getText());
		message.setSentDate(new Date());
		MimeMessage mimeMessage = createMimeMessage(fromAddress, message, true);
		mailSender.send(mimeMessage);
	}

	/**
	 * InternetAddress 생성
	 * @param email 이메일 주소
	 * @param name 이름
	 * @return InternetAddress 객체
	 */
	private InternetAddress createInternetAddress(String email, String name) throws Exception {
		if (name == null) {
			return new InternetAddress(email);
		} else {
			return new InternetAddress(email, MimeUtility.encodeText(name, "UTF-8", "B"));
		}
	}

	/**
	 * MimeMessage 생성
	 * @param fromAddress 발신자 주소
	 * @param message 메일 메시지
	 * @param isHtmlTemplate HTML 템플릿 여부
	 * @return MimeMessage 객체
	 */
	private MimeMessage createMimeMessage(InternetAddress fromAddress, SimpleMailMessage message, boolean isHtmlTemplate) throws Exception {
		MimeMessage mimeMessage = new MimeMessage(Session.getInstance(System.getProperties()));
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setFrom(fromAddress);
		mimeMessageHelper.setTo(message.getTo());
		if (message.getCc() != null) {
			mimeMessageHelper.setCc(message.getCc());
		}
		if (message.getBcc() != null) {
			mimeMessageHelper.setBcc(message.getBcc());
		}
		mimeMessageHelper.setSubject(message.getSubject());
		mimeMessageHelper.setText(message.getText(), isHtmlTemplate);
		mimeMessageHelper.setSentDate(message.getSentDate());
		return mimeMessageHelper.getMimeMessage();
	}

	/**
	 * 이메일 메시지의 데이터를 담는 DTO
	 */
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class MailMessageDto {
		private String fromEmail;	// 발신자 이메일 주소
		private String fromName;	// 발신자 이름
		private String toEmail;		// 수신자 이메일 주소
		private String toName;		// 수신자 이름
		private String ccEmail;		// 참조 이메일 주소
		private String ccName;		// 참조 이름
		private String bccEmail;	// 숨은 참조 이메일 주소
		private String bccName;		// 숨은 참조 이름
		private String subject;		// 이메일 제목
		private String text;		// 이메일 내용
	}

}
