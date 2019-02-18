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
    public class MailService{

    private Measurement measurement;
    private Measurement previousMeasurement;
    private String path;
    private JavaMailSender javaMailSender;
    @Value("${picture.directory}")
    private String dir;

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendMessage(Measurement measurement, Measurement previousMeasurement, String path) {
        this.measurement=measurement;
        this.previousMeasurement=previousMeasurement;
        this.path=path;
        System.out.println(measurement);
        System.out.println(previousMeasurement);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo("314malna@gmail.com");
            helper.setSubject("Event detected");
            helper.setText("Jelenleg " + previousMeasurement.getActualWeight() + "g táp van a tálban.\nLegutóbb " + (previousMeasurement.getActualWeight() - measurement.getActualWeight()) + "g táp fogyott.\nAz előző etetés óta összesen " + (measurement.getOrigoWeight() - measurement.getActualWeight()) + " g tápot ettek.");
            FileSystemResource file = new FileSystemResource(new File(path));
            helper.addAttachment("lesifoto.jpg", file);
            javaMailSender.send(message);

        } catch (Exception e) {
            System.out.println("Hiba e-mail küldéskor"  + "  " + e);
        }

    }}