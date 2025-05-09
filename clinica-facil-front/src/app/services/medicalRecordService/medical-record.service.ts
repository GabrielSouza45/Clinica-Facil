import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CrudService } from '../crudService/crud.service';
import { MedicalRecord } from '../../model/MedicalRecord';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService extends CrudService<MedicalRecord> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/medical-records', httpCliente, toastr);
  }

  getMedicalRecordsByPatient(patientId: number){
    return this.doGet(`/by-patient/${patientId}`);
  }

  getMedicalRecordById(id: number){
    return this.doGet(`/${id}`);
  }

  deleteMedicalRecord(id: number){
    return this.doDelete(`/${id}`);
  }
}
