package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.repository.MedicalRecordRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord getMedicalRecordsByPatient(Patient patient) {
        return medicalRecordRepository.findByPatient(patient);
    }

    public MedicalRecord createMedicalRecord(Patient patient) {
        MedicalRecord existing = medicalRecordRepository.findByPatient(patient);
        if (existing != null) {
            throw new IllegalStateException("O paciente já possui um prontuário.");
        }
        MedicalRecord medicalRecord = new MedicalRecord(patient);
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord addConsultationToMedicalRecord(Long medicalRecordId, Consultation consultation) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
            .orElseThrow(() -> new EntityNotFoundException("Prontuário não encontrado"));
        
            consultation.setMedicalRecord(medicalRecord);
            medicalRecord.getConsultations().add(consultation);
            medicalRecordRepository.save(medicalRecord);
    
        return medicalRecordRepository.save(medicalRecord);
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
