package malna314.springfeeder.service;

import malna314.springfeeder.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Scanner;

@Service
public class Menu {
    private Integer origoWeight;
    private Integer actualWeight;
    private Measurement newMeasurement;
    private MailService mailService;
    private MeasurementService measurementService;
    private LoadCell loadCell;
    private MotionDetector motionDetector;
    private PictureSaver pictureSaver;
    private PirSensor pirSensor;

    @Autowired
    public void setMotionDetector(MotionDetector motionDetector) {
        this.motionDetector = motionDetector;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Autowired
    public void setLoadCell(LoadCell loadCell) {
        this.loadCell = loadCell;
    }

    @Autowired
    public void setMeasurementService(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Autowired
    public void setPictureSaver(PictureSaver pictureSaver) {
        this.pictureSaver = pictureSaver;

    }

    @Autowired
    public void setPirSensor(PirSensor pirSensor) {
        this.pirSensor = pirSensor;
    }

    public void menu() {
        pirSensor.pir();
            try {
              motionDetector.detectMotion();
        } catch (Exception e) {
          e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nw - set weight\nr - read weight\ne - exit");
            char r = scanner.next().charAt(0);
            if (r == 'p' || r == 'c' || r == 'e' || r == 't' || r == 'w' || r == 'r') {
                switch (r) {
                    case 't': {
                        if (measurementService.getLastMeasurement() == null) {
                            addNewMeasurement();
                        }
                        pictureSaver.savePicture(motionDetector.getPicture(), measurementService.getLastMeasurement(), true);
                        break;
                    }
                    case 'w': {
                        actualWeight = loadCell.getWeight();
                        addNewMeasurement();
                        break;
                    }
                    case 'r': {
                        if (measurementService.getLastMeasurement() == null) {
                            addNewMeasurement();
                        }
                        origoWeight = measurementService.getLastMeasurement().getOrigoWeight();
                        actualWeight = loadCell.getWeight();
                        System.out.println("A kezdő súly: " + origoWeight + "g");
                        System.out.println("Az aktuális súly: " + actualWeight + "g");
                        System.out.println("Teljes fogyás: " + (origoWeight - actualWeight) + "g");
                        break;
                    }
                    case 'e': {
                        if (!pirSensor.isPirStop()) {
                            pirSensor.setPirStop(true);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }
                        if (!motionDetector.isMotionDetectorStopped()) {
                            motionDetector.setMotionDetectorStopped(true);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }
                        System.exit(0);
                    }
                }
            }
        }
    }

    private void addNewMeasurement() {
        newMeasurement = new Measurement(LocalDateTime.now(), LocalDateTime.now(), actualWeight, actualWeight, "");
        measurementService.addMeasurement(newMeasurement);
    }
}