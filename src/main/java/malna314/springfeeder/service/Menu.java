package malna314.springfeeder.service;

import malna314.springfeeder.entity.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
@Async
public class Menu {
    private int origoWeight;
    private int actualWeight;
    private Measurement newMeasurement;
    private MailService mailService;
    private MeasurementService measurementService;
    private LoadCell loadCell;
    private MotionDetector motionDetector;

    @Autowired
    public void setMotionDetector(MotionDetector motionDetector){
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

    public void menu() {

        Scanner scanner = new Scanner(System.in);
                        int origoWeight=0;
                        int actualWeight=0;
                        while(true){
                        System.out.println("\np - set new password\nc - check password\nw - set weight\nr - read weight\ne - exit");
                        char r=scanner.next().charAt(0);
                        if(r=='p'||r=='c'||r=='e'||r=='t'||r=='w'||r=='r'){
                        switch(r){
                        case'p':{
                        mailService.sendMessage();
                        break;
                        }
                        case'c':{
                        break;
                        }
                        case't':{
                        measurement=DB.getMeasurement(-1);
                        if(measurement==null){
                        addNewMeasurement();
                        }
                        Thread pst=new Thread(new PictureSaver(measurement));
                        pst.start();
                        break;
                        }
                        case'w':{
                        actualWeight=LoadCell.getWeight();
                        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        String time=LocalDateTime.now().format(dtf);
                        Measurement measurement=new Measurement(time,time,actualWeight,actualWeight);
                        DB.addMeasurement(measurement);
                        break;
                        }
                        case'r':{
                        measurement=DB.getMeasurement(-1);
                        if(measurement==null){
                        addNewMeasurement();
                        }
                        origoWeight=measurement.getOrigoWeight();
                        actualWeight=LoadCell.getWeight();
                        System.out.println("A kezdő súly: "+origoWeight+"g");
                        System.out.println("Az aktuális súly: "+actualWeight+"g");
                        System.out.println("Teljes fogyás: "+(origoWeight-actualWeight)+"g");
                        break;
                        }
                        case'e':{
                        if(!PirSensor.isPirStop()){
                        PirSensor.setPirStop(true);
                        try{
                        Thread.sleep(500);
                        }catch(InterruptedException e){
                        }
                        }
                        if(!MotionDetector.isMotionDetectorStopped()){
                        MotionDetector.setMotionDetectorStopped(true);
                        try{
                        Thread.sleep(500);
                        }catch(InterruptedException e){
                        }
                        }
                        System.exit(0);
                        }
                        }
                        }
                        }
                        }
private void addNewMeasurement(){
                actualWeight=load.getWeight();
                DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String time=LocalDateTime.now().format(dtf);
                measurement=new Measurement(time,time,actualWeight,actualWeight);
                DB.addMeasurement(measurement);

                }
                }