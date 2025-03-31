package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.repository.PatientRepository;
import br.app.tads.clinica_facil.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/get-all-actives")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPatientsActives() {
        return patientService.getAllActive();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPatients() {
        return patientService.getAll();
    }

    @GetMapping("/get-name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientsByName(@RequestBody Patient patient) {
        if (patient.getName().isBlank())
            return responseBuilder.build("Nome não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return patientService.getByName(patient);

    }

    @GetMapping("/get-email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getPatientsByEmail(@RequestBody Patient patient) {
        if (patient.getEmail().isBlank())
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return patientService.getByEmail(patient);

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPatients(@RequestBody Patient patient) {
        if (
                patient.getName().isBlank()
                        || patient.getCpf().isBlank()
                        || patient.getPassword().isBlank()
                        || patient.getBirth() == null
                        || patient.getEmail().isBlank()
        ) {
            return responseBuilder.build("Um ou mais dados são nulos, por favor, verifique e envie novamente", HttpStatus.BAD_REQUEST);
        }

        return patientService.add(patient);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editPatients(@RequestBody Patient patient) {
        if (
                patient.getName().isBlank()
                        || patient.getCpf().isBlank()
                        || patient.getPassword().isBlank()
                        || patient.getBirth() == null
                        || patient.getEmail().isBlank()
        ) {
            return responseBuilder.build("Um ou mais dados são nulos, por favor, verifique e envie novamente", HttpStatus.BAD_REQUEST);
        }

        return patientService.edit(patient);
    }

    @PutMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatients(@RequestBody Patient patient) {
        if (patient.getEmail().isBlank()) {
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);
        }

        return patientService.delete(patient);
    }



}
