import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
  {
    path: '',
    component: LoginComponent
  },
  {
    path:'home',
    component: HomeComponent
  },
  {
    path:'cadastrar-usuarios',
    component: HomeComponent // ALTERAR
  },
  {
    path:'consultas-agendadas',
    component: HomeComponent // ALTERAR
  },
  {
    path:'agendamento',
    component: HomeComponent // ALTERAR
  },
  {
    path:'consultar-prontuario',
    component: HomeComponent // ALTERAR
  },
  {
    path:'adicionar-prontuario',
    component: HomeComponent // ALTERAR
  }
];
