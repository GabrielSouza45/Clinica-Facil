import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { catchError, tap, throwError } from 'rxjs';
import { Consultation } from '../../model/Consultation';
import { Report } from '../../model/Report';
import { CrudService } from '../crudService/crud.service';
import { formatDate } from '../formatDate';

@Injectable({
  providedIn: 'root'
})
export class ConsultationService extends CrudService<Consultation>{

  constructor(
    private toastr: ToastrService,
    private http: HttpClient
  ) {
    super('/consultas', http, toastr);
  }

  getAll(){
    return this.doGet('/all');
  }

  getByPatient(id: number){
    return this.doGet(`/by-patient?patientId=${id}`);
  }

  getByDoctor(id: number){
    return this.doGet(`/by-doctor?doctorId=${id}`);
  }

  getBySpecialty(specialty: string){
    const params = new HttpParams().set('specialty', specialty);

    return this.doGet(`/by-specialty`, params);
  }

  getConsultByDate(date: Date){
    const formattedDate = formatDate(date);
    const params = new HttpParams().set('date', formattedDate);

    return this.doGet(`/by-date?date=${date}`);
  }

  cancelConsultation(consultationId: number, report: Report){
    return this.doPut(`/cancel/${consultationId}`, report);
  }

  addReportAndFinalizeConsultation(consultationId: number, report: Report) {
    return this.doPut(`/finalize/${consultationId}`, report);
  }

  editConsultation(consultationId: number, updatedConsultation: Consultation) {
    return this.doPut(`/edit/${consultationId}`, updatedConsultation);
  }

  createConsultation(consultation: Consultation){
    return this.doPost('/add', consultation)
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
}
