package malna314.springfeeder.repository;

import malna314.springfeeder.entity.Measurement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Long> {
    Measurement findFirstByOrderByMeasurementTimeDesc();
    List<Measurement> findAll();
}
