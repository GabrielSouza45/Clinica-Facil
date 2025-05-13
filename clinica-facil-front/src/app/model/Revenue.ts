import { Doctor } from "./Doctor";
import { MedicalRecord } from "./MedicalRecord";
import { Patient } from "./Patient";

export class Revenue{
  id?: number;
  medications?: string;
  dosage?: string;
  recommendations?: string;
  dateTime?: Date;
  doctor?: Doctor;
  patient?: Patient;
  medicalRecord?: MedicalRecord;
}
