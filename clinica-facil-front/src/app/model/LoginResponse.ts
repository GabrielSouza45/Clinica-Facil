import { Group } from "./enums/Group"

export type LoginResponse = {
  token: string,
  name: string,
  group: Group,
  id: number
}
