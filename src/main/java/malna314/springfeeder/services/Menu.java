package malna314.springfeeder.services;

import malna314.springfeeder.entities.Measurement;
import malna314.springfeeder.repository.DB;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Component
@Scope("singleton")
public class Menu {
    private int origoWeight;
    private int actualWeight;
    private Measurement measurement;

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        MailService ms = new MailService();
        Thread ps = new Thread(new PirSensor());
        ps.start();
        Thread md = new Thread(new MotionDetector());
        md.start();
        Thread lc = new Thread(new LoadCell());
        lc.start();
        int origoWeight = 0;
        int actualWeight = 0;
        while (true) {
            System.out.println("\np - set new password\nc - check password\nw - set weight\nr - read weight\ne - exit");
            char r = scanner.next().charAt(0);
            if (r == 'p' || r == 'c' || r == 'e' ||  r == 't' || r == 'w' || r == 'r') {
                switch (r) {
                case 'p': {
                    ms.createPropFile(ms.passReader());
                    break;
                }
                case 'c': {
                    String check = ms.passChecker();
                    System.out.println(check);
                    break;
                }
                case 't': {
                    measurement = DB.getMeasurement(-1);
                    if(measurement==null){
                        addNewMeasurement();
                    }
                    Thread pst = new Thread(new PictureSaver(measurement));
                    pst.start();
                    break;
                }
                case 'w': {
                    actualWeight = LoadCell.getWeight();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    String time = LocalDateTime.now().format(dtf);
                    Measurement measurement = new Measurement(time, time, actualWeight, actualWeight);
                    DB.addMeasurement(measurement);
                    break;
                }
                case 'r': {
                    measurement = DB.getMeasurement(-1);
                    if(measurement==null){
                        addNewMeasurement();
                    }
                    origoWeight = measurement.getOrigoWeight();
                    actualWeight = LoadCell.getWeight();
                    System.out.println("A kezdő súly: " + origoWeight + "g");
                    System.out.println("Az aktuális súly: " + actualWeight + "g");
                    System.out.println("Teljes fogyás: " + (origoWeight - actualWeight) + "g");
                    break;
                }
                case 'e': {
                    if (!PirSensor.isPirStop()) {
                        PirSensor.setPirStop(true);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                    }
                    if (!MotionDetector.isMotionDetectorStopped()) {
                        MotionDetector.setMotionDetectorStopped(true);
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
    private void addNewMeasurement(){
        actualWeight = LoadCell.getWeight();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String time = LocalDateTime.now().format(dtf);
        measurement = new Measurement(time, time, actualWeight, actualWeight);
        DB.addMeasurement(measurement);

    }
}