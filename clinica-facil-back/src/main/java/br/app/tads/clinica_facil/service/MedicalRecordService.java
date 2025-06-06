package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.model.*;
import br.app.tads.clinica_facil.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public List<MedicalRecord> getMedicalRecordsByPatient(Patient patient) {
        return medicalRecordRepository.findByPatient(patient);
    }

    public MedicalRecord createMedicalRecord(Patient patient) {
        List<MedicalRecord> existing = medicalRecordRepository.findByPatient(patient);
        if (!existing.isEmpty()) {
            throw new IllegalStateException("O paciente já possui um prontuário.");
        }
        MedicalRecord medicalRecord = new MedicalRecord(patient);
        return medicalRecordRepository.save(medicalRecord);
    }

    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord updatedRecord) {
        // Verifica se o prontuário existe
        MedicalRecord existingRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado com ID: " + id));

        // Atualiza os campos permitidos
        existingRecord.setPatient(updatedRecord.getPatient());

        // Atualiza consultas (se necessário)
        if (updatedRecord.getConsultations() != null) {
            existingRecord.setConsultations(updatedRecord.getConsultations());
        }

        // Atualiza exames (se necessário)
        if (updatedRecord.getExams() != null) {
            existingRecord.setExams(updatedRecord.getExams());
        }

        // Atualiza receitas (se necessário)
        if (updatedRecord.getRevenues() != null) {
            existingRecord.setRevenues(updatedRecord.getRevenues());
        }

        return medicalRecordRepository.save(existingRecord);
    }

    public MedicalRecord addConsultationToMedicalRecord(Long medicalRecordId, Consultation consultation) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
            .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado"));
        
            consultation.setMedicalRecord(medicalRecord);
            medicalRecord.getConsultations().add(consultation);
            medicalRecordRepository.save(medicalRecord);
    
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord addRevenueToMedicalRecord(Long medicalRecordId, Revenue revenue) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord medicalRecord = optionalMedicalRecord.get();
            revenue.setMedicalRecord(medicalRecord);
            revenue.setPatient(medicalRecord.getPatient());
            medicalRecord.getRevenues().add(revenue);
            return medicalRecordRepository.save(medicalRecord);
        } else {
            throw new IllegalArgumentException("Prontuário não encontrado");
        }
    }

    public MedicalRecord addExamToMedicalRecord(Long medicalRecordId, Exam exam) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord medicalRecord = optionalMedicalRecord.get();

            // Associa o exame ao prontuário e ao paciente
            exam.setMedicalRecord(medicalRecord);
            exam.setPatient(medicalRecord.getPatient());

            // Adiciona o exame à lista de exames do prontuário
            medicalRecord.getExams().add(exam);

            return medicalRecordRepository.save(medicalRecord);
        } else {
            throw new IllegalArgumentException("Prontuário não encontrado");
        }
    }

    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    
    public void deleteMedicalRecord(Long medicalRecordId) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (optionalMedicalRecord.isPresent()) {
            medicalRecordRepository.delete(optionalMedicalRecord.get());
        } else {
            throw new IllegalArgumentException("Prontuário não encontrado");
        }
    }
}
