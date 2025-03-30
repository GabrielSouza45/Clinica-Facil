package br.app.tads.clinica_facil.model.records;

import br.app.tads.clinica_facil.model.enums.Groups;

public record Auth(String token, String name, Groups group, Long id) {
}
