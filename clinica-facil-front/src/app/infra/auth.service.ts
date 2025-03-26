import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { LoginService } from '../services/loginService/login.service';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isUserAuthenticated = new BehaviorSubject<boolean>(false);
  private userPermission = new BehaviorSubject<string | null>(null);

  constructor(
    private loginService: LoginService,
    private router: Router,
    private ngZone: NgZone
  ) {
    this.checkAutentication();
  }

  login(email: string, password: string) {
    sessionStorage.clear();
    return this.loginService.login(email, password).pipe(
      tap((response) => {
        this.isUserAuthenticated.next(true);
        this.userPermission.next(response.group);
      })
    );
  }

  logout() {
    sessionStorage.clear();
    this.isUserAuthenticated.next(false);
    this.userPermission.next(null);
    this.ngZone.run(() => {
      this.router.navigate(['/']);
    });
  }


  private checkAutentication() {
    const token = sessionStorage.getItem('token');
    if (token) {
      this.isUserAuthenticated.next(true);
      this.userPermission.next(sessionStorage.getItem('group') || null);
    } else {
      this.isUserAuthenticated.next(false);
    }
  }

  isAuthenticated(): boolean {
    return this.isUserAuthenticated.value;
  }

  getUserName(){
    return sessionStorage.getItem('name');
  }

  getUserRole(): string | null {
    return this.userPermission.value;
  }

  getToken(): string {
    return sessionStorage.getItem('token') || '';
  }

  getIdUser(): number{
    return Number(sessionStorage.getItem('id'));
  }

  isCliente(): boolean {
    const role = this.getUserRole();
    if (!role) return false;
    return this.getUserRole() === "CLIENT";
  }
}
