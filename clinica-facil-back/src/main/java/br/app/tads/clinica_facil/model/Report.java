package br.app.tads.clinica_facil.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private String reasons;//Razões para a consulta
    private String clinicalHistory;//Antecedentes pessoais relevantes(doenças, cirugias, alergias, etc.)
    private String diagnosis;
    
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Revenue> revenues = new ArrayList<>();//Medicamentos prescritos, procedimentos realizados ou recomendados, afastamento (se necessário), com CID e período, ancaminhamentos (ex: para especialistas)

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>();

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private Consultation consultation;
    
    public Report(Long id, Patient patient, Doctor doctor, Date issueDate, String reasons, String clinicalHistory,
        String diagnosis, Consultation consultation, List<Revenue> revenues, List<Exam> exams) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.issueDate = issueDate;
        this.reasons = reasons;
        this.clinicalHistory = clinicalHistory;
        this.diagnosis = diagnosis;
        this.consultation = consultation;
        this.revenues = (revenues != null) ? revenues : new ArrayList<>();
        this.exams = (exams != null) ? exams : new ArrayList<>();
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

    public List<Revenue> getRevenues() {
        return revenues;
    }

    public void setRevenues(List<Revenue> revenues) {
        this.revenues = revenues;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }    

    

}
