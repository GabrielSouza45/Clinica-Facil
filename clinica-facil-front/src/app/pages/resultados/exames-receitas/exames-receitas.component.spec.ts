import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamesReceitasComponent } from './exames-receitas.component';

describe('ExamesReceitasComponent', () => {
  let component: ExamesReceitasComponent;
  let fixture: ComponentFixture<ExamesReceitasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamesReceitasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExamesReceitasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
