package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.UsersAbstract;
import br.app.tads.clinica_facil.model.dtos.DoctorDTO;
import br.app.tads.clinica_facil.model.dtos.PatientDTO;
import br.app.tads.clinica_facil.model.enums.Status;
import br.app.tads.clinica_facil.model.interfaces.IUser;
import br.app.tads.clinica_facil.model.records.Users;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAllActives() {
        List<Patient> patientList = patientRepository.findAllByStatus(Status.ACTIVE);
        List<Doctor> doctorList = doctorRepository.findAllByStatus(Status.ACTIVE);

        List<UsersAbstract> list = new ArrayList<>();
        list.addAll(patientList);
        list.addAll(doctorList);

        return responseBuilder.build(list, HttpStatus.OK);
    }

    private List<DoctorDTO> getDoctorsDTO(List<Doctor> doctorList) {
        List<DoctorDTO> dto = new ArrayList<>();

        doctorList.forEach(doctor -> {
            dto.add(new DoctorDTO(doctor));
        });

        return dto;
    }

    private List<PatientDTO> getpatientsDTO(List<Patient> patientList) {
        List<PatientDTO> dto = new ArrayList<>();

        patientList.forEach(patient -> {
            dto.add(new PatientDTO(patient));
        });

        return dto;
    }

    public ResponseEntity<?> getByName(String name) {

        List<Patient> patientList = patientRepository.findByNameContaining(name);
        List<Doctor> doctorList = doctorRepository.findByNameContaining(name);

        List<UsersAbstract> list = new ArrayList<>();
        list.addAll(patientList);
        list.addAll(doctorList);

        return responseBuilder.build(list, HttpStatus.OK);

    }
}
