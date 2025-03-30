package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.repository.PatientRepository;
import br.app.tads.clinica_facil.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ResponseBuilder responseBuilder;


    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('USER')")
    public ResponseEntity<?> getAllPatients () {
        return patientService.getAll();
    }

}
