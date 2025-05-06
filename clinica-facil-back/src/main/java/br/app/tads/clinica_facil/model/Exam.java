package br.app.tads.clinica_facil.model;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "exam")
@EqualsAndHashCode(of = "id")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; 
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    private String results; 

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    @JsonBackReference(value = "medicalRecord-exam")
    private MedicalRecord medicalRecord;

    public Exam(Long id, String name, String description, LocalDateTime dateTime, String results, Patient patient,
            MedicalRecord medicalRecord) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
        this.results = results;
        this.patient = patient;
        this.medicalRecord = medicalRecord;
    }

    public Exam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
    
}
