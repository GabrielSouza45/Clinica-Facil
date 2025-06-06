import { Patient } from "./Patient";
import { MedicalRecord } from "./MedicalRecord";

export class Exam{
  id?: number;
  name?: string;
  description?: string;
  dateTime?: Date;
  results?: string;
  patient?: Patient;
  medicalRecord?: MedicalRecord;
  date: string;
}
