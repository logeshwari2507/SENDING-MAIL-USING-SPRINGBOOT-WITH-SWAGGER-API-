//package com.email.service;
//
//import com.email.model.EmailModel;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//
//@Service
//@Slf4j
//public class EmailServiceImpl implements EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    @Override
//    public void sendEmail(EmailModel emailModel) {
//
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//
//            mimeMessageHelper.setTo(emailModel.getToEmail());
//            mimeMessageHelper.setCc(emailModel.getCcEmail());
//            mimeMessageHelper.setSubject(emailModel.getEmailSubject());
//            mimeMessageHelper.setText(emailModel.getEmailBody());
//
//            FileSystemResource fileSystemResource = new FileSystemResource(new File(emailModel.getEmailAttachment()));
//
//            mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
//
//            javaMailSender.send(mimeMessage);
//            
//            log.info("Email Sent Successfully");
//
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//}
package com.email.service;

import com.email.model.EmailModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailModel emailModel) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(emailModel.getToEmail());
            mimeMessageHelper.setCc(emailModel.getCcEmail());
            mimeMessageHelper.setSubject(emailModel.getEmailSubject());
            mimeMessageHelper.setText(emailModel.getEmailBody());

            if (emailModel.getEmailAttachment() != null) {
                File file = new File(emailModel.getEmailAttachment());
                if (file.exists()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(file);
                    mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
                } else {
                    logger.warn("Attachment file not found: {}", emailModel.getEmailAttachment());
                }
            }

            javaMailSender.send(mimeMessage);

            logger.info("Email Sent Successfully");
        } catch (MessagingException e) {
            logger.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
