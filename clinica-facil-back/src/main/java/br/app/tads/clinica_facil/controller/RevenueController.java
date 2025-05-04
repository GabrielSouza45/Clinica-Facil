package br.app.tads.clinica_facil.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.model.Revenue;
import br.app.tads.clinica_facil.service.RevenueService;

@RestController
@RequestMapping("/revenue")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    private ResponseBuilder responseBuilder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRevenues() {
        return revenueService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getRevenueById(@PathVariable Long id) {
        return revenueService.getById(id);
    }

    @GetMapping("/by-patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getByPatient(@PathVariable Long patientId) {
        if (patientId == null || patientId <= 0) {
            return responseBuilder.build("ID de paciente inválido.", HttpStatus.BAD_REQUEST);
        }
        return revenueService.getByPatientId(patientId);
    }

    @GetMapping("/by-doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getByDoctor(@PathVariable Long doctorId) {
        if (doctorId == null || doctorId <= 0) {
            return responseBuilder.build("ID de Médico inválido.", HttpStatus.BAD_REQUEST);
        }
        return revenueService.getByDoctorId(doctorId);
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return revenueService.getByDate(date);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> editRevenue(@RequestBody Revenue revenue) {
        return revenueService.edit(revenue);
    }
}
