package malna314.springfeeder.services;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class PirSensor implements Runnable {
    private static boolean pirStop = false;
    private static boolean pirDetected = false;

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

    @Override
    public void run() {
        final GpioController gpioSensor = GpioFactory.getInstance();
        final GpioPinDigitalInput sensor = gpioSensor.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
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