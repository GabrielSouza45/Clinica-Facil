package br.app.tads.clinica_facil.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;
import br.app.tads.clinica_facil.repository.ReportRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ResponseBuilder responseBuilder;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    public ResponseEntity<?> getPatientReport(Long patientId) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isEmpty()) {
            return responseBuilder.build("Paciente informado não encontrado.", HttpStatus.BAD_REQUEST);
        }

        List<Report> reports = reportRepository.findByPatientId(patientId);
        if (reports.isEmpty()) {
            return responseBuilder.build("Nenhum relatório encontrado para o paciente.", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(reports);
    }

    public ResponseEntity<?> getReportByDate(Date issueDate) {
        List<Report> reports = reportRepository.findByIssueDate(issueDate);
        if (reports.isEmpty()) {
            return responseBuilder.build("Nenhum relatório encontrado para a data informada.", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(reports);
    }

    public ResponseEntity<?> getAll() {
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()) {
            return responseBuilder.build("Não há relatórios cadastrados no sistema.", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(reports);
    }

    public ResponseEntity<?> add(Report report) {
        ResponseEntity<?> validationResponse = validateDoctorAndPatient(report);
        if (validationResponse != null)
            return validationResponse;

        Report saved = reportRepository.save(report);
        return ResponseEntity.ok(saved);
    }

    public Report updateReport(Long reportId, Report updatedReport) {
        Report existingReport = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Relatório não encontrado com ID: " + reportId));

        existingReport.setIssueDate(updatedReport.getIssueDate());
        existingReport.setReasons(updatedReport.getReasons());
        existingReport.setClinicalHistory(updatedReport.getClinicalHistory());
        existingReport.setDiagnosis(updatedReport.getDiagnosis());

        return reportRepository.save(existingReport);
    }

    private ResponseEntity<?> validateDoctorAndPatient(Report report) {
        if (report.getPatientId() == null || report.getDoctorId() == null) {
            return responseBuilder.build("Paciente ou Médico não informado corretamente.", HttpStatus.BAD_REQUEST);
        }

        Optional<Patient> optionalPatient = patientRepository.findById(report.getPatientId());
        Optional<Doctor> optionalDoctor = doctorRepository.findById(report.getDoctorId());

        if (optionalPatient.isEmpty() || optionalDoctor.isEmpty()) {
            return responseBuilder.build("Paciente ou Médico não encontrado.", HttpStatus.BAD_REQUEST);
        }

        report.setPatientId(optionalPatient.get().getId());
        report.setDoctorId(optionalDoctor.get().getId());
        return null;
    }
}
