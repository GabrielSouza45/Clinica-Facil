package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/consultas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> getAll() {
        return consultationService.getAllConsultations();
    }

    @GetMapping("/by-patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> getByPatient(@RequestParam Long patientId) {
        if (patientId == null) {
            return ResponseEntity.badRequest().body("É obrigatório informar o ID do paciente.");
        }
        return consultationService.getConsultationsByPatient(patientId);
    }

    @GetMapping("/by-doctor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> getByDoctor(@RequestParam Long doctorId) {
        if (doctorId == null) {
            return ResponseEntity.badRequest().body("É obrigatório informar o ID do médico.");
        }
        return consultationService.getConsultationsByDoctor(doctorId);
    }

    @GetMapping("/by-specialty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBySpecialty(@RequestParam String specialty) {
        if (specialty == null || specialty.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("É obrigatório informar a especialidade.");
        }
        return consultationService.getConsultationsBySpecialty(specialty.trim());
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getConsultByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime dateTime) {
        return consultationService.getConsultByDate(dateTime);
    }

    @PutMapping("/cancel/{consultationId}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> cancelConsultation(@PathVariable Long consultationId, @RequestBody Report report) {
        return consultationService.cancelConsultation(consultationId, report);
    }

    @PutMapping("/finalize/{consultationId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> addReportAndFinalizeConsultation(@PathVariable Long consultationId,
                                                              @RequestBody Report report) {
        return consultationService.addReportAndFinalizeConsultation(consultationId, report);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> editConsultation(@PathVariable Long id, @RequestBody Consultation updatedConsultation) {
        return consultationService.updateConsultation(id, updatedConsultation);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> createConsultation(@RequestBody Consultation consultation) {
        return consultationService.createConsultation(consultation);
    }

}
