package br.app.tads.clinica_facil.repository;

import br.app.tads.clinica_facil.model.Admin;
import br.app.tads.clinica_facil.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository <Admin, Long> {
    Admin findByEmailAndStatus(String email, Status status);

    Admin findByEmail(String email);
}
