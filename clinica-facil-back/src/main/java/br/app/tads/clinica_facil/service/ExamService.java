package br.app.tads.clinica_facil.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.model.Report;
import br.app.tads.clinica_facil.repository.ExamRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;
import br.app.tads.clinica_facil.repository.ReportRepository;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        if (exams.isEmpty()) {
            return responseBuilder.build("Nenhum exame encontrado.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    public ResponseEntity<?> getExamsByPatient(Long patientId) {
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isEmpty()) {
            return responseBuilder.build("Paciente não encontrado.", HttpStatus.NOT_FOUND);
        }

        List<Exam> exams = examRepository.findByPatient(optionalPatient.get());
        if (exams.isEmpty()) {
            return responseBuilder.build("Nenhum exame encontrado para o paciente informado.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    public ResponseEntity<?> getExamsByReport(Long reportId) {
        Optional<Report> optionalReport = reportRepository.findById(reportId);
        if (optionalReport.isEmpty()) {
            return responseBuilder.build("Relatório não encontrado.", HttpStatus.NOT_FOUND);
        }

        List<Exam> exams = examRepository.findByReport(optionalReport.get());
        if (exams.isEmpty()) {
            return responseBuilder.build("Nenhum exame vinculado a esse relatório.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    public ResponseEntity<?> getExamsByDate(Date date) {
        List<Exam> exams = examRepository.findByDate(date);
        if (exams.isEmpty()) {
            return responseBuilder.build("Nenhum exame encontrado para a data informada.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    public ResponseEntity<?> createExam(Exam exam) {
        if (exam.getPatient() == null || exam.getPatient().getId() == null) {
            return responseBuilder.build("Paciente não informado ou inválido.", HttpStatus.BAD_REQUEST);
        }

        Optional<Patient> optionalPatient = patientRepository.findById(exam.getPatient().getId());
        if (optionalPatient.isEmpty()) {
            return responseBuilder.build("Paciente não encontrado.", HttpStatus.BAD_REQUEST);
        }

        if (exam.getReport() != null && exam.getReport().getId() != null) {
            Optional<Report> optionalReport = reportRepository.findById(exam.getReport().getId());
            if (optionalReport.isEmpty()) {
                return responseBuilder.build("Relatório associado não encontrado.", HttpStatus.BAD_REQUEST);
            }
            exam.setReport(optionalReport.get());
        }

        exam.setPatient(optionalPatient.get());
        Exam savedExam = examRepository.save(exam);
        return new ResponseEntity<>(savedExam, HttpStatus.CREATED);
    }

    public ResponseEntity<?> editExam(Exam exam) {
        if (exam.getId() == null) {
            return responseBuilder.build("ID do exame não informado.", HttpStatus.BAD_REQUEST);
        }

        Optional<Exam> optionalExam = examRepository.findById(exam.getId());
        if (optionalExam.isEmpty()) {
            return responseBuilder.build("Exame não encontrado.", HttpStatus.NOT_FOUND);
        }

        Exam editable = optionalExam.get();
        editable.setName(exam.getName());
        editable.setDescription(exam.getDescription());
        editable.setDate(exam.getDate());
        editable.setResults(exam.getResults());

        if (exam.getPatient() != null && exam.getPatient().getId() != null) {
            Optional<Patient> optionalPatient = patientRepository.findById(exam.getPatient().getId());
            optionalPatient.ifPresent(editable::setPatient);
        }

        if (exam.getReport() != null && exam.getReport().getId() != null) {
            Optional<Report> optionalReport = reportRepository.findById(exam.getReport().getId());
            optionalReport.ifPresent(editable::setReport);
        }

        examRepository.save(editable);
        return new ResponseEntity<>(editable, HttpStatus.OK);
    }
}
