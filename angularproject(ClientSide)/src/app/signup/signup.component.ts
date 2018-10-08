import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UsersService } from '../services/users.service';
import { UserModel } from '../models/user';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';


@Component({
  selector: "app-signup",
  templateUrl: "./signup.component.html",
  styleUrls: ["./signup.component.css"]
})
export class SignupComponent implements OnInit {
  signUpForm: FormGroup;
  userServiceSubscription: Subscription;
  public users: UserModel[];
  public httpErrors;
  constructor(
    private formBuilder: FormBuilder,
    private userService: UsersService
  ) {}

  ngOnInit() {
    this.initSignUpForm();
  }

  initSignUpForm() {
    this.signUpForm = this.formBuilder.group({
      firstName: ["", Validators.required],
      lastName: ["", Validators.required],
      email: ["", [Validators.required, Validators.email]],
      password: ["", Validators.required],
      checkPassword: ["", Validators.required],
      role: ['employee',Validators.required],
      hisBoss: ""
    });

    this.getAllBosses();
  }

  createUserFromForm(): UserModel {
    const values = this.signUpForm.value;
    const firstName = values["firstName"];
    const lastName = values["lastName"];
    const email = values["email"];
    const password = values["password"];
    const checkPassword = values["checkPassword"];
    const role = values["role"];
    let hisBoss: UserModel = values['hisBoss'];
    hisBoss = this.userService.findUserByIdFromList(+hisBoss);

    return new UserModel(firstName, lastName, email, password, hisBoss, role);
  }

  onSubmitSignUpForm() {
    const user = this.createUserFromForm();
    this.userService.addUser(user).then(
      data => {
        this.httpErrors = data;
      },
      (errors: HttpErrorResponse) => {
        this.httpErrors = errors.error;
      }
    );
  }

  styleDecision() {
    if (this.httpErrors["type"] === "ERROR") {
      return "text-center text-danger";
    } else {
      return "text-center text-success";
    }
  }

  getAllBosses() {
     this.userService.getBosses();
     this.userServiceSubscription = this.userService.usersSubject.subscribe(
       (emittedData: UserModel[]) => {
         this.users = emittedData;
       }
     );
  }
}

