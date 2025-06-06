import { Component, NgModule, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, FormGroupName, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { MedicalRecord } from '../../../model/MedicalRecord';
import { Patient } from '../../../model/Patient';
import { StatusConsultation } from '../../../model/enums/StatusConsultation';
import { MedicalRecordService } from '../../../services/medicalRecordService/medical-record.service';
import { PatientService } from '../../../services/patientService/patient.service';
import { CommonModule } from '@angular/common';
import { LayoutPrincipalComponent } from "../../layout-principal/layout-principal.component";

interface PrescriptionForm {
  medication: string;
  dosage: string;
  instructions: string;
}

interface ExamForm {
  name: string;
  date: string;
  results: string;
}

@Component({
  selector: 'app-prontuario',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    LayoutPrincipalComponent
],
  templateUrl: './prontuario.component.html',
  styleUrls: ['./prontuario.component.css']
})
export class ProntuarioComponent implements OnInit {
  formProntuario!: FormGroup;
  medicalRecords: MedicalRecord[] = [];
  patients: Patient[] = [];
  selectedMedicalRecord: MedicalRecord | null = null;
  editando: boolean = false;
  criando: boolean = false;
  visualizando: boolean = false;
  StatusConsultation : any;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private medicalRecordService: MedicalRecordService,
    private patientService: PatientService
  ) {}

  ngOnInit(): void {
    this.novoProntuario();
    this.initForm();
    this.loadPatients();
    this.loadMedicalRecords();
  }

  initForm(): void {
    this.formProntuario = this.fb.group({
      patientId: [null, Validators.required],
      consultation: this.fb.group({
        dateTime: ['', Validators.required],
        specialty: ['', Validators.required],
        report: ['', Validators.required],
        status: [StatusConsultation.PENDING, Validators.required]
      }),
      exams: this.fb.array([this.createExamGroup()]),
      prescriptions: this.fb.array([this.createPrescriptionGroup()])
    });
  }

  createExamGroup(): FormGroup {
    return this.fb.group({
      name: ['', Validators.required],
      date: ['', Validators.required],
      results: ['', Validators.required]
    });
  }

  createPrescriptionGroup(): FormGroup {
    return this.fb.group({
      medication: ['', Validators.required],
      dosage: ['', Validators.required],
      instructions: ['', Validators.required]
    });
  }

  get exams(): FormArray {
    return this.formProntuario.get('exams') as FormArray;
  }

  get prescriptions(): FormArray {
    return this.formProntuario.get('prescriptions') as FormArray;
  }

  addExamItem(): void {
    this.exams.push(this.createExamGroup());
  }

  removeExamItem(index: number): void {
    if (this.exams.length > 1) {
      this.exams.removeAt(index);
    }
  }

  addPrescriptionItem(): void {
    this.prescriptions.push(this.createPrescriptionGroup());
  }

  removePrescriptionItem(index: number): void {
    if (this.prescriptions.length > 1) {
      this.prescriptions.removeAt(index);
    }
  }

  loadPatients(): void {
    this.patientService.getAllPatients().subscribe({
      next: (patients) => this.patients = patients,
      error: () => this.toastr.error('Erro ao carregar pacientes')
    });
  }

  loadMedicalRecords(): void {
    this.medicalRecordService.getAll().subscribe({
      next: (records) => this.medicalRecords = records,
      error: () => this.toastr.error('Erro ao carregar prontuários')
    });
  }

  visualizarProntuario(record: MedicalRecord): void {
    this.selectedMedicalRecord = record;
    this.visualizando = true;
    this.editando = false;
    this.criando = false;
  }

  novoProntuario(): void {
    this.selectedMedicalRecord = null;
    this.visualizando = false;
    this.editando = false;
    this.criando = true;
    this.initForm();
  }

  editarProntuario(record: MedicalRecord): void {
    this.selectedMedicalRecord = record;
    this.visualizando = false;
    this.editando = true;
    this.criando = false;

    // Limpa os arrays existentes
    while (this.exams.length) this.exams.removeAt(0);
    while (this.prescriptions.length) this.prescriptions.removeAt(0);

    // Preenche o formulário com os dados do prontuário
    const primeiraConsulta = record.consultations?.[0];
    this.formProntuario.patchValue({
      patientId: record.patient?.id || null,
      consultation: {
        dateTime: primeiraConsulta?.dateTime || '',
        specialty: primeiraConsulta?.specialty || '',
        report: primeiraConsulta?.report?.body || '',
        status: primeiraConsulta?.status || StatusConsultation.PENDING
      }
    });

    // Adiciona exames
    if (record.exams && record.exams.length > 0) {
      record.exams.forEach(exam => {
        this.exams.push(this.fb.group({
          name: exam.name || '',
          date: exam.date || '',
          results: exam.results || ''
        }));
      });
    } else {
      this.addExamItem();
    }

    // Adiciona prescrições
    if (record.revenues && record.revenues.length > 0) {
      record.revenues.forEach(revenue => {
        this.prescriptions.push(this.fb.group({
          medication: revenue.medications || '',
          dosage: revenue.dosage || '',
          instructions: revenue.recommendations || ''
        }));
      });
    } else {
      this.addPrescriptionItem();
    }
  }

  salvarProntuario(): void {
    if (this.formProntuario.invalid) {
      this.toastr.warning('Preencha todos os campos obrigatórios!');
      return;
    }

    const formValue = this.formProntuario.value;
    const patient = this.patients.find(p => p.id === formValue.patientId);

    if (!patient || !patient.id) {
      this.toastr.error('Paciente inválido ou não encontrado');
      return;
    }

    if (this.editando && this.selectedMedicalRecord?.id) {
      this.atualizarProntuario(formValue, patient);
    } else {
      this.criarNovoProntuario(formValue, patient);
    }
  }

  private atualizarProntuario(formValue: any, patient: Patient): void {
    if (!this.selectedMedicalRecord?.id) return;

    const updatedData = {
      patientId: patient.id,
      consultation: {
        id: this.selectedMedicalRecord.consultations?.[0]?.id, // Mantém o ID se existir
        dateTime: formValue.consultation.dateTime,
        specialty: formValue.consultation.specialty,
        status: formValue.consultation.status,
        report: {
          body: formValue.consultation.report,
          type: 'Consulta'
        }
      },
      exams: this.exams.value,
      revenues: this.prescriptions.value.map((p: any) => ({
        medications: p.medication,
        dosage: p.dosage,
        recommendations: p.instructions,
        dateTime: new Date().toISOString()
      }))
    };

    this.medicalRecordService.updateMedicalRecordWithConsultation(
      this.selectedMedicalRecord.id, // Usa o ID do registro médico
      updatedData
    ).subscribe({
      next: () => {
        this.toastr.success('Prontuário atualizado com sucesso!');
        this.loadMedicalRecords();
        this.limparFormulario();
      },
      error: () => this.toastr.error('Erro ao atualizar prontuário')
    });
}

