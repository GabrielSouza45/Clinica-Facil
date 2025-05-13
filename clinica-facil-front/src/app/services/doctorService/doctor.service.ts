import { Doctor } from './../../model/Doctor';
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';
import { CrudService } from '../crudService/crud.service';
import { HttpClient } from '@angular/common/http';
import { catchError, tap, throwError } from 'rxjs';

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
    return this.doPost('/add', doctor)
      .pipe(
        tap(() => {
          this.toastr.success("Cadastrado com sucesso.")
        }),
        catchError(error => {
          this.toastr.error("Erro ao cadastrar.")
          return throwError(() => error);
        })
      );
  }


  getAllDoctors(){
    return this.doGet('/get-all');
  }

  getDoctorsByName(doctorName: string) {
    return this.doGet(`/get-name/${doctorName}`);
  }

  getDoctorsByEmail(doctorMail: string) {
    return this.doGet(`/get-email/${doctorMail}`);
  }

  editDoctors(doctor: Doctor) {
    return this.doPut('/edit', doctor);
  }

  deleteDoctors(doctor: Doctor){
    return this.doPut('/delete', doctor);
  }
}
