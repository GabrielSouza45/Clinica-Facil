package br.app.tads.clinica_facil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.repository.ExamRepository;
import br.app.tads.clinica_facil.repository.MedicalRecordRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    public ResponseEntity<?> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        
        if (exams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum exame encontrado.");
        }

        return ResponseEntity.ok(exams);
    }

    public ResponseEntity<?> getExamsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado."));

        List<Exam> exams = examRepository.findByPatient(patient);
        if (exams.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum exame encontrado para o paciente informado.");
        }

        return ResponseEntity.ok(exams);
    }

    public ResponseEntity<?> getExamsByDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return ResponseEntity.badRequest().body("Data inválida ou não informada.");
        }
        List<Exam> exams = examRepository.findByDateTime(dateTime);
    
        if (exams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum exame encontrado para a data informada.");
        }
    
        return ResponseEntity.ok(exams);
    }

    public Exam updateExam(Long examId, Exam updatedExam) {
        Optional<Exam> existingExamOpt = examRepository.findById(examId);
        if (existingExamOpt.isPresent()) {
            Exam existingExam = existingExamOpt.get();
            
            updatedExam.setId(existingExam.getId());  
            updatedExam.setPatient(existingExam.getPatient()); 
            updatedExam.setMedicalRecord(existingExam.getMedicalRecord()); 
    
            existingExam.setName(updatedExam.getName());
            existingExam.setDescription(updatedExam.getDescription());
            existingExam.setDateTime(updatedExam.getDateTime());
            existingExam.setResults(updatedExam.getResults());

            return examRepository.save(existingExam); 
        } else {
            throw new RuntimeException("Exame não encontrado");
        }
    }

    public MedicalRecord addExam(Long medicalRecordId, Exam exam) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord medicalRecord = optionalMedicalRecord.get();
            exam.setMedicalRecord(medicalRecord);
            medicalRecord.getExams().add(exam);
            return medicalRecordRepository.save(medicalRecord);
        } else {
            throw new IllegalArgumentException("Prontuário não encontrado");
        }
    }

}
