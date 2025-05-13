import { HttpClient, HttpParams } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { Filtros } from '../../model/Filtros';

@Injectable({
  providedIn: 'root'
})
export class CrudService<T> {
  public domain: string = 'http://localhost:8080';
  public url!: string;

  constructor(
    @Inject(String) private path: string,
    private httpClient: HttpClient,
    private toastrService: ToastrService
  ) {
    this.url = this.domain + path;
  }

  doGet(acao: string, params?: HttpParams): Observable<any> {
    return this.httpClient.get<any>(this.url + acao, {params});
  }

  doPost(acao: string, dados?: any): Observable<any> {
    return this.httpClient.post<any>(this.url + acao, dados);
  }

  doPut(acao: string, dados?: any): Observable<any> {
    return this.httpClient.put<any>(this.url + acao, dados);
  }

  doDelete(acao: string) {
    return this.httpClient.delete<any>(this.url + acao);
  }
}
