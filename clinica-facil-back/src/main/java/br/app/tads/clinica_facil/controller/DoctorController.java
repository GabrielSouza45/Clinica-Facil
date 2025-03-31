package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ResponseBuilder responseBuilder;


    @GetMapping("/get-all-actives")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllDoctorsActives() {
        return doctorService.getAllActive();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllDoctors() {
        return doctorService.getAll();
    }

    @GetMapping("/get-name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getDoctorsByName(@RequestBody Doctor doctor) {
        if (doctor.getName().isBlank())
            return responseBuilder.build("Nome não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return doctorService.getByName(doctor);

    }

    @GetMapping("/get-email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<?> getDoctorsByEmail(@RequestBody Doctor doctor) {
        if (doctor.getEmail().isBlank())
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return doctorService.getByEmail(doctor);

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addDoctors(@RequestBody Doctor doctor) {
        if (
                doctor.getName().isBlank()
                        || doctor.getPassword().isBlank()
                        || doctor.getBirth() == null
                        || doctor.getEmail().isBlank()
                        || doctor.getCrm().isBlank()
        ) {
            return responseBuilder.build("Um ou mais dados são nulos, por favor, verifique e envie novamente", HttpStatus.BAD_REQUEST);
        }

        return doctorService.add(doctor);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editDoctors(@RequestBody Doctor doctor) {
        if (
                doctor.getName().isBlank()
                        || doctor.getCrm().isBlank()
                        || doctor.getPassword().isBlank()
                        || doctor.getBirth() == null
                        || doctor.getEmail().isBlank()
        ) {
            return responseBuilder.build("Um ou mais dados são nulos, por favor, verifique e envie novamente", HttpStatus.BAD_REQUEST);
        }

        return doctorService.edit(doctor);
    }

    @PutMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDoctors(@RequestBody Doctor doctor) {
        if (doctor.getEmail().isBlank()) {
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);
        }

        return doctorService.delete(doctor);
    }

}