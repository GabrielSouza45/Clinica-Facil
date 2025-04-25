import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BotaoComponent } from "../../components/botao/botao.component";
import { InputPrimarioComponent } from "../../components/input-primario/input-primario.component";
import { FormCheckerService } from '../../services/form-checker/form-checker.service';
import { AuthService } from './../../infra/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [InputPrimarioComponent, ReactiveFormsModule, BotaoComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm!: FormGroup;

  constructor(
    private formChecker: FormCheckerService,
    private authService: AuthService,
    private toastrService: ToastrService,
    private router: Router
  ) {
    this.initLoginForm();
  }

  submit(): void {
    const valid = this.formChecker.checkLoginForm(this.loginForm);
    if (!valid) return;

    this.authService.login(
      this.loginForm.value.email,
      this.loginForm.value.password
    )
      .subscribe({
        next: () => {
          this.toastrService.success("Login Realizado com sucesso!");
          this.redirectUser();
        },
        error: (erro) => {
          if (erro.status === 403) {
            this.toastrService.warning("Usuário não encontrado. Verifique suas credenciais e tente novamente.");
          } else {
            this.toastrService.error("Erro inesperado, tente novamente mais tarde.");
          }
        }
      });
  }

  private redirectUser() {
    this.router.navigate(['/home']);
  }

  private initLoginForm(): void {
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }
}
