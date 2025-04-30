import { Consultation } from "./Consultation";
import { Patient } from "./Patient";

export class Prontuario{
  id?: number;
  patient?: Patient;
  consultations?: Consultation[];
  // exam?: Exam[];
  // revenue?: Revenue[];

}
