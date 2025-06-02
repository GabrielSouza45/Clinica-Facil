import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { MenuLateralComponent } from "../../../components/menu-lateral/menu-lateral/menu-lateral.component";
import { ModalComponent } from "../../../components/modal/modal.component";

@Component({
  selector: 'app-tela-prontuario',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    MenuLateralComponent,
    ModalComponent
  ],
  templateUrl: './prontuario.component.html',
  styleUrls: ['./prontuario.component.css']
})
export class ProntuarioComponent {
  formProntuario!: FormGroup;
  prontuarios: any[] = [];
  prontuarioSelecionado: any = null;
  editando = false;

  constructor(private fb: FormBuilder, private toastr: ToastrService) {
    this.inicializarFormulario();
    this.carregarDadosExemplo();
  }

  inicializarFormulario(): void {
    this.formProntuario = this.fb.group({
      id: [null],
      paciente: ['', Validators.required],
      dataConsulta: ['', Validators.required],
      itens: this.fb.array([])
    });

    // Adiciona um item inicial
    this.adicionarItem();
  }

  carregarDadosExemplo(): void {
    this.prontuarios = [
      {
        id: 1,
        paciente: 'Carlos Souza',
        dataConsulta: '2025-05-28',
        itens: [
          { realizado: 'Avaliação da garganta', causa: 'Inflamação', procedimento: 'Palito de picolé' },
          { realizado: 'Aferição de pressão', causa: 'Monitoramento', procedimento: 'Esfigmomanômetro' }
        ]
      }
    ];
  }

  get itens(): FormArray {
    return this.formProntuario.get('itens') as FormArray;
  }

  adicionarItem(): void {
    const itemForm = this.fb.group({
      realizado: ['', Validators.required],
      causa: ['', Validators.required],
      procedimento: ['', Validators.required]
    });

    this.itens.push(itemForm);
  }

  removerItem(index: number): void {
    this.itens.removeAt(index);
  }

  editarProntuario(prontuario: any): void {
    this.editando = true;
    this.prontuarioSelecionado = prontuario;
    
    // Limpa os itens existentes
    while (this.itens.length) {
      this.itens.removeAt(0);
    }
    
    // Preenche o formulário com os dados do prontuário
    this.formProntuario.patchValue({
      id: prontuario.id,
      paciente: prontuario.paciente,
      dataConsulta: prontuario.dataConsulta
    });
    
    // Adiciona os itens do prontuário
    prontuario.itens.forEach((item: any) => {
      const itemGroup = this.fb.group({
        realizado: [item.realizado, Validators.required],
        causa: [item.causa, Validators.required],
        procedimento: [item.procedimento, Validators.required]
      });
      this.itens.push(itemGroup);
    });
  }

  visualizarProntuario(prontuario: any): void {
    this.prontuarioSelecionado = prontuario;
  }

  salvarProntuario(): void {
    if (this.formProntuario.invalid) {
      this.toastr.warning('Preencha todos os campos obrigatórios!');
      return;
    }

    const prontuarioData = this.formProntuario.value;

    if (this.editando) {
      // Atualiza prontuário existente
      const index = this.prontuarios.findIndex(p => p.id === prontuarioData.id);
      if (index !== -1) {
        this.prontuarios[index] = prontuarioData;
      }
    } else {
      // Adiciona novo prontuário
      prontuarioData.id = this.prontuarios.length > 0 
        ? Math.max(...this.prontuarios.map(p => p.id)) + 1 
        : 1;
      this.prontuarios.push(prontuarioData);
    }

    this.toastr.success('Prontuário salvo com sucesso!');
    this.limparFormulario();
  }

  limparFormulario(): void {
    this.formProntuario.reset();
    while (this.itens.length) {
      this.itens.removeAt(0);
    }
    // Adiciona um item vazio após limpar
    this.adicionarItem();
    this.prontuarioSelecionado = null;
    this.editando = false;
  }

  cancelarEdicao(): void {
    this.limparFormulario();
  }

  excluirProntuario(id: number): void {
    this.prontuarios = this.prontuarios.filter(p => p.id !== id);
    this.toastr.success('Prontuário excluído com sucesso!');
    if (this.prontuarioSelecionado && this.prontuarioSelecionado.id === id) {
      this.prontuarioSelecionado = null;
    }
  }
}