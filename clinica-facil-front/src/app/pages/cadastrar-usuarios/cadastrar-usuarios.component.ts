import { Component } from '@angular/core';
import { MenuLateralComponent } from "../../components/menu-lateral/menu-lateral/menu-lateral.component";
import { LayoutPrincipalComponent } from "../layout-principal/layout-principal.component";

@Component({
  selector: 'app-cadastrar-usuarios',
  imports: [MenuLateralComponent, LayoutPrincipalComponent],
  templateUrl: './cadastrar-usuarios.component.html',
  styleUrl: './cadastrar-usuarios.component.css'
})
export class CadastrarUsuariosComponent {

}
