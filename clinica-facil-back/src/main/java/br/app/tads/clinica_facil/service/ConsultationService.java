package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.repository.ConsultationRepository;
import br.app.tads.clinica_facil.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public ResponseEntity<?> getAllConsultations() {
        List<Consultation> list = consultationRepository.findAll();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsByPatient(Long patientId) {
        List<Consultation> list = consultationRepository.findByPatientId(patientId);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada para o paciente informado.");
        }

        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsByDoctor(Long doctorId) {
        List<Consultation> list = consultationRepository.findByDoctorId(doctorId);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada para o médico informado.");
        }

        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsByDateRange(Date start, Date end) {
        List<Consultation> list = consultationRepository.findByDateTimeBetween(start, end);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada no intervalo de datas informado.");
        }

        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsBySpecialty(String specialty) {
        List<Consultation> list = consultationRepository.findBySpecialtyContainingIgnoreCase(specialty);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada para a especialidade informada.");
        }

        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> updateConsultation(Consultation consultation) {
        if (consultation == null || consultation.getId() == null) {
            return responseBuilder.build("Consulta inválida para atualização.", HttpStatus.BAD_REQUEST);
        }

        Optional<Consultation> existingOpt = consultationRepository.findById(consultation.getId());
        if (existingOpt.isEmpty()) {
            return responseBuilder.build("Consulta não encontrada.", HttpStatus.NOT_FOUND);
        }

        if (consultation.getDateTime() == null) {
            return responseBuilder.build("A data e hora da consulta são obrigatórias.", HttpStatus.BAD_REQUEST);
        }
        if (consultation.getPatientId() == null || consultation.getPatientId() <= 0) {
            return responseBuilder.build("ID do paciente inválido.", HttpStatus.BAD_REQUEST);
        }
        if (consultation.getDoctorId() == null || consultation.getDoctorId() <= 0) {
            return responseBuilder.build("ID do médico inválido.", HttpStatus.BAD_REQUEST);
        }
        if (consultation.getSpecialty() == null || consultation.getSpecialty().trim().isEmpty()) {
            return responseBuilder.build("Especialidade da consulta é obrigatória.", HttpStatus.BAD_REQUEST);
        }
        if (consultation.getMedicalRecord() == null || consultation.getMedicalRecord().getId() == null) {
            return responseBuilder.build(
                    "É preciso informar o ID do Prontuário para arquivamento da Consulta e associação com o devido Paciente.",
                    HttpStatus.BAD_REQUEST);
        }

        // Verificando se o MedicalRecord existe no banco de dados
        Optional<MedicalRecord> medicalRecordOpt = medicalRecordRepository
                .findById(consultation.getMedicalRecord().getId());
        if (medicalRecordOpt.isEmpty()) {
            return responseBuilder.build("Prontuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        // Associando o MedicalRecord corretamente
        Consultation existing = existingOpt.get();
        existing.setDateTime(consultation.getDateTime());
        existing.setPatientId(consultation.getPatientId());
        existing.setDoctorId(consultation.getDoctorId());
        existing.setSpecialty(consultation.getSpecialty());
        existing.setReport(consultation.getReport());

        // Não sobrescrever o MedicalRecord se não foi alterado
        if (consultation.getMedicalRecord() != null) {
            existing.setMedicalRecord(medicalRecordOpt.get());
        }

        Consultation updated = consultationRepository.save(existing);

        return ResponseEntity.ok(updated);
    }

    public MedicalRecord addConsultation(Long medicalRecordId, Consultation consultation) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
            .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado para o ID: " + medicalRecordId));
    
        consultation.setMedicalRecord(medicalRecord);
        medicalRecord.getConsultations().add(consultation);
    
        consultationRepository.save(consultation);
    
        return medicalRecord;
    }

}
