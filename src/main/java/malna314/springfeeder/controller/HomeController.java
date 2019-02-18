package malna314.springfeeder.controller;

import malna314.springfeeder.entity.Measurement;
import malna314.springfeeder.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
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

    @RequestMapping("/")
    public String showAll(Model model) {
        dir = "/home/pi/camera/"+measurementService.getLastMeasurement().getFileName().substring(0, 8) + "/";
        model.addAttribute("path", dir);
        model.addAttribute("measurements", measurementService.getMeasurementsSinceLastFeeding());
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Measurement measurement = measurementService.findById(id);
        model.addAttribute("measurement", measurement);
        return "measurement";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid Measurement measurement, BindingResult result, Model model) {
        if (result.hasErrors()) {
            measurement.setId(id);
            return "measurement";
        }

        measurementService.addMeasurement(measurement);
        model.addAttribute("measurements", measurementService.getMeasurementsSinceLastFeeding());
        return "index";
    }


}
