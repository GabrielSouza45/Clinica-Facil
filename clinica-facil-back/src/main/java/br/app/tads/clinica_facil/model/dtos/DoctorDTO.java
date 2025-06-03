package br.app.tads.clinica_facil.model.dtos;

import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.enums.Status;

import java.time.LocalDate;
import java.util.Date;

public class DoctorDTO {
    private Long id;
    private String email;
    private String name;
    private LocalDate birth;
    private String crm;
    private Status status;

    public DoctorDTO() {
    }

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.email = doctor.getEmail();
        this.name = doctor.getName();
        this.birth = doctor.getBirth();
        this.crm = doctor.getCrm();
        this.status = doctor.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
