import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { MedicalRecord } from '../../../model/MedicalRecord';
import { Patient } from '../../../model/Patient';
import { StatusConsultation } from '../../../model/enums/StatusConsultation';
import { MedicalRecordService } from '../../../services/medicalRecordService/medical-record.service';
import { PatientService } from '../../../services/patientService/patient.service';
import { CommonModule } from '@angular/common';
import { LayoutPrincipalComponent } from "../../layout-principal/layout-principal.component";

@Component({
  selector: 'app-prontuario',
  imports: [
    ReactiveFormsModule,
    CommonModule,
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
  
  statusOptions = [
    { value: StatusConsultation.PENDING, label: 'Pendente' },
    { value: StatusConsultation.FINISHED, label: 'Finalizado' },
    { value: StatusConsultation.CANCELLED, label: 'Cancelado' }
  ];

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private medicalRecordService: MedicalRecordService,
    private patientService: PatientService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadPatients();
    this.loadMedicalRecords();
  }

  initForm(): void {
  this.formProntuario = this.fb.group({
    patientId: [null, [Validators.required, Validators.pattern(/^[1-9]\d*$/)]],
    consultation: this.fb.group({
      dateTime: ['', [Validators.required]],
      specialty: ['', [Validators.required, Validators.minLength(3)]],
      report: ['', [Validators.required, Validators.minLength(10)]],
      status: [StatusConsultation.PENDING, Validators.required]
    }),
    exams: this.fb.array([]),
    prescriptions: this.fb.array([])
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
    this.exams.removeAt(index);
  }

  addPrescriptionItem(): void {
    this.prescriptions.push(this.createPrescriptionGroup());
  }

  removePrescriptionItem(index: number): void {
    this.prescriptions.removeAt(index);
  }

  loadPatients(): void {
    this.patientService.getAllPatients().subscribe({
      next: (patients) => {
        this.patients = patients;
      },
      error: (error) => {
        console.error('Erro ao carregar pacientes:', error);
        this.toastr.error('Erro ao carregar pacientes');
      }
    });
  }

  loadMedicalRecords(): void {
    this.medicalRecordService.getAll().subscribe({
      next: (records) => {
        this.medicalRecords = records;
      },
      error: (error) => {
        console.error('Erro ao carregar prontuários:', error);
        this.toastr.error('Erro ao carregar prontuários');
      }
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
        dateTime: primeiraConsulta?.dateTime ? this.formatDateTimeForInput(primeiraConsulta.dateTime) : '',
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

  private formatDateTimeForInput(dateTime: any): string {
    const date = new Date(dateTime);
    return date.toISOString().slice(0, 16);
  }

  private formatDateForInput(date: any): string {
    const dateObj = new Date(date);
    return dateObj.toISOString().split('T')[0];
  }

  salvarProntuario(): void {
  
  const formValue = this.formProntuario.value;
  const patientId = formValue.patientId;

  if (this.editando && this.selectedMedicalRecord?.id) {
    this.medicalRecordService.updateMedicalRecordWithConsultation(
      this.selectedMedicalRecord.id,
      formValue
    ).subscribe({
      next: () => {
        this.toastr.success('Prontuário atualizado com sucesso!');
        this.loadMedicalRecords();
        this.limparFormulario();
      },
      error: (error) => {
        console.error('Erro:', error);
        this.toastr.error('Erro ao atualizar prontuário');
      }
    });
  } else {
    this.medicalRecordService.createMedicalRecordWithConsultation(
      patientId,
      formValue
    ).subscribe({
      next: () => {
        this.toastr.success('Prontuário criado com sucesso!');
        this.loadMedicalRecords();
        this.limparFormulario();
      },
      error: (error) => {
        console.error('Erro:', error);
        this.toastr.error('Erro ao criar prontuário');
      }
    });
  }
}

  private markFormGroupTouched(formGroup: FormGroup | FormArray) {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      
      if (control instanceof FormGroup || control instanceof FormArray) {
        this.markFormGroupTouched(control);
      }
    });
  }

  private atualizarProntuario(formValue: any, patient: Patient): void {
    if (!this.selectedMedicalRecord?.id) return;

    const updatedData = {
      patientId: patient.id,
      consultation: {
        id: this.selectedMedicalRecord.consultations?.[0]?.id,
        dateTime: formValue.consultation.dateTime,
        specialty: formValue.consultation.specialty,
        status: formValue.consultation.status,
        report: {
          body: formValue.consultation.report,
          type: 'Consulta'
        }
      },
      exams: formValue.exams,
      revenues: formValue.prescriptions.map((p: any) => ({
        medications: p.medication,
        dosage: p.dosage,
        recommendations: p.instructions,
        dateTime: new Date().toISOString()
      }))
    };

    this.medicalRecordService.updateMedicalRecordWithConsultation(
      this.selectedMedicalRecord.id,
      updatedData
    ).subscribe({
      next: () => {
        this.toastr.success('Prontuário atualizado com sucesso!');
        this.loadMedicalRecords();
        this.limparFormulario();
      },
      error: (error) => {
        console.error('Erro ao atualizar prontuário:', error);
        this.toastr.error('Erro ao atualizar prontuário');
      }
    });
  }

  private criarNovoProntuario(formValue: any, patient: Patient): void {
     
  if (!patient.id) {
    this.toastr.error('ID do paciente não disponível');
    return;
  }

  const newRecordData = {
    patientId: patient.id,
    consultation: {
      dateTime: formValue.consultation.dateTime,
      specialty: formValue.consultation.specialty,
      status: formValue.consultation.status,
      report: {
        body: formValue.consultation.report,
        type: 'Consulta'
      }
    },
    exams: formValue.exams,
    revenues: formValue.prescriptions.map((p: any) => ({
      medications: p.medication,
      dosage: p.dosage,
      recommendations: p.instructions,
      dateTime: new Date().toISOString()
    }))
  };

  console.log('Dados formatados para API:', newRecordData);

  this.medicalRecordService.createMedicalRecordWithConsultation(
    patient.id,
    newRecordData
  ).subscribe({
    next: (response) => {
      console.log('Resposta da API:', response);
      this.toastr.success('Prontuário criado com sucesso!');
      this.loadMedicalRecords();
      this.limparFormulario();
    },
    error: (error) => {
      console.error('Erro completo:', error);
      console.error('Mensagem de erro:', error.message);
      console.error('Erro ao criar prontuário:', error.error);
      this.toastr.error(`Erro ao criar prontuário: ${error.error?.message || error.message}`);
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
      error: (error) => {
        console.error('Erro ao excluir prontuário:', error);
        this.toastr.error('Erro ao excluir prontuário');
      }
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
    return this.statusOptions.find(opt => opt.value === status)?.label || 'Desconhecido';
  }
}