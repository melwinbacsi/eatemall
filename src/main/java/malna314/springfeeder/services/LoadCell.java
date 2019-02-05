package malna314.springfeeder.services;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;

import java.util.Arrays;

public class LoadCell implements Runnable {
    private static HX711 hx;

    @Override
    public void run() {
        final GpioPinDigitalInput pinHXDAT;
        final GpioPinDigitalOutput pinHXCLK;
        GpioUtil.enableNonPrivilegedAccess();

        final GpioController gpio = GpioFactory.getInstance();
        pinHXDAT = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, "HX_DAT", PinPullResistance.OFF);
        pinHXCLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "HX_CLK", PinState.LOW);

        hx = new HX711(pinHXDAT, pinHXCLK, 128);

    }

    public static int getWeight() {
        double[] weightArray = new double[7];
        double weight = 0.0d;
        for (int i = 0; i < 7; i++) {
            weightArray[i] = hx.read();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Arrays.sort(weightArray);
        for (int i = 1; i < 6; i++) {
            weight += weightArray[i];
        }
        return (int) Math.round(weight / 5);
    }
}