import { Injectable } from '@angular/core';
import { Patient } from '../../model/Patient';
import { CrudService } from '../crudService/crud.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class PatientService  extends CrudService<Patient> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/patient', httpCliente, toastr);
  }

  cadastrar(patient: Patient) {
    return this.doPost('/add', patient).subscribe({
      next: (response) => {
        this.toastr.success("Cadastrado com sucesso!");
      },
      error: (error) => {
        this.toastr.error("Erro inesperado.");
      }
    });
  }
}
