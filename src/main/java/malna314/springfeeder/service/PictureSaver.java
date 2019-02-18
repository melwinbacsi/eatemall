package malna314.springfeeder.service;


import malna314.springfeeder.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PictureSaver {
    private String path;
    private Measurement previousMeasurement;
    private MailService mailService;
    private MeasurementService measurementService;
    @Value("${picture.directory}")
    private String dir;
    private String filename;

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setMeasurementService(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Async
    public void savePicture(BufferedImage picture, Measurement measurement, boolean testMailSend) {
        previousMeasurement = measurementService.getLastMeasurement();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        filename = measurement.getMeasurementTime().format(dtf) + ".jpg";
        measurement.setFileName(filename);

        try {
            if ((measurement.getActualWeight() < (previousMeasurement.getActualWeight() - 1)) || testMailSend) {
                if (!testMailSend) {
                    measurementService.addMeasurement(measurement);
                }
                path = dir + filename.substring(0, 8) + "/" + filename;
                File directory = new File(dir + filename.substring(0, 8));
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                ImageIO.write(picture, "jpg", new File(path));
                mailService.sendMessage(measurement, previousMeasurement, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
