import { Consultation } from "./Consultation";
import { Doctor } from "./Doctor";
import { Patient } from "./Patient";

export class Report{
  constructor(
    public body: any,
    public type: string,
    public url: string
  ) {}

  toJSON() {
    return {
      body: this.body,
      type: this.type,
      url: this.url
    };
  }
  
  id?: number;
  patientId?: number;
  doctorId?: number;
  issueDate?: Date;
  reasons?: string;
  clinicalHistory?: string;
  diagnosis?: string;
  consultation?: Consultation;
}
