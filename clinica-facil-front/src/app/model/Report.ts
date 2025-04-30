import { Doctor } from "./Doctor";
import { Patient } from "./Patient";

export class Report{
  id?: number;
  patient?: Patient;
  doctor?: Doctor;
  issueData?: Date;
  reasons?: string;
  clinicalHistory?: string;
  diagnosis?: string;
  // revenues?: Revenue[];
  // exams?: Exam[];
  // consultation?: Consultation[];

}
