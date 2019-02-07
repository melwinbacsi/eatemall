package malna314.springfeeder.service;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.GpioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
@Async
public class LoadCell {

    LoadCell(){
        GpioUtil.enableNonPrivilegedAccess();
        final GpioController gpio = GpioFactory.getInstance();
    }
    @Value("${pi4j.HX711.pinDAT}")
    private GpioPinDigitalOutput pinHXCLK;

    @Value("${pi4j.HX711.pinCLK}")
    private GpioPinDigitalInput pinHXDAT;

    public HX711 hx711;

    @Autowired()
    public void setHx711(HX711 hx711){
        hx711.setGain(128);
        hx711.setPinDAT(pinHXDAT);
        hx711.setPinCLK(pinHXCLK);
        this.hx711=hx711;
    }

    public int getWeight() {
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