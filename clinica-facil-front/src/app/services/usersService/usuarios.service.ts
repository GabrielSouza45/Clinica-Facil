import { Injectable } from '@angular/core';
import { Usuario } from '../../model/Usuario';
import { CrudService } from '../crudService/crud.service';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class UsuariosService extends CrudService<Usuario> {

  constructor(
    private httpCliente: HttpClient,
    private toastr: ToastrService
  ) {
    super('/users', httpCliente, toastr);
  }

  listarTodosAtivos() {
    return this.doGet('/get-all-actives');
  }

  getByName(name: string) {
    return this.doGet('/get-by-name/' + name);
  }

}
