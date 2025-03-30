package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.enums.Status;
import br.app.tads.clinica_facil.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PatientService {


    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAll (){
        return responseBuilder.build(patientRepository.findAllByStatus(Status.ACTIVE), HttpStatus.OK);
    }

}
