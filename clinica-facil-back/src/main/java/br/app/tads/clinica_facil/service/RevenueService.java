package br.app.tads.clinica_facil.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.model.Revenue;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;
import br.app.tads.clinica_facil.repository.RevenueRepository;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public ResponseEntity<?> getAll() {
        List<Revenue> revenues = revenueRepository.findAll();

        if (revenues.isEmpty()) {
            return responseBuilder.build("Nenhuma receita cadastrada.", HttpStatus.NO_CONTENT);
        }

        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getById(Long id) {
        if (id <= 0) {
            return responseBuilder.build("ID inválido.", HttpStatus.BAD_REQUEST);
        }
    
        Optional<Revenue> revenueOpt = revenueRepository.findById(id);
    
        return revenueOpt
                .<ResponseEntity<?>>map(revenue -> responseBuilder.build(revenue, HttpStatus.OK))
                .orElseGet(() -> responseBuilder.build("Receita não encontrada.", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<?> getByPatientId(Long patientId) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            return responseBuilder.build("Paciente não encontrado.", HttpStatus.NOT_FOUND);
        }
    
        List<Revenue> revenues = revenueRepository.findByPatient(patientOpt.get());
        if (revenues.isEmpty()) {
            return responseBuilder.build("Nenhuma receita encontrada para o paciente.", HttpStatus.NOT_FOUND);
        }
    
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getByDoctorId(Long doctorId) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return responseBuilder.build("Médico não encontrado.", HttpStatus.NOT_FOUND);
        }
    
        List<Revenue> revenues = revenueRepository.findByDoctor(doctorOpt.get());
        if (revenues.isEmpty()) {
            return responseBuilder.build("Nenhuma receita encontrada para o médico.", HttpStatus.NOT_FOUND);
        }
    
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getByDate(Date date) {
        List<Revenue> revenues = revenueRepository.findByDate(date);
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> edit(Revenue revenue) {
        if (revenue.getId() == null) {
            return responseBuilder.build("O ID da receita é obrigatório para edição.", HttpStatus.BAD_REQUEST);
        }
    
        Optional<Revenue> existingRevenueOpt = revenueRepository.findById(revenue.getId());
        if (existingRevenueOpt.isEmpty()) {
            return responseBuilder.build("Receita não encontrada para edição.", HttpStatus.NOT_FOUND);
        }
    
        Revenue existingRevenue = existingRevenueOpt.get();
    
        // Atualiza apenas os campos alteráveis
        if (revenue.getMedications() != null) {
            existingRevenue.setMedications(revenue.getMedications());
        }
        if (revenue.getDosage() != null) {
            existingRevenue.setDosage(revenue.getDosage());
        }
        if (revenue.getRecommendations() != null) {
            existingRevenue.setRecommendations(revenue.getRecommendations());
        }
        if (revenue.getDate() != null) {
            existingRevenue.setDate(revenue.getDate());
        }
    
        // Não altera doctor, patient, medicalRecord se não passados no JSON
        if (revenue.getDoctor() != null) {
            existingRevenue.setDoctor(revenue.getDoctor());
        } else {
            // Caso não passe doctor no JSON, preserva o valor existente
            existingRevenue.setDoctor(existingRevenue.getDoctor());
        }
    
        if (revenue.getPatient() != null) {
            existingRevenue.setPatient(revenue.getPatient());
        } else {
            // Caso não passe patient no JSON, preserva o valor existente
            existingRevenue.setPatient(existingRevenue.getPatient());
        }
    
        if (revenue.getMedicalRecord() != null) {
            existingRevenue.setMedicalRecord(revenue.getMedicalRecord());
        } else {
            // Caso não passe medicalRecord no JSON, preserva o valor existente
            existingRevenue.setMedicalRecord(existingRevenue.getMedicalRecord());
        }
    
        // Atualiza a receita no banco
        Revenue updatedRevenue = revenueRepository.save(existingRevenue);
    
        return responseBuilder.build(updatedRevenue, HttpStatus.OK);
    }
}
