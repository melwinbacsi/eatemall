package malna314.springfeeder.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "meastime")
    private LocalDateTime measurementTime;
    @Column(name = "actualweight")
    private Integer actualWeight;
    @Column(name = "origotime")
    private LocalDateTime origoTime;
    @Column(name = "origoweight")
    private Integer origoWeight;
    @Column(name = "filename")
    private String fileName;

    public Measurement(){}

    public Measurement(LocalDateTime measurementTime, LocalDateTime origoTime, int actualWeight, int origoWeight) {
        this.measurementTime = measurementTime;
        this.origoTime = origoTime;
        this.actualWeight = actualWeight;
        this.origoWeight = origoWeight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(LocalDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public Integer getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Integer actualWeight) {
        this.actualWeight = actualWeight;
    }

    public LocalDateTime getOrigoTime() {
        return origoTime;
    }

    public void setOrigoTime(LocalDateTime origoTime) {
        this.origoTime = origoTime;
    }

    public Integer getOrigoWeight() {
        return origoWeight;
    }

    public void setOrigoWeight(Integer origoWeight) {
        this.origoWeight = origoWeight;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Measurement{" + "id=" + id + ", measurementTime=" + measurementTime + ", actualWeight=" + actualWeight + ", origoTime=" + origoTime
                        + ", origoWeight=" + origoWeight + '}';
    }
}