import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Exam } from '../../model/Exam';
import { CrudService } from '../crudService/crud.service';
import { formatDate } from '../formatDate';

@Injectable({
  providedIn: 'root'
})
export class ExamService extends CrudService<Exam> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/exam', httpCliente, toastr);
  }


  getAllExams(){
    return this.doGet("/all");
  }

  getExamsByPatient(patientId: number) {
    return this.doGet(`/by-patient/${patientId}`);
  }

  getExamsByDate(date: Date) {
    const formattedDate = formatDate(date);
    const params = new HttpParams().set('date', formattedDate);

    return this.doGet(`/by-date`, params);
  }

  updateExam(examId: number, updatedExam: Exam) {
    return this.doPut(`/edit/${examId}`, updatedExam);
  }

  addExamToMedicalRecord(medicalRecordId: number, exam: Exam){
    return this.doPost(`/add/${medicalRecordId}`, exam)
  }

}
