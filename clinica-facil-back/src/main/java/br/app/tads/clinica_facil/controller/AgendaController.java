package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.model.Agenda;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getAllAgendas() {
        return agendaService.getAllAgendas();
    }

    @PostMapping("/by-doctor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getAgendasByDoctor(@RequestBody Doctor doctor) {
        return agendaService.getAgendasByDoctor(doctor);
    }

    @PostMapping("/available-by-doctor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<?> getAvailableAgendasByDoctor(@RequestBody Doctor doctor) {
        return agendaService.getAvailableAgendasByDoctor(doctor);
    }

    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAgendasByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return agendaService.getAgendasByDateRange(start, end);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAgenda(@RequestBody Agenda agenda) {
        return agendaService.createAgenda(agenda);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAgenda(@RequestBody Agenda agenda) {
        return agendaService.updateAgenda(agenda);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAgenda(@PathVariable Long id) {
        return agendaService.deleteAgenda(id);
    }
}
