package br.app.tads.clinica_facil.service;

import java.util.ArrayList;
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

    public ResponseEntity<?> getPatientReport(Patient patient) {
        Optional<Patient> optionalPatient = patientRepository.findById(patient.getId());
        if (optionalPatient.isEmpty()) {
            return responseBuilder.build("Paciente informado não encontrado.", HttpStatus.BAD_REQUEST);
        }

        List<Report> reports = reportRepository.findByPatientId(patient.getId());
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
        if (validationResponse != null) return validationResponse;

        Report saved = reportRepository.save(report);
        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<?> edit(Report report) {
        if (report.getId() == null) {
            return responseBuilder.build("ID do relatório não informado.", HttpStatus.BAD_REQUEST);
        }

        Optional<Report> optionalReport = reportRepository.findById(report.getId());
        if (optionalReport.isEmpty()) {
            return responseBuilder.build("Relatório não encontrado.", HttpStatus.NOT_FOUND);
        }

        ResponseEntity<?> validationResponse = validateDoctorAndPatient(report);
        if (validationResponse != null) return validationResponse;

        Report editable = optionalReport.get();
        editable.setClinicalHistory(report.getClinicalHistory());
        editable.setDiagnosis(report.getDiagnosis());
        editable.setDoctor(report.getDoctor());
        editable.setIssueDate(report.getIssueDate());
        editable.setPatient(report.getPatient());
        editable.setReasons(report.getReasons());
        editable.setRevenues(report.getRevenues() != null ? report.getRevenues() : new ArrayList<>());
        editable.setExams(report.getExams() != null ? report.getExams() : new ArrayList<>());

        reportRepository.save(editable);
        return ResponseEntity.ok(editable);
    }

    
    private ResponseEntity<?> validateDoctorAndPatient(Report report) {
        if (report.getPatient() == null || report.getPatient().getId() == null ||
            report.getDoctor() == null || report.getDoctor().getId() == null) {
            return responseBuilder.build("Paciente ou Médico não informado corretamente.", HttpStatus.BAD_REQUEST);
        }

        Optional<Patient> optionalPatient = patientRepository.findById(report.getPatient().getId());
        Optional<Doctor> optionalDoctor = doctorRepository.findById(report.getDoctor().getId());

        if (optionalPatient.isEmpty() || optionalDoctor.isEmpty()) {
            return responseBuilder.build("Paciente ou Médico não encontrado.", HttpStatus.BAD_REQUEST);
        }

        report.setPatient(optionalPatient.get());
        report.setDoctor(optionalDoctor.get());
        return null;
    }
}
