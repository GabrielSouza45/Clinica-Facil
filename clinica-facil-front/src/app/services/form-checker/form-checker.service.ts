import { FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormCheckerService {
  constructor(private toastrService: ToastrService) { }

  checkLoginForm(formGroup: FormGroup): boolean {
    const controls = formGroup.controls;
    let valid: boolean = true;

    if (controls['email'].errors) {
      if (controls['email'].errors['required']) {
        this.toastrService.warning("O campo de email é obrigatório.");
      } else if (controls['email'].errors['email']) {
        this.toastrService.warning("O email inserido não é válido.");
      }
      valid = false;

    } else if(controls['password'].errors){
      this.toastrService.warning("O campo de senha é obrigatório.");
      valid = false;

    }

    return valid;
  }
}
