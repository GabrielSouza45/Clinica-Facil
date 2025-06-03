package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/get-all-actives")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getAllActives(){
        return usersService.getAllActives();
    }

    @GetMapping("/get-by-name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> getByName(@PathVariable("name") String name){
        return usersService.getByName(name);
    }
}
