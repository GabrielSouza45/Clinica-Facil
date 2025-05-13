import { Consultation } from "./Consultation";
import { Doctor } from "./Doctor";
import { Patient } from "./Patient";

export class Report{
  id?: number;
  patientId?: number;
  doctorId?: number;
  issueDate?: Date;
  reasons?: string;
  clinicalHistory?: string;
  diagnosis?: string;
  consultation?: Consultation;
}
