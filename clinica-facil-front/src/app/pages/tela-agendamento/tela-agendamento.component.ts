
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { InputPrimarioComponent } from '../../components/input-primario/input-primario.component';
import { ModalComponent } from '../../components/modal/modal.component';
import { TablePaginationComponent } from '../../components/back-office/table-pagination/table-pagination.component';

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
  styleUrl: './tela-agendamento.component.css'
})
export class TelaAgendamentoComponent {
  consultas: any[] = [];
  formConsulta!: FormGroup;
  modalAberto = false;
  clickCadastro = true;
  totalItens = 0;
  pagina = 1;
  consultaSelecionada: any = null;
  nomeUsuario = 'Paciente';

  constructor(private fb: FormBuilder, private toastr: ToastrService) {
    this.inicializarFormulario();
    this.carregarConsultas();
  }

  inicializarFormulario() {
    this.formConsulta = this.fb.group({
      paciente: ['', Validators.required],
      medico: ['', Validators.required],
      data: ['', Validators.required],
      hora: ['', Validators.required],
    });
  }

  carregarConsultas() {
    this.consultas = [
      { id: 1, paciente: 'João', medico: 'Dra. Ana', data: '2025-05-25', hora: '14:00', status: 'Agendada' },
      { id: 2, paciente: 'Maria', medico: 'Dr. Pedro', data: '2025-05-26', hora: '10:00', status: 'Agendada' },
    ];
    this.totalItens = this.consultas.length;
  }

  abrirModalAgendamento() {
    this.formConsulta.reset();
    this.clickCadastro = true;
    this.modalAberto = true;
  }

  abrirModalReagendamento(consulta: any) {
    this.consultaSelecionada = consulta;
    this.formConsulta.patchValue({
      paciente: consulta.paciente,
      medico: consulta.medico,
      data: consulta.data,
      hora: consulta.hora,
    });
    this.clickCadastro = false;
    this.modalAberto = true;
  }

  cancelarConsulta(consulta: any) {
    this.toastr.info(`Consulta com ${consulta.medico} cancelada.`);
    this.consultas = this.consultas.filter(c => c.id !== consulta.id);
  }

  mudaEstadoClick() {
    if (this.formConsulta.invalid) {
      this.toastr.warning('Preencha todos os campos!');
      return;
    }

    if (this.clickCadastro) {
      this.agendarConsulta();
    } else {
      this.reagendarConsulta();
    }
  }

  agendarConsulta() {
    const nova = { ...this.formConsulta.value, id: Date.now(), status: 'Agendada' };
    this.consultas.push(nova);
    this.toastr.success('Consulta agendada com sucesso!');
    this.modalAberto = false;
  }

  reagendarConsulta() {
    Object.assign(this.consultaSelecionada, this.formConsulta.value);
    this.toastr.success('Consulta reagendada com sucesso!');
    this.modalAberto = false;
  }

  fecharModal() {
    this.modalAberto = false;
  }

  logout() {
    sessionStorage.clear();
    window.location.href = '/login';
  }

  pageChanged(page: number) {
    this.pagina = page;
  }

  acoes = [
    {
      nome: () => 'Reagendar',
      icone: () => 'bi bi-calendar-check',
      funcao: (item: any) => this.abrirModalReagendamento(item),
    },
    {
      nome: () => 'Cancelar',
      icone: () => 'bi bi-x-circle',
      funcao: (item: any) => this.cancelarConsulta(item),
    },
  ];
}
