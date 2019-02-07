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
import java.time.format.DateTimeFormatter;

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

    public void savePicture(Measurement measurement){
        previousMeasurement = measurementService.getLastMeasurement();
        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        measurement.setFileName(measurement.getMeasurementTime().format(dtf));

        try {
            if ((measurement.getActualWeight() < (previousMeasurement.getActualWeight() - 1)) || testMailSend) {
                if (!testMailSend) {
                    measurementService.addMeasurement(measurement);
                }
                measurementTime = measurement.getMeasurementTime();
                path = "/home/pi/camera/" + measurement.getFileName() + ".jpg";
                File directory = new File("/home/pi/camera/" + measurement.getFileName().substring(0, 8));
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                ImageIO.write(picture, "jpg", new File(path));
                mailService.sendMessage(measurement, previousMeasurement);
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
}