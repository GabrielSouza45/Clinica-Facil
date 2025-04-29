import { Injectable } from '@angular/core';
import { CrudService } from '../crudService/crud.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ProntuarioService extends CrudService<any>{

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/', httpCliente, toastr);
  }
}
