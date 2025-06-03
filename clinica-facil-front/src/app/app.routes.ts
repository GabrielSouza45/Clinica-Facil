import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { CadastrarUsuariosComponent } from './pages/cadastrar-usuarios/cadastrar-usuarios.component';
import { MenuLateralComponent } from './components/menu-lateral/menu-lateral/menu-lateral.component';
import { TelaAgendamentoComponent } from './pages/tela-agendamento/tela-agendamento.component';
import { ProntuarioComponent } from './pages/prontuario/prontuario/prontuario.component';

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
    path:'consultas-agendadas',
    component: TelaAgendamentoComponent
  },
   {
   path:'agendadar-consulta',
    component: TelaAgendamentoComponent
  },
  {
    path:'consultar-prontuario',
    component: ProntuarioComponent
  },
  {
    path:'consultar-resultados',
    component: HomeComponent
  }
];
