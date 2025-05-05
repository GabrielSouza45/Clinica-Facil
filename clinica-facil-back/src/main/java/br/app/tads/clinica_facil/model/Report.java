package br.app.tads.clinica_facil.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.EqualsAndHashCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name = "report")
@Table(name = "report")
@EqualsAndHashCode(of = "id")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

    private Date issueDate;
    private String reasons;// Razões para a consulta
    private String clinicalHistory;// Antecedentes pessoais relevantes(doenças, cirugias, alergias, etc.)
    private String diagnosis;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "report-consultation")
    private Consultation consultation;

    public Report(Long id, Long patientId, Long doctorId, Date issueDate, String reasons, String clinicalHistory,
            String diagnosis, Consultation consultation) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.issueDate = issueDate;
        this.reasons = reasons;
        this.clinicalHistory = clinicalHistory;
        this.diagnosis = diagnosis;
        this.consultation = consultation;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctor) {
        this.doctorId = doctor;
    }
   
    public Report() {
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


    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getReasons() {
        return reasons;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
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
