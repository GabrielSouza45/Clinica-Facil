package br.app.tads.clinica_facil.repository;

import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    
    List<MedicalRecord> findByPatient(Patient patient);

    Optional<MedicalRecord> findFirstByPatient(Patient patient);

}
