package malna314.springfeeder.service;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PirSensor{
    private volatile boolean pirStop = false;
    private  volatile boolean pirDetected = false;

    public boolean isPirDetected() {
        return pirDetected;
    }

    public void setPirDetected(boolean pirDetected) {

        this.pirDetected = pirDetected;
    }

    public boolean isPirStop() {
        return pirStop;
    }

    public void setPirStop(boolean pirStop) {

        this.pirStop = pirStop;
    }

    @Async
    public void pir() {
        final GpioController gpioSensor = GpioFactory.getInstance();
        final GpioPinDigitalInput sensor = gpioSensor.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN);
        sensor.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

                if (event.getState().isHigh()) {
                    setPirDetected(true);
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setPirDetected(false);
                }

            }
        });
        System.out.println("pir aktiv");
        try {
            while (!isPirStop()) {
                Thread.sleep(500);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}