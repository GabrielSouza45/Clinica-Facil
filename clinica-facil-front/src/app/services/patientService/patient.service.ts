import { Injectable } from '@angular/core';
import { Patient } from '../../model/Patient';
import { CrudService } from '../crudService/crud.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { catchError, tap, throwError } from 'rxjs';
import { error } from 'console';

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

  getAllPatientsActives(){
    return this.doGet('/get-all-actives');
  }

  getAllPatients(){
    return this.doGet('/get-all');
  }

  getPatientsByName(patientName: string){
    return this.doGet(`/get-name/${patientName}`);
  }

  getPatientByEmail(patientEmail: string){
    return this.doGet(`/get-email/${patientEmail}`);
  }

  addPatient(patient: Patient){
    return this.doPost("/add", patient)
    .pipe(
      tap(() => {
        this.toastr.success("Cadastrado com sucesso.");
      }),
      catchError(err => {
        this.toastr.error("Erro ao cadastrar.");
        return throwError(() => err);
      })
    )
  }

  editPatient(patient: Patient){
    return this.doPut("/edit", patient)
  }

  removePatientByEmail(email: string){
    return this.doPut(`/delete/${email}`)
  }
}
