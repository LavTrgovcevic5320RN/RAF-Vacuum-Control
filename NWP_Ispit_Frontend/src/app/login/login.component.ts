import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl, ReactiveFormsModule, FormsModule,} from '@angular/forms';
import { AuthService } from '../service/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, FormsModule],
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl(''),
    password: new FormControl(''),
  });

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  async onSubmit(): Promise<void> {
    console.log(this.loginForm);
    const { email, password } = this.loginForm.value;
    try {
      const response = await this.authService.login(email ?? 'na', password ?? 'na').subscribe((res) => {
        if(res.jwt){
          this.authService.setToken(res.jwt);
          alert("Success!");
        }
      }, (err) => {
        console.log(err)
      })
    } catch (error) {
      console.error('Login failed:', error);
    }
  }
}
