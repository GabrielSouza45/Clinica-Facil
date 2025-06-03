package br.app.tads.clinica_facil.model;

import br.app.tads.clinica_facil.model.enums.Status;

import java.time.LocalDate;
import java.util.Date;

public abstract class UsersAbstract {
    private Long id;

    private String email;
    private String password;
    private String name;
    private LocalDate birth;
    private String crm;
    private String cpf;
    private Status status;

}
