package br.app.tads.clinica_facil.model.dtos;

import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.model.enums.StatusConsultation;

import java.time.LocalDateTime;

public class ConsultationDTO {
    private LocalDateTime dateTime;
    private String specialty;
    private StatusConsultation status;
    private Report report;

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

    public StatusConsultation getStatus() {
        return status;
    }

    public void setStatus(StatusConsultation status) {
        this.status = status;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
