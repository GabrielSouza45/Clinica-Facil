package br.app.tads.clinica_facil.model.records;

import br.app.tads.clinica_facil.model.dtos.DoctorDTO;
import br.app.tads.clinica_facil.model.dtos.PatientDTO;

import java.util.List;

public record Users (List<DoctorDTO> doctorsList, List<PatientDTO> patientsList) {
}
