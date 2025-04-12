package br.app.tads.clinica_facil.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.model.Revenue;
import br.app.tads.clinica_facil.service.RevenueService;

@RestController
@RequestMapping("/revenue")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllRevenues() {
        return revenueService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getRevenueById(@PathVariable Long id) {
        return revenueService.getById(id);
    }

    @PostMapping("/by-patient")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getByPatient(@RequestBody Patient patient) {
        return revenueService.getByPatient(patient);
    }

    @PostMapping("/by-doctor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getByDoctor(@RequestBody Doctor doctor) {
        return revenueService.getByDoctor(doctor);
    }

    @PostMapping("/by-report")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getByReport(@RequestBody Report report) {
        return revenueService.getByReport(report);
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getByDate(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return revenueService.getByDate(date);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createRevenue(@RequestBody Revenue revenue) {
        return revenueService.add(revenue);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> editRevenue(@RequestBody Revenue revenue) {
        return revenueService.edit(revenue);
    }
}
