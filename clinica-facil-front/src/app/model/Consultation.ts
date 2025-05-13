import { MedicalRecord } from './MedicalRecord';
import { Doctor } from "./Doctor";
import { Patient } from "./Patient";
import { StatusConsultation } from './enums/StatusConsultation';

export class Consultation {
  id?: number;
  dateTime?: Date;
  patientId?: number;
  doctorId?: number;
  specialty?: string;
  report?: Report;
  medicalRecord?: MedicalRecord;
  status?: StatusConsultation;
}
