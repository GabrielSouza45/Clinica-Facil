package br.app.tads.clinica_facil.repository;

import br.app.tads.clinica_facil.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByPatientId(Long patientId);

    List<Consultation> findByDoctorId(Long doctorId);

    List<Consultation> findByDateTimeBetween(Date start, Date end);

    List<Consultation> findBySpecialtyContainingIgnoreCase(String specialty);
}
