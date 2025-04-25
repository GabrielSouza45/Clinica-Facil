import { Component } from '@angular/core';
import { LayoutPrincipalComponent } from "../layout-principal/layout-principal.component";
import { MenuLateralComponent } from "../../components/menu-lateral/menu-lateral/menu-lateral.component";

@Component({
  selector: 'app-home',
  imports: [LayoutPrincipalComponent, MenuLateralComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