private criarNovoProntuario(formValue: any, patient: Patient): void {
    // Verificação explícita do patient.id
    if (patient?.id === undefined) {
        this.toastr.error('ID do paciente não disponível');
        return;
    }

    const consultationData = {
        patientId: patient.id, // Agora garantido que não é undefined
        consultation: {
            dateTime: formValue.consultation.dateTime,
            specialty: formValue.consultation.specialty,
            status: formValue.consultation.status,
            report: {
                body: formValue.consultation.report,
                type: 'Consulta'
            }
        },
        exams: this.exams.value,
        revenues: this.prescriptions.value.map((p: any) => ({
            medications: p.medication,
            dosage: p.dosage,
            recommendations: p.instructions,
            dateTime: new Date().toISOString()
        }))
    };

    this.medicalRecordService.createMedicalRecordWithConsultation(
        patient.id, // TypeScript agora sabe que é number
        consultationData
    ).subscribe({
        next: () => {
            this.toastr.success('Prontuário criado com sucesso!');
            this.loadMedicalRecords();
            this.limparFormulario();
        },
        error: () => {
            this.toastr.error('Erro ao criar prontuário');
        }
    });
    this.medicalRecordService.createMedicalRecordWithConsultation(
      patient?.id,
      consultationData
    ).subscribe({
      next: () => {
        this.toastr.success('Prontuário criado com sucesso!');
        this.loadMedicalRecords();
        this.limparFormulario();
      },
      error: () => {
        this.toastr.error('Erro ao criar prontuário');
      }
    });
}
  excluirProntuario(id: number): void {
    if (!confirm('Tem certeza que deseja excluir este prontuário?')) return;

    this.medicalRecordService.deleteMedicalRecord(id).subscribe({
      next: () => {
        this.toastr.success('Prontuário excluído com sucesso!');
        this.loadMedicalRecords();
        if (this.selectedMedicalRecord?.id === id) {
          this.selectedMedicalRecord = null;
          this.limparFormulario();
        }
      },
      error: () => this.toastr.error('Erro ao excluir prontuário')
    });
  }

  limparFormulario(): void {
    this.selectedMedicalRecord = null;
    this.visualizando = false;
    this.editando = false;
    this.criando = false;
    this.initForm();
  }

  getPatientName(patientId?: number): string {
    if (patientId === undefined) return 'Paciente desconhecido';
    const patient = this.patients.find(p => p.id === patientId);
    return patient ? patient.name || 'Paciente' : 'Paciente desconhecido';
  }

  getStatusClass(status?: StatusConsultation): string {
    switch (status) {
      case StatusConsultation.PENDING: return 'badge bg-primary';
      case StatusConsultation.FINISHED: return 'badge bg-success';
      case StatusConsultation.CANCELLED: return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }

  getStatusText(status?: StatusConsultation): string {
    if (status === undefined) return 'Desconhecido';
    return StatusConsultation[status] || 'Desconhecido';
  }
}