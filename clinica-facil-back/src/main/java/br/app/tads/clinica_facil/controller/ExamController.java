package br.app.tads.clinica_facil.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.service.ExamService;

@RestController
@RequestMapping("/exam")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllExams() {
        List<Exam> exams = (List<Exam>) examService.getAllExams(); 
        if (exams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum exame encontrado.");
        }
        return ResponseEntity.ok(exams); 
    }

    @GetMapping("/by-patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getExamsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(examService.getExamsByPatient(patientId));
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')")
    public ResponseEntity<?> getExamsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return examService.getExamsByDate(date);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> editExam(@RequestBody Exam exam) {
        return examService.editExam(exam);
    }
}
