package br.app.tads.clinica_facil.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "consultation")
@EqualsAndHashCode(of = "id")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime; // Data e horário da consulta

    private Long patientId;

    private Long doctorId;

    private String specialty; // Especialidade da consulta (ex: "Cardiologia")

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    @JsonBackReference
    private MedicalRecord medicalRecord; 

    public Consultation(Long id, Date dateTime, long patientId, long doctorId, String specialty, Report report,
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
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
