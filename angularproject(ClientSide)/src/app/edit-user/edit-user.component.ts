import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserModel } from '../models/user';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../services/users.service';
import { Subscription } from 'rxjs/Subscription';
import { AppGlobals } from '../globals/app.globals';
import { TokenStorage } from '../services/token.storage';

@Component({
  selector: "app-edit-user",
  templateUrl: "./edit-user.component.html",
  styleUrls: ["./edit-user.component.css"]
})
export class EditUserComponent implements OnInit {
  public editUserForm: FormGroup;
  public user: UserModel;
  public bosses: UserModel[];
  public userServiceSubscription: Subscription;
  public userId: number;
  public messages: string;
  public myEmail;
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private userService: UsersService,
    private appGlobals: AppGlobals,
    private tokenStorage: TokenStorage
  ) {
    this.userId = +this.activatedRoute.snapshot.params.id;
  }

  ngOnInit() {
    this.resolvePromiseReturningUserFromId();
    this.initFormEditUser(true);
    this.subscribeToUserService();
  }
  getUserEmail() {
    this.myEmail = this.appGlobals.getUserEmail(this.tokenStorage.getToken());
  }

  initFormEditUser(isEmpty: boolean) {
    this.editUserForm = this.formBuilder.group({
      firstName: [isEmpty ? "" : this.user.firstName, Validators.required],
      lastName: [isEmpty ? "" : this.user.lastName, Validators.required],
      email: [
        isEmpty ? "" : this.user.email,
        [Validators.required, Validators.email]
      ],
      password: ["anything", Validators.required],
      role: [isEmpty ? "" : this.user.role, Validators.required],
      hisBoss: [
        isEmpty
          ? ""
          : this.user && this.user.hisBoss
            ? this.user.hisBoss.id
            : ""
      ]
    });
  }

  resolvePromiseReturningUserFromId() {
    this.userService
      .getOneUserById(this.userId)
      .then((resolvedData: UserModel) => {
        this.user = resolvedData;
        this.initFormEditUser(false);
      });
  }

  subscribeToUserService() {
    this.userService.getBosses();
    this.userServiceSubscription = this.userService.usersSubject.subscribe(
      (emittedData: UserModel[]) => {
        this.bosses = emittedData;
        this.getUserEmail();
      }
    );
  }

  onSubmitEditUserForm() {
    const formValuesObject = this.editUserForm.value;
    console.log(formValuesObject);
    const boss = this.userService.findUserByIdFromList(
      +formValuesObject.hisBoss
    );
    formValuesObject["hisBoss"] = boss;
    const user: UserModel = this.appGlobals.createUserModelFromObject(
      formValuesObject
    );
    console.log(user);
    this.userService.updateUser(this.userId, user).then(
      (dataResolved: boolean) => {
        console.log(dataResolved);
        if (dataResolved === true) {
          this.messages = "utilisateur modifé avec success";
        } else {
          this.messages =
            "une erreur est survenue lors de la modification,veuillez ressayer";
        }
      },
      error => {
        console.log(error);
      }
    );
  }
}
