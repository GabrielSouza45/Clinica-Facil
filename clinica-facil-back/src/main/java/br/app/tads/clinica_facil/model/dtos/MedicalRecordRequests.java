package br.app.tads.clinica_facil.model.dtos;

import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.Revenue;

import java.util.List;

public class MedicalRecordRequests {
    private ConsultationDTO consultation;
    private List<Exam> exams;
    private List<Revenue> revenues;

    public ConsultationDTO getConsultation() {
        return consultation;
    }

    public void setConsultation(ConsultationDTO consultation) {
        this.consultation = consultation;
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

