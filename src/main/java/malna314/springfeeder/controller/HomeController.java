package malna314.springfeeder.controller;

import malna314.springfeeder.entity.Measurement;
import malna314.springfeeder.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private MeasurementService measurementService;
    private String dir;

    @Autowired
    public void setMeasurementService(MeasurementService measurementService){
        this.measurementService=measurementService;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String story(Model model) {
        List<Measurement> measurements = new ArrayList<>(measurementService.getMeasurementsSinceLastFeeding());
        dir = "/home/pi/camera/"+measurementService.getLastMeasurement().getFileName().substring(0, 8) + "/";
        model.addAttribute("measurementList", measurements);
        model.addAttribute("path", dir);
            return "measurements";
    }
}
