import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CrudService } from '../crudService/crud.service';
import { MedicalRecord } from '../../model/MedicalRecord';
import { Revenue } from '../../model/Revenue';
import { Observable } from 'rxjs/internal/Observable';
import { Exam } from '../../model/Exam';
import { throwError } from 'rxjs';

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

  createMedicalRecordWithConsultation(patientId: number, consultation: any) {
  return this.doPost(`/create/${patientId}`, consultation);
}

updateMedicalRecordWithConsultation(recordId: number, consultationData: any): Observable<any> {
  return this.doPost(`/api/medical-records/${recordId}`, consultationData);
}

getAll() {
  return this.doGet('');
}

addRevenueToMedicalRecord(medicalRecordId: number, revenue: Revenue): Observable<MedicalRecord> {
  return this.doPost(`/${medicalRecordId}/add-revenue`, revenue);
}

addExamToMedicalRecord(medicalRecordId: number, exam: Exam): Observable<MedicalRecord> {
    return this.doPost(`/${medicalRecordId}/add-exam`, exam);
  }

}
