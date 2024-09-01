import { Component, ElementRef, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { IconsModule } from '../../icons/icons.module';
import { RequestService } from '../../services/request/request.service';
import { RegisterRequest } from '../../interfaces/requests/register-request.interface';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-experimental-class',
  standalone: true,
  imports: [FormsModule, IconsModule, ReactiveFormsModule],
  templateUrl: './trial-class.component.html',
  styleUrls: ['./trial-class.component.scss'],
  providers: [RequestService],
})
export class ExperimentalClassComponent {
  userEmail: string = '';
  userName: string = '';
  userPhone: string = '';
  userInterest: string = '';
  userAge: string = '';
  form: FormGroup;

  @ViewChild('phoneError') phoneError!: ElementRef<HTMLSpanElement>;
  @ViewChild('emailError') emailError!: ElementRef<HTMLSpanElement>;

  constructor(
    private requestService: RequestService,
    private router: Router,
    private fb: FormBuilder,
    private location: Location
  ) {
    this.form = this.fb.group({
      userEmail: ['', [Validators.required, Validators.email]],
      userName: ['', Validators.required],
      userPhone: [
        '',
        [Validators.required, Validators.pattern(/^\(\d{2}\) \d{5}-\d{4}$/)],
      ],
      userInterest: ['', Validators.required],
      userAge: ['', Validators.required],
    });
  }

  goBack() {
    console.log('go back');
    this.location.back();
  }

  submit(): void {
    const loginData = {
      email: this.userEmail,
      name: this.userName,
      phone: this.userPhone,
      interest: this.userInterest,
      age: this.userAge,
    };

    const request: RegisterRequest = {
      userEmail: loginData.email,
      userName: loginData.name,
      userPhone: loginData.phone,
      description: `Nome: ${loginData.name}, Email: ${loginData.email}, Telefone: ${loginData.phone}, Interesse: ${loginData.interest}, Idade: ${loginData.age}`,
    };

    this.requestService.createRegisterRequest(request).subscribe(() => {
      this.router.navigateByUrl('/login');
      window.alert('Requisição de cadastrado concluída.');
    });
  }
}
