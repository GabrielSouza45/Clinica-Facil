import { Injectable } from '@angular/core';
import { CrudService } from '../crudService/crud.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { formatDate } from '../formatDate';

@Injectable({
  providedIn: 'root'
})
export class ReportService extends CrudService<Report> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/report', httpCliente, toastr);
  }


  getAllReports(){
    return this.doGet('/all');
  }

  getReportByPatient(patientId: number){
    return this.doGet(`/by-patient/${patientId}`);
  }

  getReportAtDate(date: Date){
    const params = new HttpParams().set('date', formatDate(date));
    return this.doGet('/by-date', params);
  }

  updateReport(id: number, updatedReport: Report){
    return this.doPut(`/edit/${id}`, updatedReport);
  }
}
