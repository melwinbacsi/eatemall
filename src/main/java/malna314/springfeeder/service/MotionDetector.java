package malna314.springfeeder.service;

import malna314.springfeeder.entity.Measurement;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;


import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class MotionDetector {

    private volatile boolean motionDetectorStopped = false;
    private BufferedImage picture;
    private BufferedImage capturedPic = null;
    private MeasurementService measurementService;
    private LoadCell loadCell;
    private PictureSaver pictureSaver;
    private PirSensor pirSensor;

    @Autowired
    public void setMeasurementService(MeasurementService measurementService){
        this.measurementService = measurementService;
    }

    @Autowired
    public void setLoadCell(LoadCell loadCell){
        this.loadCell = loadCell;
    }

    @Autowired
    public void setPictureSaver(PictureSaver pictureSaver){
        this.pictureSaver = pictureSaver;
    }

    @Autowired
    public void setPirSensor(PirSensor pirSensor){
        this.pirSensor = pirSensor;
    }

    void setCapturedPic(BufferedImage capturedPic) {

        this.capturedPic = capturedPic;
    }

    BufferedImage getCapturedPic() {
        return capturedPic;
    }

    BufferedImage getPicture() {

        return picture;
    }

    public void setPicture(BufferedImage picture) {

        this.picture = picture;
    }

    public boolean isMotionDetectorStopped() {

        return motionDetectorStopped;
    }

    public void setMotionDetectorStopped(boolean motionDetectorStopped) {
        this.motionDetectorStopped = motionDetectorStopped;
    }


    @Async
    public void detectMotion() throws Exception {
        long time = 0;

        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(-1);
        grabber.start();
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Mat frame = converter.convert(grabber.grab());
        Mat image = null;
        Mat previousImage = null;
        Mat difference = null;
        MatVector contour = new MatVector();
        int detectionCounter = 0;
        boolean captured = false;
        BufferedImage cachedPicture = null;
        while (!isMotionDetectorStopped()) {
            setPicture(new Java2DFrameConverter().getBufferedImage(grabber.grab()));
            GaussianBlur(frame, frame, new Size(3, 3), 0);
            if (image == null) {
                image = new Mat(frame.rows(), frame.cols(), CV_8UC1);
                cvtColor(frame, image, CV_BGR2GRAY);
            } else {
                previousImage = image;
                image = new Mat(frame.rows(), frame.cols(), CV_8UC1);
                cvtColor(frame, image, CV_BGR2GRAY);
            }
            if (difference == null) {
                difference = new Mat(frame.rows(), frame.cols(), CV_8UC1);
            }
            if (previousImage != null) {
                if ((System.currentTimeMillis() / 1000) - time > 60 && System.currentTimeMillis() / 1000 - time < 100 && captured) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    Measurement previousMeasurement = measurementService.getLastMeasurement();
                    Measurement measurement = new Measurement(LocalDateTime.now(), previousMeasurement.getOrigoTime(), loadCell.getWeight(), previousMeasurement.getOrigoWeight(), "");
                    pictureSaver.savePicture(getCapturedPic(), measurement, false);
                    captured = false;
                    cachedPicture = null;
                    pirSensor.setPirDetected(false);
                }

                absdiff(image, previousImage, difference);
                threshold(difference, difference, 45, 255, CV_THRESH_BINARY);
                findContours(difference, contour, CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
                if (contour.size() > 0 && (System.currentTimeMillis() / 1000) - time > 7) {
                    if ((System.currentTimeMillis() / 1000) - time < 15 && pirSensor.isPirDetected()) {
                        detectionCounter++;
                    } else {
                        detectionCounter = 0;
                    }
                    time = System.currentTimeMillis() / 1000;
                    if (detectionCounter >= 3) {
                        if (cachedPicture == null) {
                            cachedPicture = getPicture();
                        }
                        setCapturedPic(cachedPicture);
                        cachedPicture = getPicture();
                        captured = true;
                    }
                }
            }
        }
        grabber.stop();
    }
}