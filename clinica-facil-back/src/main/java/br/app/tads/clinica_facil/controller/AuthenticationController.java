package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.infra.security.AuthorizationService;
import br.app.tads.clinica_facil.infra.security.TokenService;
import br.app.tads.clinica_facil.model.Admin;
import br.app.tads.clinica_facil.model.Doctor;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.enums.Groups;
import br.app.tads.clinica_facil.model.records.Auth;
import br.app.tads.clinica_facil.model.records.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Collection;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private ResponseBuilder responseBuilder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        try{
            var usernamePassword = new UsernamePasswordAuthenticationToken(
                    login.email(),
                    login.password()
            );

            var auth = this.authenticationManager.authenticate(usernamePassword);
            String authority = auth.getAuthorities().toArray()[0].toString();

            boolean roleAdmin = authority.equals("ROLE_ADMIN");
            boolean roleDoctor = authority.equals("ROLE_DOCTOR");
            boolean rolePatient = authority.equals("ROLE_PATIENT");

            Auth userAuthorized = null;
            if (roleAdmin)
                userAuthorized = getUserAdmin(auth);
            else if (roleDoctor)
                userAuthorized = getUserDoctor(auth);
            else if (rolePatient)
                userAuthorized = getUserPatient(auth);
            else
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

            return ResponseEntity.ok(userAuthorized);

        } catch (Exception e){
            return responseBuilder.build(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    private Auth getUserAdmin(Authentication auth) {
        var token = tokenService.generateToken((Admin) auth.getPrincipal());

        Long id = ((Admin) auth.getPrincipal()).getId();
        String name = ((Admin) auth.getPrincipal()).getName();
        Groups group = Groups.ADMIN;

        return new Auth(token, name, group, id);
    }

    private Auth getUserDoctor(Authentication auth) {
        var token = tokenService.generateToken((Doctor) auth.getPrincipal());

        Long id = ((Doctor) auth.getPrincipal()).getId();
        String name = ((Doctor) auth.getPrincipal()).getName();
        Groups group = Groups.DOCTOR;

        return new Auth(token, name, group, id);
    }

    private Auth getUserPatient(Authentication auth) {
        var token = tokenService.generateToken((Patient) auth.getPrincipal());

        Long id = ((Patient) auth.getPrincipal()).getId();
        String name = ((Patient) auth.getPrincipal()).getName();
        Groups group = Groups.PATIENT;

        return new Auth(token, name, group, id);
    }


}
