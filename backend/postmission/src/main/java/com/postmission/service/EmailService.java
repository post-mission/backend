package com.postmission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String email,String template) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // 첨부파일 보낼거면 true
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("이메일을 인증해주세요");

            //html은 true
            mimeMessageHelper.setText(template,true);

            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.info("failed to send email", e);
        }

    }
}
