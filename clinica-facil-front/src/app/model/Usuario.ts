import { Status } from "./enums/Status";

export interface Usuario {
  id?: number;
  name?: string;
  email?: string;
  password?: string;
  birth?: Date;
  cpf?: string;
  crm?: string;
  status?: Status;
}
