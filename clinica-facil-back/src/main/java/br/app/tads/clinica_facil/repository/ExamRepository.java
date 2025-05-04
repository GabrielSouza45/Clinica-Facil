package br.app.tads.clinica_facil.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.Patient;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByPatient(Patient patient);

    List<Exam> findByPatientAndDate(Patient patient, Date date);

    List<Exam> findByDate(LocalDate date);

    @Query("SELECT e FROM Exam e WHERE e.date BETWEEN :startDate AND :endDate")
    List<Exam> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}