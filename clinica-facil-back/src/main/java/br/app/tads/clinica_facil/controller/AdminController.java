package br.app.tads.clinica_facil.controller;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Admin;
import br.app.tads.clinica_facil.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ResponseBuilder responseBuilder;


    @GetMapping("/get-all-actives")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAdminsActives() {
        return adminService.getAllActive();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllAdmins() {
        return adminService.getAll();
    }

    @GetMapping("/get-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminsByName(@RequestBody Admin admin) {
        if (admin.getName().isBlank())
            return responseBuilder.build("Nome não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return adminService.getByName(admin);

    }

    @GetMapping("/get-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminsByEmail(@RequestBody Admin admin) {
        if (admin.getEmail().isBlank())
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);

        return adminService.getByEmail(admin);

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addAdmins(@RequestBody Admin admin) {
        if (
                admin.getName().isBlank()
                        || admin.getPassword().isBlank()
                        || admin.getEmail().isBlank()
        ) {
            return responseBuilder.build("Um ou mais dados são nulos, por favor, verifique e envie novamente", HttpStatus.BAD_REQUEST);
        }

        return adminService.add(admin);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editAdmins(@RequestBody Admin admin) {
        if (
                admin.getName().isBlank()
                        || admin.getPassword().isBlank()
                        || admin.getEmail().isBlank()
        ) {
            return responseBuilder.build("Um ou mais dados são nulos, por favor, verifique e envie novamente", HttpStatus.BAD_REQUEST);
        }

        return adminService.edit(admin);
    }

    @PutMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAdmins(@RequestBody Admin admin) {
        if (admin.getEmail().isBlank()) {
            return responseBuilder.build("Email não pode ser nulo!", HttpStatus.BAD_REQUEST);
        }

        return adminService.delete(admin);
    }

}

