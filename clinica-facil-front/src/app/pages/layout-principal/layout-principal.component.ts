import { Component, Input } from '@angular/core';
import { MenuLateralComponent } from "../../components/menu-lateral/menu-lateral/menu-lateral.component";

@Component({
  selector: 'app-layout-principal',
  imports: [MenuLateralComponent],
  templateUrl: './layout-principal.component.html',
  styleUrl: './layout-principal.component.css'
})
export class LayoutPrincipalComponent {
  @Input() titulo:string = "";
  @Input() classMain: string = "mainTitulo";
}
