import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IconsModule } from '../../icons/icons.module';
import {
  AuthApiService,
  isPasswordCorrect,
} from '../../services/auth/auth-api.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, IconsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  providers: [AuthApiService],
})
export class LoginComponent {
  @ViewChild('passwordInput') passwordInput!: ElementRef<HTMLInputElement>;
  @ViewChild('errorMessage') errorMessage!: ElementRef<HTMLSpanElement>;
  isPasswordVisible: boolean = false;
  isPasswordCorrect: boolean = false;
  username: string = '';
  password: string = '';

  constructor(private authApiService: AuthApiService) {}

  submit(): void {
    this.authApiService.onLogin(this.username, this.password);
    this.showError();
  }

  showError(): void {
    if (!isPasswordCorrect) {
      this.errorMessage.nativeElement.style.display = 'block';
    }
  }

  togglePasswordVisibility(): void {
    this.isPasswordVisible = !this.isPasswordVisible;
    const input = this.passwordInput.nativeElement;
    input.type = input.type === 'password' ? 'text' : 'password';
  }
}
