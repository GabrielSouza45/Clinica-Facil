import { Status } from "./enums/Status";

export class Doctor {
  id?: number;
  name?: string;
  email?: string;
  password?: string;
  birth?: Date;
  crm?: string;
  status?: Status;
}
