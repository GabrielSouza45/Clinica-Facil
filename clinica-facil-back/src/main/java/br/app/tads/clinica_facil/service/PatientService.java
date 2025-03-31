package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.enums.Status;
import br.app.tads.clinica_facil.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {


    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAllActive (){
        return responseBuilder.build(patientRepository.findAllByStatus(Status.ACTIVE), HttpStatus.OK);
    }

    public ResponseEntity<?> getAll() {
        return responseBuilder.build(patientRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> add(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail()))
            return responseBuilder.build("Email já cadastrado", HttpStatus.UNAUTHORIZED);

        patient.setStatus(Status.ACTIVE);
        Patient saved = patientRepository.save(patient);
        return responseBuilder.build(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getByName(Patient patient) {

        List<Patient> patients = patientRepository.findByNameContainingAndStatus(patient.getName(), Status.ACTIVE);

        return new ResponseEntity<>(patients, HttpStatus.OK);

    }

    public ResponseEntity<?> getByEmail(Patient patient) {

        Patient patientSaved = patientRepository.findByEmailAndStatus(patient.getEmail(), Status.ACTIVE);

        return new ResponseEntity<>(patientSaved, HttpStatus.OK);

    }

    public ResponseEntity<?> edit(Patient patient) {

        Patient obj = patientRepository.findByEmail(patient.getEmail());
        if (obj == null)
            return responseBuilder.build("Usuário não localizado!", HttpStatus.NOT_FOUND);

        obj.setName(patient.getName());
        obj.setBirth(patient.getBirth());
        obj.setPassword(new BCryptPasswordEncoder().encode(patient.getPassword()));
        obj.setCpf(patient.getCpf());
        obj.setStatus(patient.getStatus());
        Patient saved = patientRepository.save(obj);

        return responseBuilder.build(saved, HttpStatus.OK);

    }

    public ResponseEntity<?> delete(Patient patient) {

        Patient patientSaved = patientRepository.findByEmailAndStatus(patient.getEmail(), Status.ACTIVE);
        if (patientSaved == null)
            return responseBuilder.build("Usuário não localizado!", HttpStatus.NOT_FOUND);

        patientSaved.setStatus(Status.INACTIVE);
        patientRepository.save(patientSaved);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
