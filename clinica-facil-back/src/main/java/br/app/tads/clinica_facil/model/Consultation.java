package br.app.tads.clinica_facil.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "consultation")
@EqualsAndHashCode(of = "id")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "birth")
    private LocalDateTime dateTime;

    private Long patientId;

    private Long doctorId;

    private String specialty;

    @OneToOne
    @JoinColumn(name = "report_id")
    @JsonBackReference(value = "report-consultation")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    @JsonBackReference(value = "medicalRecord-consultation")
    private MedicalRecord medicalRecord;

    public Consultation(Long id, LocalDateTime dateTime, Long patientId, Long doctorId, String specialty, Report report,
            MedicalRecord medicalRecord) {
        this.id = id;
        this.dateTime = dateTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.specialty = specialty;
        this.report = report;
        this.medicalRecord = medicalRecord;
    }

    public Consultation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

}
