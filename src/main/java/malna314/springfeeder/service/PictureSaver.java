package malna314.springfeeder.service;


import malna314.springfeeder.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;

@Service
@Scope("prototype")
@Async
public class PictureSaver{
    private String path;
    private LocalDateTime measurementTime;
    private Measurement measurement;
    private Measurement previousMeasurement;
    private BufferedImage picture;
    private boolean testMailSend;
    private MailService mailService;
    private MeasurementService measurementService;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setMeasurementService(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    public void savePicture(){
        previousMeasurement = measurementService.getLastMeasurement();
        try {
            if ((measurement.getActualWeight() < (previousMeasurement.getActualWeight() - 1)) || testMailSend) {
                if (!testMailSend) {
                    measurementService.addMeasurement(measurement);
                }
                measurementTime = measurement.getMeasurementTime();
                path = "/home/pi/camera/" + measurementTime.substring(0, 8) + "/" + measurementTime.substring(9) + ".jpg";
                File directory = new File("/home/pi/camera/" + measurementTime.substring(0, 8));
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                ImageIO.write(picture, "jpg", new File(path));
                mailService.sendMail(path, measurement, previousMeasurement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PictureSaver(){}

    public PictureSaver(Measurement measurement) {
        this(MotionDetector.getPicture(), measurement, measurement, true);
    }
    public PictureSaver(BufferedImage picture, Measurement measurement, Measurement previousMeasurement, boolean testMailSend) {
        this.picture = picture;
        this.measurement = measurement;
        this.testMailSend = testMailSend;
        this.previousMeasurement = previousMeasurement;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public void setPreviousMeasurement(Measurement previousMeasurement) {
        this.previousMeasurement = previousMeasurement;
    }

    public void setPicture(BufferedImage picture) {
        this.picture = picture;
    }

    public void setTestMailSend(boolean testMailSend) {
        this.testMailSend = testMailSend;
    }


}