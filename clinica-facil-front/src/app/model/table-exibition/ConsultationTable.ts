import { StatusConsultation } from "../enums/StatusConsultation";

export default interface ConsultationTable {
  id?: number;
  patientName?: string;
  patientCpf?: string;
  doctorName?: string;
  doctorCrm?: string;
  dateTime?: Date;
  speciality?: string;
  status?: StatusConsultation;
}

export const consultationTableHeaders: { [key in keyof ConsultationTable]: string } = {
  id: "#",
  patientName: "Nome do Paciente",
  patientCpf: "CPF do Paciente",
  doctorName: "Nome do Médico",
  doctorCrm: "CRM do Médico",
  dateTime: "Data e hora da consulta",
  speciality: "Especialidade",
  status: "Status"
};
