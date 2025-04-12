package br.app.tads.clinica_facil.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
        return examService.getAllExams();
    }

    @GetMapping("/by-patient/{patientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getExamsByPatient(@PathVariable Long patientId) {
        return examService.getExamsByPatient(patientId);
    }

    @GetMapping("/by-report/{reportId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getExamsByReport(@PathVariable Long reportId) {
        return examService.getExamsByReport(reportId);
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('DOCTOR')") 
    public ResponseEntity<?> getExamsByDate(@RequestParam("date") 
        @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return examService.getExamsByDate(date);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> editExam(@RequestBody Exam exam) {
        return examService.editExam(exam);
    }
}
