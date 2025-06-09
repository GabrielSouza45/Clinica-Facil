import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../infra/auth.service';

@Component({
  selector: 'app-menu-lateral',
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './menu-lateral.component.html',
  styleUrl: './menu-lateral.component.css'
})
export class MenuLateralComponent {
  grupo: string = '';
  administrador: boolean = false;
  doutor: boolean = false;
  paciente: boolean = false;
  userLogado: string = this.capitalizarPrimeiraLetra(sessionStorage.getItem('name') || "Nao Logado");

  constructor(
    public router: Router,
    public authService : AuthService,
  ){
    this.grupo = sessionStorage.getItem("group") || '';

    if (this.grupo == "ADMIN") {
    this.administrador = true;
    }

    if (this.grupo === "DOCTOR") {
      this.doutor = true;
    }

    if (this.grupo === "PATIENT") {
      this.paciente = true;
    }
  }

  capitalizarPrimeiraLetra(input: string): string {
    if (!input) return input;

    return input.charAt(0).toUpperCase() + input.slice(1);
  }

  isActive(route: string): boolean{
    return this.router.url === route;
  }

  logOut(){
    this.authService.logout();
  }

}
