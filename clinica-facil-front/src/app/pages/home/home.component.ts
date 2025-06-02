import { Component } from '@angular/core';
import { MenuLateralComponent } from "../../components/menu-lateral/menu-lateral/menu-lateral.component";
import { LayoutPrincipalComponent } from "../layout-principal/layout-principal.component";

@Component({
  selector: 'app-home',
  imports: [LayoutPrincipalComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
