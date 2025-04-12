package br.app.tads.clinica_facil.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Report;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    List<Exam> findByPatient(Patient patient);

    List<Exam> findByReport(Report report);

    List<Exam> findByDate(Date date);

    List<Exam> findByPatientAndDate(Patient patient, Date date);
}