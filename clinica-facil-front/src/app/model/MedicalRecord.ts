import { Consultation } from "./Consultation";
import { Exam } from "./Exam";
import { Patient } from "./Patient";
import { Revenue } from "./Revenue";

export class MedicalRecord{
  id?: number;
  patient?: Patient;
  consultations?: Consultation[];
  exams?: Exam[];
  revenues?: Revenue[];
  prescriptions: boolean;
}
