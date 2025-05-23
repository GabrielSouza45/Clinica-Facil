import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Group } from '../../../app/model/enums/Group';
import { InputPrimarioComponent } from '../../components/input-primario/input-primario.component';
import { ModalComponent } from '../../components/modal/modal.component';
import { PatientService } from '../../services/patientService/patient.service';
import { DoctorService } from '../../services/doctorService/doctor.service';

@Component({
  selector: 'app-cadastrar-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputPrimarioComponent,
    ModalComponent,
  ],
  templateUrl: './cadastrar-usuarios.component.html',
  styleUrl: './cadastrar-usuarios.component.css',
})
export class CadastrarUsuariosComponent {
[x: string]: any;
  formCadastroUsuario!: FormGroup;
  modalAberto: boolean = false;
  clickCadastro: boolean = true;
  grupoSelecionado: Group = Group.PATIENT;
  Group = Group;

  constructor(
    private toastr: ToastrService,
    private patientService: PatientService,
    private doctorService: DoctorService
  ) {
    this.inicializarFormulario();
  }

  inicializarFormulario() {
    this.formCadastroUsuario = new FormGroup({
      nome: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      senha: new FormControl('', [Validators.required]),
      confirmarSenha: new FormControl('', [Validators.required]),
      cpf: new FormControl(''),
      crm: new FormControl('')
    });

    this.atualizarValidadores();
  }

  abrirModalComGrupo(grupo: Group) {
    this.formCadastroUsuario.reset();
    this.grupoSelecionado = grupo;
    this.clickCadastro = true;
    this.modalAberto = true;
    this.atualizarValidadores();
  }

  fecharModal() {
    this.modalAberto = false;
  }

  atualizarValidadores() {
    const cpfControl = this.formCadastroUsuario.get('cpf');
    const crmControl = this.formCadastroUsuario.get('crm');

    cpfControl?.clearValidators();
    crmControl?.clearValidators();

    if (this.grupoSelecionado === Group.PATIENT) {
      cpfControl?.setValidators([Validators.required]);
    } else if (this.grupoSelecionado === Group.Doctor) {
      crmControl?.setValidators([Validators.required]);
    }

    cpfControl?.updateValueAndValidity();
    crmControl?.updateValueAndValidity();
  }

  cadastrar() {
    if (this.formCadastroUsuario.invalid) {
      this.toastr.warning('Preencha todos os campos obrigatórios!');
      return;
    }

    if (
      this.formCadastroUsuario.value.senha !==
      this.formCadastroUsuario.value.confirmarSenha
    ) {
      this.toastr.warning('As senhas não coincidem!');
      return;
    }

    const dados = this.formCadastroUsuario.value;

    if (this.grupoSelecionado === Group.PATIENT) {
      const patientData = {
        nome: dados.nome,
        email: dados.email,
        senha: dados.senha,
        cpf: dados.cpf,
      };

      this.patientService.addPatient(patientData).subscribe({
        next: () => this.sucessoCadastro('Paciente'),
        error: () => this.erroCadastro(),
      });
    } else if (this.grupoSelecionado === Group.Doctor) {
      const doctorData = {
        nome: dados.nome,
        email: dados.email,
        senha: dados.senha,
        crm: dados.crm,
      };

      this.doctorService.cadastrar(doctorData).subscribe({
        next: () => this.sucessoCadastro('Médico'),
        error: () => this.erroCadastro(),
      });
    }
  }

  private sucessoCadastro(tipo: string) {
    this.toastr.success(`${tipo} cadastrado com sucesso!`);
    this.fecharModal();
  }

  private erroCadastro() {
    this.toastr.error('Erro ao cadastrar. Tente novamente.');
  }
}
