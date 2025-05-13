import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { InputPrimarioComponent } from '../../components/input-primario/input-primario.component';
import { PaginaInicialLayoutComponent } from "../../components/back-office/pagina-inicial-layout/pagina-inicial-layout.component";
import { ModalComponent } from "../../components/modal/modal.component";

@Component({
  selector: 'app-cadastrar-usuarios',
  templateUrl: './cadastrar-usuarios.component.html',
  styleUrls: ['./cadastrar-usuarios.component.css'],
  standalone: true,
  imports: [
    InputPrimarioComponent,
    ReactiveFormsModule
]
})
export class CadastrarUsuariosComponent implements OnInit {
  formCadastro!: FormGroup;

  ngOnInit() {
    this.formCadastro = new FormGroup({
      nome: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      senha: new FormControl('', Validators.required),
      confirmarSenha: new FormControl('', Validators.required),
      tipo: new FormControl('PATIENT', Validators.required),
      cpf: new FormControl(''),
      crm: new FormControl('')
    });
  }

  cadastrar() {
    if (this.formCadastro.invalid) {
      alert('Preencha todos os campos obrigatórios!');
      return;
    }

    const tipo = this.formCadastro.get('tipo')?.value;

    // Exibir valores no console ou enviar para backend
    console.log('Dados do cadastro:', this.formCadastro.value);

    alert(`${tipo} cadastrado com sucesso!`);
    this.formCadastro.reset({ tipo: 'PATIENT' });
  }
}
