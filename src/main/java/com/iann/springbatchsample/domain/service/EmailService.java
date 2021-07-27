package com.iann.springbatchsample.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (Exception e) {
            // 이메일 전송 실패, 로그에 넣는다.
            // 다시 보내기 위한 전 작업을 해야 한다.
            log.error("EmailService.sendSimpleMessage()");
            log.error("{}", e.getMessage());
        }
    }


    public void sendAttachMessage(String to, String subject, String text,String fileName, String fileToAttach)  {
        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText("text", true);

            FileSystemResource file = new FileSystemResource(new File(fileToAttach));
            helper.addAttachment(fileName, file);

            emailSender.send(message);
        } catch (MessagingException e) {
            // 이메일 전송 실패, 로그에 넣는다.
            // 다시 보내기 위한 전 작업을 해야 한다.
            log.error("EmailService.sendAttachMessage()");
            log.error("{}", e.getMessage());
        }
    }
}