import { Injectable } from '@angular/core';
import { CrudService } from '../crudService/crud.service';
import { Agenda } from '../../model/Agenda';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Doctor } from '../../model/Doctor';

@Injectable({
  providedIn: 'root'
})
export class AgendaService extends CrudService<Agenda>{

  constructor(
    private toastr: ToastrService,
    private http: HttpClient
  ) {
    super('/agenda', http, toastr);
  }

  getAllAgendas(){
    return this.doGet('/get-all');
  }

  getAgendasByDoctor(doctor: Doctor) {
    return this.doPost('/by-doctor', doctor);
  }

  getAvailableAgendasByDoctor(doctor: Doctor){
    return this.doPost('/available-by-doctor', doctor);
  }

  getAgendasByDateRange(start: Date, end: Date){
    return this.doGet(`/by-date-range?start=${start}&end=${end}}`);
  }

  createAgenda(agenda: Agenda) {
    return this.doPost('', agenda);
  }

  updateAgenda(agenda: Agenda){
    return this.doPut('', agenda);
  }

  deleteAgenda(id: number){
    return this.doDelete(`/${id}`);
  }
}
