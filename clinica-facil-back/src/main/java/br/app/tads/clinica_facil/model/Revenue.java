package br.app.tads.clinica_facil.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "revenue")
@EqualsAndHashCode(of = "id")
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medications; // Medicamentos prescritos (Ex: Paracetamol 500mg)
    private String dosage;      // Posologia (Ex: Tomar 1 comprimido a cada 8h)
    private String recommendations; // Outras recomendações (Ex: repouso, alimentação)

    @Temporal(TemporalType.DATE)
    private Date date; // Data da receita

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    @JsonBackReference(value = "medicalRecord-revenue")
    private MedicalRecord medicalRecord;

    public Revenue(Long id, String medications, String dosage, String recommendations, Date date, Doctor doctor,
            Patient patient, MedicalRecord medicalRecord) {
        this.id = id;
        this.medications = medications;
        this.dosage = dosage;
        this.recommendations = recommendations;
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
        this.medicalRecord = medicalRecord;
    }

    public Revenue() {
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
