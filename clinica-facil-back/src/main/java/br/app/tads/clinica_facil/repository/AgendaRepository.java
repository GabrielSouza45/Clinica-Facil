package br.app.tads.clinica_facil.repository;

import br.app.tads.clinica_facil.model.Agenda;
import br.app.tads.clinica_facil.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByDoctor(Doctor doctor);

    List<Agenda> findByDoctorAndAvailableTrue(Doctor doctor);

    List<Agenda> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Agenda> findByDoctorAndStartDateTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);

    @Query(value = """
        SELECT * FROM doctor d
        WHERE d.id NOT IN (
            SELECT a.doctor_id
            FROM agenda a
            WHERE 
                a.start_date_time < :end
                AND a.end_date_time > :start
        )
        """, nativeQuery = true)
    List<Doctor> findAvailableDoctors(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
