import { Doctor } from "./Doctor";
import { Patient } from "./Patient";

export class Consultation {
  id?: number;
  patient?: Patient;
  doctor?: Doctor;
  specialty?: string;
  report?: Report;
  
}
