package br.app.tads.clinica_facil.model;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@Table(name = "exam")
@EqualsAndHashCode(of = "id")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Nome do exame (Ex: Hemograma, Raio-X)
    private String description; // Descrição ou observações
    private Date date; // Data do exame
    private String results; // Resultados do exame

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = true)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    public Exam(Long id, String name, String description, Date date, String results, Patient patient, Report report,
            MedicalRecord medicalRecord) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.results = results;
        this.patient = patient;
        this.report = report;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
