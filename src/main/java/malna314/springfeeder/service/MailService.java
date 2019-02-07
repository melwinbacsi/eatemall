package malna314.springfeeder.service;

import malna314.springfeeder.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Async
    public class MailService{

    private JavaMailSender javaMailSender;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendMessage(Measurement measurement, Measurement previousMeasurement) {
        MimeMessage message = null;

        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("314malna@gmail.com");
            helper.setSubject("Event detected");
            helper.setText("Legutóbb xy táp fogyott.");
            FileSystemResource file = new FileSystemResource(new File("C:\\Users\\melwin\\Pictures\\7.png"));
            helper.addAttachment("Invoice", file);
            javaMailSender.send(message);

        } catch (Exception e) {
            System.out.println("Hiba e-mail küldéskor"  + "  " + e);
        }

    }}