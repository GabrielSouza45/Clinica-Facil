package br.app.tads.clinica_facil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Revenue;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import br.app.tads.clinica_facil.repository.MedicalRecordRepository;
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

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

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

    public ResponseEntity<?> getRevenueByDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return ResponseEntity.badRequest().body("Data inválida ou não informada.");
        }
        List<Revenue> revenues = revenueRepository.findByDateTime(dateTime);

        if (revenues.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma Receita encontrada para a data informada.");
        }

        return ResponseEntity.ok(revenues);
    }

    public ResponseEntity<?> updateRevenue(Long id, Revenue updatedRevenue) {
        
        Optional<Revenue> optionalRevenue = revenueRepository.findById(id);
        if (!optionalRevenue.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receita não encontrada.");
        }

        Revenue existingRevenue = optionalRevenue.get();
       
        existingRevenue.setMedications(updatedRevenue.getMedications());
        existingRevenue.setDosage(updatedRevenue.getDosage());
        existingRevenue.setRecommendations(updatedRevenue.getRecommendations());
        existingRevenue.setDateTime(updatedRevenue.getDateTime());

        revenueRepository.save(existingRevenue);

        return ResponseEntity.ok(existingRevenue);
    }

    public MedicalRecord addRevenue(Long medicalRecordId, Revenue revenue) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord medicalRecord = optionalMedicalRecord.get();
            revenue.setMedicalRecord(medicalRecord);
            medicalRecord.getRevenues().add(revenue);
            return medicalRecordRepository.save(medicalRecord);
        } else {
            throw new IllegalArgumentException("Prontuário não encontrado");
        }
    }
}
