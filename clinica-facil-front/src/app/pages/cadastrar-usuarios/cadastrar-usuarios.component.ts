import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Group } from '../../../app/model/enums/Group';
import { TablePaginationComponent } from "../../components/back-office/table-pagination/table-pagination.component";
import { InputPrimarioComponent } from '../../components/input-primario/input-primario.component';
import { ModalComponent } from '../../components/modal/modal.component';
import { UserTable } from '../../model/table-exibition/UserTable';
import { DoctorService } from '../../services/doctorService/doctor.service';
import { PatientService } from '../../services/patientService/patient.service';
import { LayoutPrincipalComponent } from "../layout-principal/layout-principal.component";
import { UsuariosService } from './../../services/usersService/usuarios.service';

@Component({
  selector: 'app-cadastrar-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    InputPrimarioComponent,
    ModalComponent,
    LayoutPrincipalComponent,
    TablePaginationComponent
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

  usuarios: UserTable[] = [];
  buscarForm!: FormGroup;

  constructor(
    private toastr: ToastrService,
    private usuariosService: UsuariosService,
    private patientService: PatientService,
    private doctorService: DoctorService
  ) {
    this.inicializarFormulario();
    this.pesquisar();
  }


  // PESQUISAR
  pesquisar() {
    const nome = this.buscarForm.value.nome || null;
    if (nome) {

      this.usuariosService.getByName(nome).subscribe((response: UserTable[]) => {
        this.usuarios = response.map(user => ({
          id: user.id,
          email: user.email,
          name: user.name,
          birth: user.birth,
          group: (user.cpf ? "Paciente" : "Médico"),
          cpf: user.cpf,
          crm: user.crm,
          status: user.status
        }));
      });

      this.buscarForm.reset();

    } else {

      this.usuariosService.listarTodosAtivos().subscribe((response: UserTable[]) => {
        console.log(response);

        this.usuarios = response.map(user => ({
          id: user.id,
          email: user.email,
          name: user.name,
          birth: user.birth,
          group: (user.cpf ? "Paciente" : "Médico"),
          cpf: user.cpf,
          crm: user.crm,
          status: user.status
        }));

      });

    }
  }

  inicializarFormulario() {
    this.formCadastroUsuario = new FormGroup({
      name: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      birth: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      confirmarSenha: new FormControl('', [Validators.required]),
      cpf: new FormControl(''),
      crm: new FormControl('')
    });

    this.buscarForm = new FormGroup({
      nome: new FormControl('', [Validators.required])
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
      this.formCadastroUsuario.value.password !==
      this.formCadastroUsuario.value.confirmarSenha
    ) {
      this.toastr.warning('As senhas não coincidem!');
      return;
    }

    const dados = this.formCadastroUsuario.value;

    if (this.grupoSelecionado === Group.PATIENT) {
      const patientData = {
        name: dados.name,
        email: dados.email,
        birth: dados.birth,
        password: dados.password,
        cpf: dados.cpf,
      };

      this.patientService.addPatient(patientData).subscribe({
        next: () => this.sucessoCadastro('Paciente'),
        error: () => this.erroCadastro(),
      });
    } else if (this.grupoSelecionado === Group.Doctor) {
      const doctorData = {
        name: dados.name,
        email: dados.email,
        birth: dados.birth,
        password: dados.password,
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
    this.pesquisar();
  }

  private erroCadastro() {
    this.toastr.error('Erro ao cadastrar. Tente novamente.');
  }
}
