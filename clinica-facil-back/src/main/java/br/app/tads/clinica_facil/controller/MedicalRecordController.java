package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.Revenue;
import br.app.tads.clinica_facil.service.MedicalRecordService;
import br.app.tads.clinica_facil.service.PatientService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    MedicalRecordController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/by-patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPatient(@PathVariable Long patientId) {
        Optional<Patient> optionalPatient = patientService.getPatientById(patientId);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecordsByPatient(patient);
            return ResponseEntity.ok(medicalRecords);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        Optional<MedicalRecord> medicalRecordOpt = medicalRecordService.getMedicalRecordById(id);
        return medicalRecordOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/{medicalRecordId}/consultations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalRecord> addConsultationToMedicalRecord(
            @PathVariable Long medicalRecordId, @RequestBody Consultation consultation) {
                System.out.println("BATEUUUUU");
        try {
            MedicalRecord updatedMedicalRecord = medicalRecordService.addConsultationToMedicalRecord(medicalRecordId, consultation);
            return ResponseEntity.ok(updatedMedicalRecord);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }     
    }

    @PostMapping("/{medicalRecordId}/exams")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalRecord> addExamToMedicalRecord(
            @PathVariable Long medicalRecordId, @RequestBody Exam exam) {
        try {
            MedicalRecord updatedMedicalRecord = medicalRecordService.addExamToMedicalRecord(medicalRecordId, exam);
            return ResponseEntity.ok(updatedMedicalRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{medicalRecordId}/revenues")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalRecord> addRevenueToMedicalRecord(
            @PathVariable Long medicalRecordId, @RequestBody Revenue revenue) {
        try {
            MedicalRecord updatedMedicalRecord = medicalRecordService.addRevenueToMedicalRecord(medicalRecordId, revenue);
            return ResponseEntity.ok(updatedMedicalRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        try {
            medicalRecordService.deleteMedicalRecord(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
