import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UsersService } from '../services/users.service';
import { UserModel } from '../models/user';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginUser } from '../models/loginUser';
import { AuthService } from '../services/authentification.service';
import { Subscription } from 'rxjs/Subscription';
import { TokenStorage } from '../services/token.storage';
import { Router } from '@angular/router';
import { TokenModel } from '../models/token';
import { AppGlobals } from '../globals/app.globals';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  public authSubscription: Subscription;
  public httpMessages;
  constructor(
    private userService: UsersService,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private tokenStorageService: TokenStorage,
    private router: Router
  ) {}

  ngOnInit() {
               this.initLoginForm();
               this.authSubscription = this.authService.authSubject.subscribe(
                 isAuthentified => {
                   if (isAuthentified === true) {
                     this.router.navigate(['']);
                   } else {
                     this.httpMessages = isAuthentified;
                   }
                 }
               );
             }

  initLoginForm() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmitLoginForm() {
    const formValues = this.loginForm.value;
    const email = formValues['email'];
    const password = formValues['password'];
    const user = new LoginUser(email, password);

    this.authService.attemptAuth(user);
  }


  styleDecision() {
       return 'text-center text-danger';
  }
}
