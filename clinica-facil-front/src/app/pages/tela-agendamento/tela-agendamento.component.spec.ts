import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TelaAgendamentoComponent } from './tela-agendamento.component';

describe('TelaAgendamentoComponent', () => {
  let component: TelaAgendamentoComponent;
  let fixture: ComponentFixture<TelaAgendamentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TelaAgendamentoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TelaAgendamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
