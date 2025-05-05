package br.app.tads.clinica_facil.model;

import br.app.tads.clinica_facil.model.enums.StatusConsultation;
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
    @Column(name = "dateTime")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusConsultation status;

    public Consultation(Long id, LocalDateTime dateTime, Long patientId, Long doctorId, String specialty, 
                        Report report, MedicalRecord medicalRecord, StatusConsultation status) {
        this.id = id;
        this.dateTime = dateTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.specialty = specialty;
        this.report = report;
        this.medicalRecord = medicalRecord;
        this.status = status;
    }

    public Consultation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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

    public StatusConsultation getStatus() {
        return status;
    }

    public void setStatus(StatusConsultation status) {
        this.status = status;
    }
}
