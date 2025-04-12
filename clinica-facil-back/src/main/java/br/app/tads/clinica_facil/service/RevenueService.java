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
import br.app.tads.clinica_facil.repository.RevenueRepository;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAll() {
        List<Revenue> revenues = revenueRepository.findAll();
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getById(Long id) {
        Optional<Revenue> revenue = revenueRepository.findById(id);
        if (revenue.isPresent()) {
            return responseBuilder.build(revenue.get(), HttpStatus.OK);
        } else {
            return responseBuilder.build("Receita não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> getByPatient(Patient patient) {
        List<Revenue> revenues = revenueRepository.findByPatient(patient);
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getByDoctor(Doctor doctor) {
        List<Revenue> revenues = revenueRepository.findByDoctor(doctor);
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getByDate(Date date) {
        List<Revenue> revenues = revenueRepository.findByDate(date);
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> getByReport(Report report) {
        List<Revenue> revenues = revenueRepository.findByReport(report);
        return responseBuilder.build(revenues, HttpStatus.OK);
    }

    public ResponseEntity<?> add(Revenue revenue) {
        Revenue savedRevenue = revenueRepository.save(revenue);
        return responseBuilder.build(savedRevenue, HttpStatus.CREATED);
    }

    public ResponseEntity<?> edit(Revenue revenue) {
        if (revenue.getId() == null) {
            return responseBuilder.build("O ID da receita é obrigatório para edição.", HttpStatus.BAD_REQUEST);
        }

        boolean exists = revenueRepository.existsById(revenue.getId());
        if (!exists) {
            return responseBuilder.build("Receita não encontrada para edição.", HttpStatus.NOT_FOUND);
        }

        Revenue updatedRevenue = revenueRepository.save(revenue);
        return responseBuilder.build(updatedRevenue, HttpStatus.OK);
    }
}
