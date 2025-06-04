import { Component, OnInit, Pipe } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

import { MedicalRecord } from '../../../model/MedicalRecord';
import { Consultation } from '../../../model/Consultation';
import { Patient } from '../../../model/Patient';
import { StatusConsultation } from '../../../model/enums/StatusConsultation';

import { MedicalRecordService } from '../../../services/medicalRecordService/medical-record.service';
import { PatientService } from '../../../services/patientService/patient.service';
import { ConsultationService } from '../../../services/consultationService/consultation.service';
import { CommonModule } from '@angular/common';
import { LayoutPrincipalComponent } from "../../layout-principal/layout-principal.component";

@Component({
  selector: 'app-prontuario',
  imports: [
    CommonModule,
    LayoutPrincipalComponent,
    ReactiveFormsModule
],
  templateUrl: './prontuario.component.html',
  styleUrls: ['./prontuario.component.css']
})
export class ProntuarioComponent implements OnInit {
novoProntuario() {
throw new Error('Method not implemented.');
}
  formProntuario!: FormGroup;
  medicalRecords: MedicalRecord[] = [];
  patients: Patient[] = [];
  selectedMedicalRecord: MedicalRecord | null = null;
  editando: boolean = false;
  StatusConsultation = StatusConsultation;
criando: any;
 

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private medicalRecordService: MedicalRecordService,
    private patientService: PatientService,
    private consultationService: ConsultationService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadPatients();
    this.loadMedicalRecords();
  }

  private initForm(): void {
    this.formProntuario = this.fb.group({
      id: [null],
      patientId: [null, Validators.required],
      consultationDateTime: [null, Validators.required],
      specialty: ['', Validators.required],
      consultations: this.fb.array([])
    });

    this.addConsultationItem();
  }

  get consultations(): FormArray {
    return this.formProntuario.get('consultations') as FormArray;
  }

  addConsultationItem(): void {
    // Agora o form da consulta tem só o report como string JSON, specialty, status e dateTime
    const consultationGroup = this.fb.group({
      report: ['', Validators.required],  // report como JSON string
      specialty: ['', Validators.required],
      status: [StatusConsultation.PENDING, Validators.required],
      dateTime: [null, Validators.required]
    });

    this.consultations.push(consultationGroup);
  }

  removeConsultationItem(index: number): void {
    this.consultations.removeAt(index);
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
    this.editando = false;
  }

  editarProntuario(record: MedicalRecord): void {
    this.selectedMedicalRecord = record;
    this.editando = true;

    this.formProntuario.reset();

    while(this.consultations.length) {
      this.consultations.removeAt(0);
    }

    this.formProntuario.patchValue({
      id: record.id,
      patientId: record.patient?.id || null,
      consultationDateTime: record.consultations?.[0]?.dateTime || null,
      specialty: record.consultations?.[0]?.specialty || ''
    });

    if (record.consultations) {
      record.consultations.forEach(consult => {
        this.consultations.push(this.fb.group({
          report: consult.report?.body || '',
          specialty: consult.specialty || '',
          status: consult.status || StatusConsultation.PENDING,
          dateTime: consult.dateTime || null
        }));
      });
    }
  }

  salvarProntuario(): void {
    if (this.formProntuario.invalid) {
      this.toastr.warning('Preencha todos os campos obrigatórios!');
      return;
    }

    const formValue = this.formProntuario.value;
    const patient = this.patients.find(p => p.id === formValue.patientId);

    if (!patient) {
      this.toastr.error('Paciente inválido ou não encontrado');
      return;
    }

    if (this.editando && formValue.id) {
      this.atualizarProntuario(formValue, patient);
    } else {
      this.criarNovoProntuario(formValue, patient);
    }
  }

  private atualizarProntuario(formValue: any, patient: Patient): void {
    if (!this.selectedMedicalRecord) return;

    const consultasAtualizadas: Consultation[] = this.consultations.controls.map((ctrl, idx) => {
      const val = ctrl.value;
      return {
        id: this.selectedMedicalRecord?.consultations?.[idx]?.id,
        dateTime: val.dateTime,
        patientId: patient.id,
        specialty: val.specialty,
        status: val.status
      };
    });

    // Atualizar a primeira consulta para simplificar
    this.consultationService.editConsultation(this.selectedMedicalRecord.id!, consultasAtualizadas[0]).subscribe({
      next: () => {
        this.toastr.success('Prontuário atualizado com sucesso!');
        this.loadMedicalRecords();
        this.limparFormulario();
      },
      error: () => {
        this.toastr.error('Erro ao atualizar prontuário');
      }
    });
  }

  private criarNovoProntuario(formValue: any, patient: Patient): void {
    if (!patient.id) {
      this.toastr.error('ID do paciente indefinido');
      return;
    }

    const consultaInicial = this.consultations.at(0)?.value;

    if (!consultaInicial) {
      this.toastr.error('Consulta inicial inválida');
      return;
    }

    const novaConsulta: Consultation = {
      dateTime: consultaInicial.dateTime,
      patientId: patient.id,
      specialty: consultaInicial.specialty,
      report: {
        body: consultaInicial.report,
        type: 'Consulta Inicial',
        url: '',
        toJSON: function () {
          throw new Error('Function not implemented.');
        }
      },
      status: consultaInicial.status
    };

    this.medicalRecordService.createMedicalRecordWithConsultation(patient.id, novaConsulta).subscribe({
      next: (createdRecord: any) => {
        if (this.consultations.length > 1) {
          const consultasAdicionais = this.consultations.controls.slice(1).map(ctrl => {
            const val = ctrl.value;
            return {
              dateTime: val.dateTime,
              patientId: patient.id!,
              specialty: val.specialty,
              report: {
                body: val.report,
                type: 'Consulta Adicional',
                url: ''
              },
              status: val.status
            };
          });

          consultasAdicionais.forEach(consulta => {
            this.consultationService.createConsultation(createdRecord.id, consulta.report.body).subscribe({
              error: () => this.toastr.error('Erro ao adicionar consulta adicional')
            });
          });
        }

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
    this.formProntuario.reset();
    while (this.consultations.length) {
      this.consultations.removeAt(0);
    }
    this.addConsultationItem();
    this.selectedMedicalRecord = null;
    this.editando = false;
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
    return StatusConsultation[status as keyof typeof StatusConsultation];
  }
}
