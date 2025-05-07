package br.app.tads.clinica_facil.service;

import br.app.tads.clinica_facil.model.Admin;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.enums.Status;
import br.app.tads.clinica_facil.repository.AdminRepository;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Service
public class CheckInitialDataInDB implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    private final String STANDARD_ADMIN_MAIL = "admin@admin.com";
    private final String STANDARD_DOCTOR_MAIL = "doctor@standard.com";
    private final String STANDARD_PATIENT_MAIL = "patient@standard.com";

    @Override
    public void run(String... args) throws Exception {

        Admin admin = adminRepository.findByEmail(STANDARD_ADMIN_MAIL);
        if (admin == null) registerStandardAdmin();

        Doctor doctor = doctorRepository.findByEmail(STANDARD_DOCTOR_MAIL);
        if (doctor == null) registerStandardDoctor();

        Patient patient = patientRepository.findByEmail(STANDARD_PATIENT_MAIL);
        if (patient == null) registerStandardPatient();

    }

    private void registerStandardAdmin() {
        Admin admin = new Admin(
                STANDARD_ADMIN_MAIL,
                new BCryptPasswordEncoder().encode("12345"),
                "John Doe Admin",
                Status.ACTIVE
        );
        adminRepository.save(admin);
        System.out.println("ADMIN padrão criado!");
    }

    private void registerStandardDoctor() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(1999, Calendar.OCTOBER, 15, 0, 0, 0);
        Doctor doctor = new Doctor(
                STANDARD_DOCTOR_MAIL,
                new BCryptPasswordEncoder().encode("12345"),
                "John Doe Doctor",
                calendar.getTime(),
                "123456789",
                Status.ACTIVE
        );
        doctorRepository.save(doctor);
        System.out.println("DOCTOR padrão criado!");
    }

    private void registerStandardPatient() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2001, Calendar.OCTOBER, 17, 0, 0, 0);
        Patient patient = new Patient(
                STANDARD_PATIENT_MAIL,
                new BCryptPasswordEncoder().encode("12345"),
                "John Doe Patient",
                calendar.getTime(),
                "12674811021",
                Status.ACTIVE
        );
        patientRepository.save(patient);
        System.out.println("PATIENT padrão criado!");
    }
}
