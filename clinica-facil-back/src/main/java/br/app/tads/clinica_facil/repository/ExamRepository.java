package br.app.tads.clinica_facil.repository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.Patient;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByPatient(Patient patient);

    List<Exam> findByPatientAndDateTime(Patient patient, LocalDateTime dateTime);

    List<Exam> findByDateTime(LocalDateTime dateTime);

    List<Exam> findByDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}