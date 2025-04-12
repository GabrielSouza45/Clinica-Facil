package br.app.tads.clinica_facil.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

//<---------------PRONTUARIO--------------->//
//<---------------PRONTUARIO--------------->//
//<---------------PRONTUARIO--------------->//


@Entity(name = "medical_record")
@Table(name = "medical_record")
@EqualsAndHashCode(of = "id")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id", unique = true)
    private Patient patient;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>(); 

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams = new ArrayList<>(); 

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Revenue> revenues = new ArrayList<>(); 

    public MedicalRecord() {}

    public MedicalRecord(Patient patient) {
        this.patient = patient;
        this.consultations = new ArrayList<>();
        this.exams = new ArrayList<>();
        this.revenues = new ArrayList<>();
    }

    public MedicalRecord(Long id, Patient patient, List<Consultation> consultations, List<Exam> exams, List<Revenue> revenues) {
        this.id = id;
        this.patient = patient;
        this.consultations = (consultations != null) ? consultations : new ArrayList<>();
        this.exams = (exams != null) ? exams : new ArrayList<>();
        this.revenues = (revenues != null) ? revenues : new ArrayList<>();
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

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Revenue> getRevenues() {
        return revenues;
    }

    public void setRevenues(List<Revenue> revenues) {
        this.revenues = revenues;
    }
}
