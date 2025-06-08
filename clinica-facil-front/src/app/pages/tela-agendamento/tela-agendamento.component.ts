import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { InputPrimarioComponent } from '../../components/input-primario/input-primario.component';
import { ModalComponent } from '../../components/modal/modal.component';
import { TablePaginationComponent } from '../../components/back-office/table-pagination/table-pagination.component';
import { AuthService } from '../../infra/auth.service';
import { Router } from '@angular/router';
import { LayoutPrincipalComponent } from "../layout-principal/layout-principal.component";
import { Doctor } from '../../model/Doctor';
import { ConsultationService } from '../../services/consultationService/consultation.service';
import { Consultation } from '../../model/Consultation';
import ConsultationTable, { consultationTableHeaders } from '../../model/table-exibition/ConsultationTable';
import { SelectComponent } from "../../components/select/select.component";
import { Patient } from '../../model/Patient';
import { PatientService } from '../../services/patientService/patient.service';
import { DoctorService } from '../../services/doctorService/doctor.service';

@Component({
  selector: 'app-tela-agendamento',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputPrimarioComponent,
    ModalComponent,
    TablePaginationComponent,
    LayoutPrincipalComponent,
    SelectComponent
  ],
  templateUrl: './tela-agendamento.component.html',
  styleUrls: ['./tela-agendamento.component.css']
})
export class TelaAgendamentoComponent {
  consultas: ConsultationTable[] = [];
  formConsulta!: FormGroup;
  modalAberto = false;
  isCadastro = true;
  totalItens = 0;
  pagina = 1;
  consultaSelecionada: any = null;
  usuarioAtual: string = '';
  isMedico: boolean = false;
  isPaciente: boolean = false;
  today: string = new Date().toISOString().split('T')[0];
  buscarForm!: FormGroup;
  doutores: SelectOptions[] = [];
  pacientes: SelectOptions[] = [];

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    public authService: AuthService,
    private router: Router,
    private consultationService: ConsultationService,
    private patientService: PatientService,
    private doctorService: DoctorService,
  ) {
    this.inicializarFormulario();
    this.carregarConsultas();
    this.usuarioAtual = this.authService.getUserName() || '';
    this.isMedico = this.authService.getUserRole() === 'DOCTOR';
    this.isPaciente = this.authService.getUserRole() === 'PATIENT';
    this.getDoutoresDisponiveis();
    this.getPacientes();
  }


  acoes = [
    {
      nome: 'Reagendar',
      icone: 'bi bi-calendar-check',
      funcao: (item: any) => this.abrirModalReagendamento(item),
      mostrar: (item: any) => this.isPaciente && this.podeEditar(item)
    },
    {
      nome: 'Cancelar',
      icone: 'bi bi-x-circle',
      funcao: (item: any) => this.cancelarConsulta(item),
      mostrar: (item: any) => this.isPaciente && this.podeEditar(item)
    },
  ];


  inicializarFormulario() {
    this.formConsulta = this.fb.group({
      patientId: ['', Validators.required],
      doctorId: ['', Validators.required],
      date: ['', Validators.required],
      time: ['', Validators.required],
    });

    const role = this.authService.getUserRole();
    const nome = this.authService.getUserName();

    if (role === 'PATIENT') {
      this.formConsulta.patchValue({ patient: nome });
      this.formConsulta.get('patient')?.disable();
    }
  }





  notificarPaciente(consulta: Consultation) {
    const mensagem = `Consulta agendada com ${consulta.doctor} para ${consulta.dateTime}`;
    this.toastr.info(mensagem, 'Notificação');
  }

  abrirModalAgendamento() {
    if (!this.authService.isAuthenticated()) {
      this.toastr.warning('Por favor, faça login para agendar consultas');
      return;
    }

    if (this.isMedico) {
      this.toastr.warning('Médicos não podem agendar consultas');
      return;
    }

    this.formConsulta.reset();
    this.inicializarFormulario();
    this.isCadastro = true;
    this.modalAberto = true;
  }

  abrirModalReagendamento(consulta: any) {
    if (!this.podeEditar(consulta)) {
      this.toastr.warning('Você não tem permissão para editar esta consulta');
      return;
    }

    this.consultaSelecionada = consulta;
    this.formConsulta.patchValue({
      patient: consulta.patient,
      doctor: consulta.doctor,
      date: consulta.date,
      time: consulta.time,
    });

    this.isCadastro = false;
    this.modalAberto = true;
  }

  cancelarConsulta(consulta: any) {
    if (!this.podeEditar(consulta)) {
      this.toastr.warning('Você não tem permissão para cancelar esta consulta');
      return;
    }

    if (confirm(`Tem certeza que deseja cancelar a consulta com ${consulta.doctor} marcada para ${consulta.date}?`)) {
      this.consultas = this.consultas.filter(c => c.id !== consulta.id);
      this.toastr.success('Consulta cancelada com sucesso!');
      this.totalItens = this.consultas.length;
    }
  }

  fecharModal() {
    this.modalAberto = false;
  }

  confirmarAcao() {
    console.log(this.formConsulta);

    if (this.formConsulta.invalid) {
      this.toastr.warning('Preencha todos os campos corretamente!');
      return;
    }

    this.isCadastro ? this.agendarConsulta() : this.reagendarConsulta();
  }

  agendarConsulta() {
    const date = this.formConsulta.get("date")?.value;
    const time = this.formConsulta.get("time")?.value;

    const dateTimeString = `${date}T${time}:00`; // formato ISO 8601

    const consultation = new Consultation();
    consultation.doctorId = this.formConsulta.get("doctorId")?.value;
    consultation.patientId = this.formConsulta.get("patientId")?.value;
    consultation.dateTime = new Date(dateTimeString);

    // if (!this.verificarDisponibilidadeMedico(formValue)) {
    //   this.toastr.error('O médico não está disponível no horário selecionado');
    //   return;
    // }

    this.consultationService.createConsultation(consultation).subscribe({
      next: () => {
        this.toastr.success('Consulta agendada com sucesso!');
        this.notificarPaciente(consultation);
        this.modalAberto = false;
        this.carregarConsultas();
      }
    })
  }

  reagendarConsulta() {
    const formValue = this.formConsulta.getRawValue();

    // if (!this.verificarDisponibilidadeMedico(formValue)) {
    //   this.toastr.error('O médico não está disponível no horário selecionado');
    //   return;
    // }

    Object.assign(this.consultaSelecionada, formValue);
    this.toastr.success('Consulta reagendada com sucesso!');
    this.notificarPaciente(this.consultaSelecionada);
    this.modalAberto = false;
  }

  logout() {
    this.authService.logout();
  }



  podeEditar(consulta: any): boolean {
    return this.isPaciente && consulta.patient === this.authService.getUserName();
  }

  carregarConsultas() {
    this.consultationService.getAll().subscribe({
      next: (response: Consultation[]) => {
        this.consultas = response.map((consulta) => ({
          id: consulta.id,
          patientName: consulta.patient.name,
          patientCpf: consulta.patient.cpf,
          doctorName: consulta.doctor.name,
          doctorCrm: consulta.doctor.crm,
          dateTime: consulta.dateTime,
          speciality: consulta.specialty,
          status: consulta.status
        }));
      }
    })
  }

  irParaLogin(): void {
    this.router.navigate(['/login']);
  }


  getPacientes(): void {

    this.patientService.getAllPatientsActives().subscribe({
      next: (response: Patient[]) => {
        response.map((paciente) => {
          const option = new SelectOptions();
          option.text = paciente.name + ", CPF: " + paciente.cpf;
          option.value = Number(paciente.id);

          this.pacientes.push(option);
        });
      }
    });
  }

  getDoutoresDisponiveis(): void {
    this.doctorService.getAllDoctors().subscribe({
      next: (response: Doctor[]) => {
        response.map((doutor) => {
          const option = new SelectOptions();
          option.text = doutor.name + ", CRM: " + doutor.crm;
          option.value = Number(doutor.id);

          this.doutores.push(option);
        });
      }
    });
  }
}

class SelectOptions {
  text!: string;
  value!: number;
}
