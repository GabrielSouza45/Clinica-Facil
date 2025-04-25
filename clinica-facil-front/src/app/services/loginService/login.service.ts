import { Injectable } from '@angular/core';
import { Login } from '../../model/Login';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from '../../model/LoginResponse';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginObj!: Login;

  constructor(private httpClient: HttpClient) { }

  login(email: string, password: string): Observable<LoginResponse> {
    this.loginObj = new Login(
      email,
      password
    );

    return this.httpClient
      .post<LoginResponse>("http://localhost:8080/auth/login", this.loginObj)
      .pipe(
        tap((value) => {
          sessionStorage.setItem("token", value.token);
          sessionStorage.setItem("name", value.name);
          sessionStorage.setItem("id", value.id.toString());
          sessionStorage.setItem("group", value.group);
        })
      )
  }
}
