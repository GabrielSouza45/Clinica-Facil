import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { InputPrimarioComponent } from '../../components/input-primario/input-primario.component';
import { ModalComponent } from '../../components/modal/modal.component';
import { TablePaginationComponent } from '../../components/back-office/table-pagination/table-pagination.component';
import { AuthService } from '../../infra/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tela-agendamento',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputPrimarioComponent,
    ModalComponent,
    TablePaginationComponent
  ],
  templateUrl: './tela-agendamento.component.html',
  styleUrls: ['./tela-agendamento.component.css']
})
export class TelaAgendamentoComponent {
  consultas: any[] = [];
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

  constructor(
    private fb: FormBuilder, 
    private toastr: ToastrService,
    public authService: AuthService,
    private router: Router,
  ) {
    this.inicializarFormulario();
    this.carregarConsultas();
    this.usuarioAtual = this.authService.getUserName() || '';
    this.isMedico = this.authService.getUserRole() === 'DOCTOR';
    this.isPaciente = this.authService.getUserRole() === 'PATIENT';
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
      patient: ['', Validators.required],
      doctor: ['', Validators.required],
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

  verificarDisponibilidadeMedico(consulta: any): boolean {
    const consultasConflitantes = this.consultas.filter(c => 
      c.doctor === consulta.doctor && 
      c.date === consulta.date &&
      c.time === consulta.time &&
      (!this.consultaSelecionada || c.id !== this.consultaSelecionada.id)
    );

    return consultasConflitantes.length === 0;
  }

  notificarPaciente(consulta: any) {
    const mensagem = `Consulta agendada com ${consulta.doctor} para ${consulta.date} às ${consulta.time}`;
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
    if (this.formConsulta.invalid) {
      this.toastr.warning('Preencha todos os campos corretamente!');
      return;
    }

    this.isCadastro ? this.agendarConsulta() : this.reagendarConsulta();
  }

  agendarConsulta() {
    const formValue = this.formConsulta.getRawValue();
    
    if (!this.verificarDisponibilidadeMedico(formValue)) {
      this.toastr.error('O médico não está disponível no horário selecionado');
      return;
    }

    const novaConsulta = {
      ...formValue,
      id: Date.now(),
      status: 'Agendada',
      idPatient: this.authService.getIdUser(),
      idDoctor: null 
    };

    this.consultas.push(novaConsulta);
    this.toastr.success('Consulta agendada com sucesso!');
    this.notificarPaciente(novaConsulta);
    this.modalAberto = false;
    this.carregarConsultas();
  }

  reagendarConsulta() {
    const formValue = this.formConsulta.getRawValue();
    
    if (!this.verificarDisponibilidadeMedico(formValue)) {
      this.toastr.error('O médico não está disponível no horário selecionado');
      return;
    }

    Object.assign(this.consultaSelecionada, formValue);
    this.toastr.success('Consulta reagendada com sucesso!');
    this.notificarPaciente(this.consultaSelecionada);
    this.modalAberto = false;
  }

  logout() {
    this.authService.logout();
  }

  pageChanged(page: number) {
    this.pagina = page;
  }

  podeEditar(consulta: any): boolean {
    return this.isPaciente && consulta.patient === this.authService.getUserName();
  }

  carregarConsultas() {
    const userName = this.authService.getUserName();
    const userRole = this.authService.getUserRole();

    // Dados 
    const mockConsultas = [
      {
        id: 1,
        patient: 'João da Silva',
        doctor: 'Dra. Ana Lima',
        date: '2025-06-01',
        time: '14:00',
        status: 'Agendada'
      },
      {
        id: 2,
        patient: 'Maria Clara',
        doctor: 'Dr. Carlos Souza',
        date: '2025-06-02',
        time: '09:00',
        status: 'Agendada'
      }
    ];

    this.consultas = mockConsultas.filter(c => 
      (userRole === 'DOCTOR' && c.doctor === userName) ||
      (userRole === 'PATIENT' && c.patient === userName)
    );

    this.totalItens = this.consultas.length;
  }

  irParaLogin(): void {
    this.router.navigate(['/login']);
  }
}