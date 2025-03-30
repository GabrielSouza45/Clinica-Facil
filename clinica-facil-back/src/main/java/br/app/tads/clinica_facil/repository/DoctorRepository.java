package br.app.tads.clinica_facil.repository;

import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmailAndStatus(String email, Status status);

    Doctor findByEmail(String mail);
}
