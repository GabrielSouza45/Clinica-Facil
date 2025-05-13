import { Doctor } from "./Doctor";

export class Agenda{
  id?: number;
  startDateTime?: Date;
  endDateTime?: Date;
  doctor?: Doctor;
  available?: boolean;
}
