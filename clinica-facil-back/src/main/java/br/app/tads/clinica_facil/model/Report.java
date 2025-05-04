package br.app.tads.clinica_facil.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.EqualsAndHashCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name = "report")
@Table(name = "report")
@EqualsAndHashCode(of = "id")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private Date issueDate;
    private String reasons;// Razões para a consulta
    private String clinicalHistory;// Antecedentes pessoais relevantes(doenças, cirugias, alergias, etc.)
    private String diagnosis;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "report-consultation")
    private Consultation consultation;

    public Report(Long id, Patient patient, Doctor doctor, Date issueDate, String reasons, String clinicalHistory,
            String diagnosis, Consultation consultation) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.issueDate = issueDate;
        this.reasons = reasons;
        this.clinicalHistory = clinicalHistory;
        this.diagnosis = diagnosis;
        this.consultation = consultation;
    }

    public Report() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public String getClinicalHistory() {
        return clinicalHistory;
    }

    public void setClinicalHistory(String clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }



}
