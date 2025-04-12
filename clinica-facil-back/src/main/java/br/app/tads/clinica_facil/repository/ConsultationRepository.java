package br.app.tads.clinica_facil.repository;

import br.app.tads.clinica_facil.model.Consultation;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByPatient(Patient patient);

    List<Consultation> findByDoctor(Doctor doctor);

    List<Consultation> findByDateTimeBetween(Date start, Date end);

    List<Consultation> findBySpecialtyContainingIgnoreCase(String specialty);
}
