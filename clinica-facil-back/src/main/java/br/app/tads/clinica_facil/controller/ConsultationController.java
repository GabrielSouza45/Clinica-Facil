package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;


    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> getAll() {
        return consultationService.getAllConsultations();
    }

  
    @PostMapping("/by-patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> getByPatient(@RequestBody Patient patient) {
        return consultationService.getConsultationsByPatient(patient);
    }


    @PostMapping("/by-doctor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getByDoctor(@RequestBody Doctor doctor) {
        return consultationService.getConsultationsByDoctor(doctor);
    }

    @GetMapping("/by-specialty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBySpecialty(@RequestParam String specialty) {
        return consultationService.getConsultationsBySpecialty(specialty);
    }

  
    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {
        return consultationService.getConsultationsByDateRange(start, end);
    }

   
    @PostMapping//Agendar
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> schedule(@RequestBody Consultation consultation) {
        return consultationService.scheduleConsultation(consultation);
    }


    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> update(@RequestBody Consultation consultation) {
        return consultationService.updateConsultation(consultation);
    }
}
