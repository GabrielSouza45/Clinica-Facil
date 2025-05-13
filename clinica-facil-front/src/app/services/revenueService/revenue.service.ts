import { Injectable } from '@angular/core';
import { CrudService } from '../crudService/crud.service';
import { Revenue } from '../../model/Revenue';
import { ToastrService } from 'ngx-toastr';
import { HttpClient, HttpParams } from '@angular/common/http';
import { formatDate } from '../formatDate';

@Injectable({
  providedIn: 'root'
})
export class RevenueService extends CrudService<Revenue> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/revenue', httpCliente, toastr);
  }


  getAllRevenues(){
    return this.doGet('');
  }

  getRevenueById(id: number){
    return this.doGet(`/${id}`);
  }


  getByDoctor(doctorId: number){
    return this.doGet(`/by-doctor/${doctorId}`);
  }

  getRevenueByDate(date: Date){
    const params = new HttpParams().set('date', formatDate(date));
    return this.doGet(`/by-date`, params);
  }

  updateRevenue(id: number, updatedRevenue: Revenue){
    return this.doPut(`/edit/${id}`, updatedRevenue);
  }

  addRevenueToMedicalRecord(medicalRecordId: number, revenue: Revenue){
    return this.doPost(`/add/${medicalRecordId}`, revenue);
  }
}
