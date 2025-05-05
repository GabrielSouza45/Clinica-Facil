package br.app.tads.clinica_facil.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.app.tads.clinica_facil.model.Revenue;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    
    List<Revenue> findByPatient(Patient patient);

    List<Revenue> findByDoctor(Doctor doctor);

    List<Revenue> findByDateTime(LocalDateTime dateTime);

}
