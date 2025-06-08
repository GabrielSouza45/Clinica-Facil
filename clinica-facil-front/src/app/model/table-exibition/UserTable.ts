import { Status } from "../enums/Status";


export interface UserTable{
  id: number;
  email: string;
  name: string;
  birth: Date;
  group: string;
  cpf: string;
  crm: string;
  status: Status;
}
