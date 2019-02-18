package malna314.springfeeder.service;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Future;


@Service
public class LoadCell {

    final GpioPinDigitalInput pinHXDAT;
    final GpioPinDigitalOutput pinHXCLK;
    HX711 hx711;

    LoadCell(){

        GpioUtil.enableNonPrivilegedAccess();
        final GpioController gpio = GpioFactory.getInstance();
        pinHXDAT = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, "HX_DAT", PinPullResistance.OFF);
        pinHXCLK = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "HX_CLK", PinState.LOW);
    }


    public int getWeight() {

        hx711 = new HX711(pinHXDAT, pinHXCLK, 128);
        double[] weightArray = new double[7];
        double weight = 0.0d;
        for (int i = 0; i < 7; i++) {
            weightArray[i] = hx711.read();
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