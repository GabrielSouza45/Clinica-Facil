package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Agenda;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAllAgendas() {
        List<Agenda> agendas = agendaRepository.findAll();
        return responseBuilder.build(agendas, HttpStatus.OK);
    }

    public ResponseEntity<?> getAgendasByDoctor(Doctor doctor) {
        if (doctor == null || doctor.getId() == null) {
            return responseBuilder.build("Médico inválido para busca de agenda", HttpStatus.BAD_REQUEST);
        }
        List<Agenda> agendas = agendaRepository.findByDoctor(doctor);
        return responseBuilder.build(agendas, HttpStatus.OK);
    }

    public ResponseEntity<?> getAvailableAgendasByDoctor(Doctor doctor) {
        if (doctor == null || doctor.getId() == null) {
            return responseBuilder.build("Médico inválido para busca de horários disponíveis", HttpStatus.BAD_REQUEST);
        }
        List<Agenda> agendas = agendaRepository.findByDoctorAndAvailableTrue(doctor);
        return responseBuilder.build(agendas, HttpStatus.OK);
    }

    public ResponseEntity<?> getAgendasByDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || end.isBefore(start)) {
            return responseBuilder.build("Intervalo de datas inválido", HttpStatus.BAD_REQUEST);
        }

        List<Doctor> doctors = agendaRepository.findAvailableDoctors(start, end);
        return responseBuilder.build(doctors, HttpStatus.OK);
    }

    public ResponseEntity<?> createAgenda(Agenda agenda) {
        if (agenda == null) {
            return responseBuilder.build("Agenda não pode ser nula", HttpStatus.BAD_REQUEST);
        }
        Agenda saved = agendaRepository.save(agenda);
        return responseBuilder.build(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateAgenda(Agenda agenda) {
        if (agenda == null || agenda.getId() == null) {
            return responseBuilder.build("Agenda inválida para edição", HttpStatus.BAD_REQUEST);
        }

        Optional<Agenda> existing = agendaRepository.findById(agenda.getId());
        if (existing.isEmpty()) {
            return responseBuilder.build("Agenda não encontrada", HttpStatus.NOT_FOUND);
        }

        Agenda updated = agendaRepository.save(agenda);
        return responseBuilder.build(updated, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAgenda(Long id) {
        if (id == null) {
            return responseBuilder.build("ID inválido para exclusão", HttpStatus.BAD_REQUEST);
        }

        Optional<Agenda> existing = agendaRepository.findById(id);
        if (existing.isEmpty()) {
            return responseBuilder.build("Agenda não encontrada", HttpStatus.NOT_FOUND);
        }

        agendaRepository.deleteById(id);
        return responseBuilder.build("Agenda excluída com sucesso", HttpStatus.OK);
    }
}
