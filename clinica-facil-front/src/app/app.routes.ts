import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { CadastrarUsuariosComponent } from './pages/cadastrar-usuarios/cadastrar-usuarios.component';
import { MenuLateralComponent } from './components/menu-lateral/menu-lateral/menu-lateral.component';
import { TelaAgendamentoComponent } from './pages/tela-agendamento/tela-agendamento.component';

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
    component: CadastrarUsuariosComponent
  },
  {
    path:'menu-lateral',
    component: MenuLateralComponent
  },
  {
    path:'consultas-agendadas',
    component: HomeComponent // ALTERAR
  },
  {
    path:'agendamento',
    component: TelaAgendamentoComponent // ALTERAR
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
