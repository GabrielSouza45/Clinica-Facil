import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { PageChangedEvent, PaginationModule } from 'ngx-bootstrap/pagination';
import { FormsModule } from '@angular/forms';
import { log } from 'console';

@Component({
  selector: 'app-table-pagination',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationModule],
  templateUrl: './table-pagination.component.html',
  styleUrl: './table-pagination.component.css',
})
export class TablePaginationComponent {
  @Input() dados: any[] = [];
  @Input() acoes: any[] = [];
  @Input() headerInput!: { [key: string]: string };
  colunas: string[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    console.log('input: ', this.headerInput);
    console.log('colunas: ', this.colunas);

    // Caso headerInput esteja definido e com conteúdo
    if (this.headerInput && Object.keys(this.headerInput).length > 0) {
      this.colunas = Object.values(this.headerInput);
      console.log('Usando headerInput:', this.colunas);
    }
    // Caso contrário, tenta usar os dados
    else if (changes['dados'] && this.dados.length > 0) {
      this.colunas = Object.keys(this.dados[0]);
      console.log('Usando colunas dos dados:', this.colunas);
    } else {
      console.log('Nenhuma coluna detectada');
    }

    console.log('Dados recebidos:', this.dados);
  }

  // PAGINATION
  @Input() totalItens: number = 50;
  @Output() pageChangeEvent = new EventEmitter<number>();
  page?: number = 1;
}
