package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.service.MedicalRecordService;
import br.app.tads.clinica_facil.service.PatientService;


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

    @PostMapping("/create/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<MedicalRecord> createMedicalRecordAndAddConsultation(
            @PathVariable Long patientId,
            @RequestBody Consultation consultation) {

        Optional<Patient> optionalPatient = patientService.getPatientById(patientId);

        if (optionalPatient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            // Cria o prontuário
            MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(optionalPatient.get());

            // Adiciona a consulta
            MedicalRecord updatedRecord = medicalRecordService.addConsultationToMedicalRecord(
                    medicalRecord.getId(),
                    consultation
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(updatedRecord);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // paciente já possui prontuário
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
