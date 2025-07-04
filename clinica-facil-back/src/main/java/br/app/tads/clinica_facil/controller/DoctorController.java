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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getAllDoctorsActives() {
        return doctorService.getAllActive();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getAllDoctors() {
        return doctorService.getAll();
    }

    @GetMapping("/get-name/{doctorName}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getDoctorsByName(@PathVariable String doctorName) {
        if (doctorName.isBlank())
            return responseBuilder.build("Nome não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return doctorService.getByName(doctorName);

    }

    @GetMapping("/get-email/{doctorEmail}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getDoctorsByEmail(@PathVariable String doctorEmail) {
        if (doctorEmail.isBlank())
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return doctorService.getByEmail(doctorEmail);

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
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
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> deleteDoctors(@RequestBody Doctor doctor) {
        if (doctor.getEmail().isBlank()) {
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);
        }

        return doctorService.delete(doctor);
    }

}