package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.*;
import br.app.tads.clinica_facil.model.enums.StatusConsultation;
import br.app.tads.clinica_facil.repository.*;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;



    //Método para buscar Todas as Consultas
    public ResponseEntity<?> getAllConsultations() {
        List<Consultation> list = consultationRepository.findAll();
        list.forEach(consulta -> {
            Patient patient = patientRepository.findById(consulta.getPatientId()).orElse(null);
            consulta.setPatient(patient);
            Doctor doctor = doctorRepository.findById(consulta.getDoctorId()).orElse(null);
            consulta.setDoctor(doctor);
        });
        return ResponseEntity.ok(list);
    }

    //Método para buscar as Consultas pelo Paciente
    public ResponseEntity<?> getConsultationsByPatient(Long patientId) {
        List<Consultation> list = consultationRepository.findByPatientId(patientId);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada para o paciente informado.");
        }

        return ResponseEntity.ok(list);
    }

    //Método para buscar as Consultas pelo Médico
    public ResponseEntity<?> getConsultationsByDoctor(Long doctorId) {
        List<Consultation> list = consultationRepository.findByDoctorId(doctorId);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada para o médico informado.");
        }

        return ResponseEntity.ok(list);
    }

    //Método para buscar as Consultas pela Data
    public ResponseEntity<?> getConsultByDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return ResponseEntity.badRequest().body("Data inválida ou não informada.");
        }
        List<Consultation> consultations = consultationRepository.findByDateTime(dateTime);

        if (consultations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum exame encontrado para a data informada.");
        }

        return ResponseEntity.ok(consultations);
    }

    //Método para buscar as Cosultas por Especialidade
    public ResponseEntity<?> getConsultationsBySpecialty(String specialty) {
        List<Consultation> list = consultationRepository.findBySpecialtyContainingIgnoreCase(specialty);

        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma consulta encontrada para a especialidade informada.");
        }

        return ResponseEntity.ok(list);
    }

    //Método para cancelar a Consulta é necessário informar o motivo do cancelamento
    public ResponseEntity<?> cancelConsultation(Long consultationId, Report report) {
        if (report == null || report.getReasons() == null || report.getReasons().trim().isEmpty()) {
            return responseBuilder.build("Motivo do cancelamento é obrigatório.", HttpStatus.BAD_REQUEST);
        }
    
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + consultationId));
    
        if (consultation.getStatus() == StatusConsultation.FINISHED) {
            return responseBuilder.build("A consulta já foi finalizada e não pode ser cancelada.", HttpStatus.BAD_REQUEST);
        }
    
        if (consultation.getStatus() == StatusConsultation.CANCELLED) {
            return responseBuilder.build("A consulta já foi cancelada anteriormente.", HttpStatus.BAD_REQUEST);
        }
    
        report.setConsultation(consultation);
    
        report.setPatientId(consultation.getPatientId());

        report.setDoctorId(consultation.getDoctorId());
    
        reportRepository.save(report);
    
        consultation.setReport(report);
        consultation.setStatus(StatusConsultation.CANCELLED);
        consultationRepository.save(consultation);
    
        return responseBuilder.build("Consulta cancelada com sucesso!", HttpStatus.OK);
    }
    
    //Método para finalizar a Consulta e adicionar um Relatório 
    public ResponseEntity<?> addReportAndFinalizeConsultation(Long consultationId, Report report) {
        if (report == null || report.getDiagnosis() == null || report.getDiagnosis().trim().isEmpty()) {
            return responseBuilder.build("Relatório e diagnóstico são obrigatórios.", HttpStatus.BAD_REQUEST);
        }

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta não encontrada com ID: " + consultationId));

        if (consultation.getStatus() == StatusConsultation.FINISHED) {
            return responseBuilder.build("A consulta já foi finalizada.", HttpStatus.BAD_REQUEST);
        }

        report.setConsultation(consultation);
    
        report.setPatientId(consultation.getPatientId());

        report.setDoctorId(consultation.getDoctorId());
    
        reportRepository.save(report);

        consultation.setReport(report);
        consultation.setStatus(StatusConsultation.FINISHED);
        consultationRepository.save(consultation);

        return responseBuilder.build("Consulta finalizada com sucesso!", HttpStatus.OK);
    }

    //Método para Atualizar dados da Consulta
    public ResponseEntity<?> updateConsultation(Long id, Consultation updatedConsultation) {
        Optional<Consultation> optionalConsultation = consultationRepository.findById(id);
    
        if (optionalConsultation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta não encontrada.");
        }
    
        Consultation existingConsultation = optionalConsultation.get();
    
        if (existingConsultation.getStatus() == StatusConsultation.CANCELLED
                || existingConsultation.getStatus() == StatusConsultation.FINISHED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Consultas canceladas ou finalizadas não podem ser editadas.");
        }
    
        if (updatedConsultation.getDateTime() == null
                || updatedConsultation.getDateTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("A data da consulta deve ser futura.");
        }
    
        if (updatedConsultation.getSpecialty() == null || updatedConsultation.getSpecialty().isBlank()) {
            return ResponseEntity.badRequest().body("A especialidade não pode ser vazia.");
        }
    
        if (updatedConsultation.getStatus() == null) {
            return ResponseEntity.badRequest().body("O status da consulta é obrigatório.");
        }
    
        existingConsultation.setDateTime(updatedConsultation.getDateTime());
        existingConsultation.setSpecialty(updatedConsultation.getSpecialty());
        existingConsultation.setStatus(updatedConsultation.getStatus());
    
        Consultation savedConsultation = consultationRepository.save(existingConsultation);
    
        return ResponseEntity.ok(savedConsultation);
    }
    

    //Método para Agendamento de Consultas
    public ResponseEntity<?>  createConsultation(Consultation consultation) {
        if (consultation.getDateTime().isBefore(LocalDateTime.now())) {
            return responseBuilder.build("A consulta deve ser agendada para uma data no futuro.",
                    HttpStatus.BAD_REQUEST);
        }

        if (consultation.getSpecialty() == null || consultation.getSpecialty().isEmpty()) {
            return responseBuilder.build("A especialidade deve ser informada.", HttpStatus.BAD_REQUEST);
        }

        Optional<Patient> patient = patientRepository.findById(consultation.getPatientId());
        if (patient.isEmpty()) {
            return responseBuilder.build("Paciente não localizado.", HttpStatus.NOT_FOUND);
        }

        MedicalRecord medicalRecord = medicalRecordRepository.findByPatient(patient.get());

        if (medicalRecord == null) {
            return responseBuilder.build("Prontuario do paciente não encontrado.", HttpStatus.BAD_REQUEST);
        }

        consultation.setMedicalRecord(medicalRecord);

        consultation.setStatus(StatusConsultation.PENDING);

        consultationRepository.save(consultation);

        return responseBuilder.build("Consulta criada com sucesso.", HttpStatus.CREATED);
    }

}
