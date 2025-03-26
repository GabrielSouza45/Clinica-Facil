import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class CrudService<T> {
  public domain: string = 'http://localhost:8080';
  public url!: string;

  constructor(
    @Inject(String) private path: string,
    private httpCliente: HttpClient,
    private toastrService: ToastrService
  ) {
    this.url = this.domain + path;
  }

  login() {}

  doGet() { }

  doPost() { }

  doPut() { }

  doDelete() { }
}
