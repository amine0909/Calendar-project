import { Component, OnInit } from '@angular/core';
import { TokenStorage } from '../services/token.storage';
import { UsersService } from '../services/users.service';
import { AppGlobals } from '../globals/app.globals';
import { Subscription } from 'rxjs/Subscription';
import { UserModel } from '../models/user';

@Component({
  selector: "app-my-employees",
  templateUrl: "./my-employees.component.html",
  styleUrls: ["./my-employees.component.css"]
})
export class MyEmployeesComponent implements OnInit {
  public userServiceSubscription: Subscription;
  public myEmployees: UserModel[];
  constructor(
    private userService: UsersService,
    private appGlobals: AppGlobals,
    private tokenStorage: TokenStorage
  ) {}

  ngOnInit() {
    this.userServiceSubscription = this.userService.usersSubject.subscribe(
      (emittedData: UserModel []) => {
         this.myEmployees = emittedData;
      }
    );
    const email = this.appGlobals.getUserEmail(this.tokenStorage.getToken());
    this.userService.getManagerEmployees(email);
  }
}
