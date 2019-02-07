package malna314.springfeeder.service;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PirSensor{
    private static boolean pirStop = false;
    private static boolean pirDetected = false;
    @Value("${pi4j.PIR.sensor}")
    private GpioPinDigitalInput pirSensor;

    public static boolean isPirDetected() {
        return pirDetected;
    }

    public static void setPirDetected(boolean pirDetected) {
        PirSensor.pirDetected = pirDetected;
    }

    public static boolean isPirStop() {
        return pirStop;
    }

    public static void setPirStop(boolean pirStop) {
        PirSensor.pirStop = pirStop;
    }

    public void pir() {
        final GpioController gpioSensor = GpioFactory.getInstance();
        final GpioPinDigitalInput sensor = pirSensor;
        sensor.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                if (event.getState().isHigh()) {
                    PirSensor.setPirDetected(true);
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PirSensor.setPirDetected(false);
                }

            }
        });

        try {
            while (!isPirStop()) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}