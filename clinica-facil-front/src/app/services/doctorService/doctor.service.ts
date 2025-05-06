import { Doctor } from './../../model/Doctor';
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';
import { CrudService } from '../crudService/crud.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DoctorService extends CrudService<Doctor> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/doctor', httpCliente, toastr);
  }

  cadastrar(doctor: Doctor) {
    return this.doPost('/add', doctor).subscribe({
      next: (response) => {
        this.toastr.success("Cadastrado com sucesso!");
      },
      error: (error) => {
        this.toastr.error("Erro inesperado.");
      }
    });
  }

}
