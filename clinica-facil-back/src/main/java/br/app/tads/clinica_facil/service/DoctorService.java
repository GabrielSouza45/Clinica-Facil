package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.enums.Status;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {


    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAllActive (){
        return responseBuilder.build(doctorRepository.findAllByStatus(Status.ACTIVE), HttpStatus.OK);
    }

    public ResponseEntity<?> getAll() {
        return responseBuilder.build(doctorRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> add(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail()))
            return responseBuilder.build("Email já cadastrado", HttpStatus.UNAUTHORIZED);

        doctor.setStatus(Status.ACTIVE);
        Doctor saved = doctorRepository.save(doctor);
        return responseBuilder.build(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getByName(String doctorName) {

        List<Doctor> doctors = doctorRepository.findByNameContaining(doctorName);

        return new ResponseEntity<>(doctors, HttpStatus.OK);

    }

    public ResponseEntity<?> getByEmail(String doctorEmail) {

        Doctor doctorSaved = doctorRepository.findByEmail(doctorEmail);

        return new ResponseEntity<>(doctorSaved, HttpStatus.OK);

    }

    public ResponseEntity<?> edit(Doctor doctor) {

        Doctor obj = doctorRepository.findByEmail(doctor.getEmail());
        if (obj == null)
            return responseBuilder.build("Usuário não localizado!", HttpStatus.NOT_FOUND);

        obj.setName(doctor.getName());
        obj.setBirth(doctor.getBirth());
        obj.setPassword(new BCryptPasswordEncoder().encode(doctor.getPassword()));
        obj.setCrm(doctor.getCrm());
        obj.setStatus(doctor.getStatus());
        Doctor saved = doctorRepository.save(obj);

        return responseBuilder.build(saved, HttpStatus.OK);

    }

    public ResponseEntity<?> delete(Doctor doctor) {

        Doctor doctorSaved = doctorRepository.findByEmailAndStatus(doctor.getEmail(), Status.ACTIVE);
        if (doctorSaved == null)
            return responseBuilder.build("Usuário não localizado!", HttpStatus.NOT_FOUND);

        doctorSaved.setStatus(Status.INACTIVE);
        doctorRepository.save(doctorSaved);

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
