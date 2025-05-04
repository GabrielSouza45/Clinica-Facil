package br.app.tads.clinica_facil.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.app.tads.clinica_facil.infra.responseBuilder.ResponseBuilder;
import br.app.tads.clinica_facil.model.Exam;
import br.app.tads.clinica_facil.model.MedicalRecord;
import br.app.tads.clinica_facil.model.Patient;
import br.app.tads.clinica_facil.repository.ExamRepository;
import br.app.tads.clinica_facil.repository.MedicalRecordRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;


@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private ResponseBuilder responseBuilder;

    public ResponseEntity<?> getAllExams() {
        List<Exam> exams = examRepository.findAll();
        if (exams.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum exame encontrado.");
        }
        return ResponseEntity.ok(exams);
    }

    public ResponseEntity<?> getExamsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado."));

        List<Exam> exams = examRepository.findByPatient(patient);
        if (exams.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nenhum exame encontrado para o paciente informado.");
        }

        return ResponseEntity.ok(exams);
    }

    public ResponseEntity<?> getExamsByDate(LocalDate localDate) {
        if (localDate == null) {
            return responseBuilder.build("Data inválida ou não informada.", HttpStatus.BAD_REQUEST);
        }
    
        ZoneId zone = ZoneId.systemDefault();
        Date startDate = Date.from(localDate.atStartOfDay(zone).toInstant());
        Date endDate = Date.from(localDate.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant());
    
        List<Exam> exams = examRepository.findByDateBetween(startDate, endDate);
    
        if (exams.isEmpty()) {
            return responseBuilder.build("Nenhum exame encontrado para a data informada.", HttpStatus.NOT_FOUND);
        }
    
        return ResponseEntity.ok(exams);
    }

    public ResponseEntity<?> editExam(Exam exam) {
    if (exam.getId() == null) {
        return responseBuilder.build("ID do exame não informado.", HttpStatus.BAD_REQUEST);
    }

    // Buscar o exame pelo ID informado
    Optional<Exam> optionalExam = examRepository.findById(exam.getId());
    if (optionalExam.isEmpty()) {
        return responseBuilder.build("Exame não encontrado.", HttpStatus.NOT_FOUND);
    }

    // Obtenção do exame para edição
    Exam editable = optionalExam.get();

    // Atualização dos campos apenas se os valores não forem null
    if (exam.getName() != null) editable.setName(exam.getName());
    if (exam.getDescription() != null) editable.setDescription(exam.getDescription());
    if (exam.getDate() != null) editable.setDate(exam.getDate());
    if (exam.getResults() != null) editable.setResults(exam.getResults());

    // Verificação e atualização da associação com o paciente, se informado
    if (exam.getPatient() != null && exam.getPatient().getId() != null) {
        Optional<Patient> optionalPatient = patientRepository.findById(exam.getPatient().getId());
        if (optionalPatient.isPresent()) {
            editable.setPatient(optionalPatient.get());
        } else {
            return responseBuilder.build("Paciente informado não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    // Garantir que a associação com o prontuário (medicalRecord) não seja perdida
    if (exam.getMedicalRecord() != null && exam.getMedicalRecord().getId() != null) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(exam.getMedicalRecord().getId());
        if (optionalMedicalRecord.isPresent()) {
            editable.setMedicalRecord(optionalMedicalRecord.get());
        } else {
            return responseBuilder.build("Prontuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    // Salvar o exame com as alterações feitas
    examRepository.save(editable);

    // Retorno com o exame atualizado
    return new ResponseEntity<>(editable, HttpStatus.OK);
}

}
