package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.*;
import br.app.tads.clinica_facil.model.dtos.MedicalRecordRequests;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PostMapping("/{medicalRecordId}/add-exam")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecord> addExamToMedicalRecord(
            @PathVariable Long medicalRecordId,
            @RequestBody Exam exam) {
        try {
            MedicalRecord updatedRecord = medicalRecordService.addExamToMedicalRecord(medicalRecordId, exam);
            return ResponseEntity.ok(updatedRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecord updatedRecord) {
        try {
            MedicalRecord record = medicalRecordService.updateMedicalRecord(id, updatedRecord);
            return ResponseEntity.ok(record);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create-with-data/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecord> createMedicalRecordWithData(
            @PathVariable Long patientId,
            @RequestBody MedicalRecordRequests request) {

        Optional<Patient> optionalPatient = patientService.getPatientById(patientId);
        if (optionalPatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            // Cria o prontuário
            MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(optionalPatient.get());

            // Adiciona a consulta
            Consultation consultation = new Consultation();
            consultation.setDateTime(request.getConsultation().getDateTime());
            consultation.setSpecialty(request.getConsultation().getSpecialty());
            consultation.setStatus(request.getConsultation().getStatus());
            consultation.setReport(request.getConsultation().getReport());

            medicalRecord = medicalRecordService.addConsultationToMedicalRecord(
                    medicalRecord.getId(),
                    consultation
            );

            // Adiciona exames
            for (Exam exam : request.getExams()) {
                medicalRecord = medicalRecordService.addExamToMedicalRecord(
                        medicalRecord.getId(),
                        exam
                );
            }

            // Adiciona receitas
            for (Revenue revenue : request.getRevenues()) {
                medicalRecord = medicalRecordService.addRevenueToMedicalRecord(
                        medicalRecord.getId(),
                        revenue
                );
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecord);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{medicalRecordId}/add-revenue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecord> addRevenueToMedicalRecord(
            @PathVariable Long medicalRecordId,
            @RequestBody Revenue revenue) {
        try {
            MedicalRecord updatedRecord = medicalRecordService.addRevenueToMedicalRecord(medicalRecordId, revenue);
            return ResponseEntity.ok(updatedRecord);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        Optional<MedicalRecord> medicalRecordOpt = medicalRecordService.getMedicalRecordById(id);
        return medicalRecordOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        try {
            medicalRecordService.deleteMedicalRecord(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
