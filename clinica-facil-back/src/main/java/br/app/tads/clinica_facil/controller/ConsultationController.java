package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.service.ConsultationService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/consultations")
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
    public ResponseEntity<?> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {

        if (start == null || end == null) {
            return ResponseEntity.badRequest().body("As datas de início e fim são obrigatórias.");
        }

        if (start.after(end)) {
            return ResponseEntity.badRequest().body("A data de início não pode ser posterior à data de fim.");
        }

        return consultationService.getConsultationsByDateRange(start, end);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> update(@RequestBody Consultation consultation) {
        if (consultation == null || consultation.getId() == null) {
            return ResponseEntity.badRequest().body("Consulta inválida para atualização.");
        }
        return consultationService.updateConsultation(consultation);
    }

    @PostMapping("/{medicalRecordId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addConsultation(
            @PathVariable Long medicalRecordId,
            @RequestBody Consultation consultation) {

        System.out.println("Chegou na API de adicionar consulta");
        if (consultation.getDateTime() == null ||
                consultation.getPatientId() == null ||
                consultation.getDoctorId() == null ||
                consultation.getSpecialty() == null || consultation.getSpecialty().isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Campos obrigatórios ausentes na consulta.");
        }

        try {
            MedicalRecord updatedMedicalRecord = consultationService.addConsultation(medicalRecordId, consultation);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(updatedMedicalRecord);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao adicionar consulta.");
        }
    }

}
