package malna314.springfeeder.service;

import malna314.springfeeder.entity.Measurement;
import malna314.springfeeder.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {
    MeasurementRepository measurementRepository;

    @Autowired
    public void setMeasurementRepository(MeasurementRepository measurementRepository){
        this.measurementRepository = measurementRepository;
    }

    public void addMeasurement(Measurement measurement){
        measurementRepository.save(measurement);
    }


    public Measurement getLastMeasurement(){
        return measurementRepository.findFirstByOrderByMeasurementTimeDesc();
    }

    public List<Measurement> searchAllMeasurements(){
        return measurementRepository.findAll();
    }

    public List<Measurement> getMeasurementsSinceLastFeeding() {
        return measurementRepository.findByOrigoTimeEqualsAndFileNameIsNot(getLastMeasurement().getOrigoTime(), "");
    }
    public Measurement findById(Long id){
        return measurementRepository.findByIdis(id);
    }
}
