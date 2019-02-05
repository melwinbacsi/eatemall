package malna314.springfeeder.services;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;


public class HX711 {

    private final GpioPinDigitalOutput pinCLK;
    private final GpioPinDigitalInput pinDAT;
    private int gain;

    private long emptyValue = 8122210;
    private double emptyWeight = 0.0d;
    private long calibrationValue = 7721060;
    private double calibrationWeight = 450.0d;


    private double weight = 0.0d;
    private long value = 0;

    public HX711(GpioPinDigitalInput pinDAT, GpioPinDigitalOutput pinSCK, int gain) {
        this.pinCLK = pinSCK;
        this.pinDAT = pinDAT;
        setGain(gain);
    }

    public double read() {
        pinCLK.setState(PinState.LOW);
        while (!isReady()) {
            sleep(1);
        }

        long count = 0;
        for (int i = 0; i < this.gain; i++) {
            pinCLK.setState(PinState.HIGH);
            count = count << 1;
            pinCLK.setState(PinState.LOW);
            if (pinDAT.isHigh()) {
                count++;
            }
        }

        pinCLK.setState(PinState.HIGH);
        count = count ^ 0x800000;
        pinCLK.setState(PinState.LOW);
        value = count;

        weight = (value - emptyValue)*((calibrationWeight - emptyWeight)/(calibrationValue - emptyValue));
        return weight;
    }

    public void setGain(int gain) {
        switch (gain) {
        case 128:       // channel A, gain factor 128
            this.gain = 24;
            break;
        case 64:        // channel A, gain factor 64
            this.gain = 26;
            break;
        case 32:        // channel B, gain factor 32
            this.gain = 25;
            break;
        }

        pinCLK.setState(PinState.LOW);
        read();
    }

    public boolean isReady() {
        return (pinDAT.isLow());
    }

    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception ex) {
        }
    }
}