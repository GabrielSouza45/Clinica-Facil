package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.repository.ConsultationRepository;
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

    public ResponseEntity<?> getAllConsultations() {
        List<Consultation> list = consultationRepository.findAll();
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsByPatient(Patient patient) {
        List<Consultation> list = consultationRepository.findByPatient(patient);
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsByDoctor(Doctor doctor) {
        List<Consultation> list = consultationRepository.findByDoctor(doctor);
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsByDateRange(Date start, Date end) {
        List<Consultation> list = consultationRepository.findByDateTimeBetween(start, end);
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> getConsultationsBySpecialty(String specialty) {
        List<Consultation> list = consultationRepository.findBySpecialtyContainingIgnoreCase(specialty);
        return ResponseEntity.ok(list);
    }

    public ResponseEntity<?> scheduleConsultation(Consultation consultation) {
        if (consultation == null || consultation.getDoctor() == null || consultation.getPatient() == null) {
            return responseBuilder.build("Consulta inválida. Verifique os dados.", HttpStatus.BAD_REQUEST);
        }

        Consultation saved = consultationRepository.save(consultation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<?> updateConsultation(Consultation consultation) {
        if (consultation == null || consultation.getId() == null) {
            return responseBuilder.build("Consulta inválida para atualização.", HttpStatus.BAD_REQUEST);
        }

        Optional<Consultation> existing = consultationRepository.findById(consultation.getId());
        if (existing.isEmpty()) {
            return responseBuilder.build("Consulta não encontrada.", HttpStatus.NOT_FOUND);
        }

        Consultation updated = consultationRepository.save(consultation);
        return ResponseEntity.ok(updated);
    }
}
