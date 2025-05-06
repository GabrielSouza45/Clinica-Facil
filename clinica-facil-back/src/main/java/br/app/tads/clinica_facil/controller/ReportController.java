package br.app.tads.clinica_facil.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.service.PatientService;
import br.app.tads.clinica_facil.service.ReportService;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private ResponseBuilder responseBuilder;
    @Autowired
    PatientService patientService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllReports() {
        return reportService.getAll();
    }

    @GetMapping("/by-patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getReportByPatient(@RequestParam Long patientId) {
        if (patientId == null) {
            return responseBuilder.build("É obrigatório informar um paciente para realizar uma busca!",
                    HttpStatus.BAD_REQUEST);
        }

        return reportService.getPatientReport(patientId);
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getReportAtDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date issueDate) {
        if (issueDate.after(new Date())) {
            return responseBuilder.build("A data informada não pode estar no futuro.", HttpStatus.BAD_REQUEST);
        }

        return reportService.getReportByDate(issueDate);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Report> updateReport(@PathVariable Long id, @RequestBody Report updatedReport) {
        Report report = reportService.updateReport(id, updatedReport);
        return ResponseEntity.ok(report);
    }

}
