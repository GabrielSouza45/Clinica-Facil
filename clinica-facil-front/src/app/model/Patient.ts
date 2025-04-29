import { Status } from "./enums/Status";

export class Patient {
  id?: number;
  name?: string;
  email?: string;
  password?: string;
  birth?: Date;
  cpf?: string;
  status?: Status;
}
