import { Component, OnInit } from '@angular/core';
import { UsersService } from '../services/users.service';
import { Subscription } from 'rxjs/Subscription';
import { UserModel } from '../models/user';
import { AppGlobals } from '../globals/app.globals';
import { TokenStorage } from '../services/token.storage';

@Component({
  selector: "app-all-users",
  templateUrl: "./all-users.component.html",
  styleUrls: ["./all-users.component.css"]
})
export class AllUsersComponent implements OnInit {
  private userServiceSubscription: Subscription;
  public users: UserModel[];
  public myEmail = "";
  public idToSupposeDelete: number;
  public disableNextButton = false;
  public disablePreviousButton = false;

  constructor(
    private userService: UsersService,
    private appGlobals: AppGlobals,
    private tokenStorage: TokenStorage
  ) {}

  ngOnInit() {
    this.userService.getNextTenUsers(-1);
    this.subscribeToUserService();
    this.idToSupposeDelete = this.appGlobals.getUserIdentifier(this.tokenStorage.getToken());
  }

  subscribeToUserService() {
    this.userServiceSubscription = this.userService.usersSubject.subscribe(
      (emittedData: UserModel[]) => {
            this.users = emittedData;
            this.getUserEmail();
      }
    );
  }

  setIdToSupposeDelete(id: number) {
    this.idToSupposeDelete = +id;
  }


  onDeleteUser() {
    this.userService
      .deleteUser(this.idToSupposeDelete)
      .then(resolvedData => {});
    document.getElementById("" + this.idToSupposeDelete + "").remove();
  }

  getUserEmail() {
    this.myEmail = this.appGlobals.getUserEmail(this.tokenStorage.getToken());
  }

  getNextUsersPage() {
   const lastUserId = this.users[this.users.length - 1].id;
   this.userService.getNextTenUsers(lastUserId);
   this.userServiceSubscription = this.userService.usersSubject.subscribe(
     (emittedData: UserModel[]) => {
       if (emittedData.length !== 0) {
         this.users = emittedData;
       } else {
         this.disableNextButton = true;
         this.disablePreviousButton = false;
       }
     }
   );
  }

  getPreviousUsersPage() {
    const firstUserId = this.users[0].id;
    this.userService.getPreviousTenUsers(firstUserId);
      this.userServiceSubscription = this.userService.usersSubject.subscribe(
        (emittedData: UserModel[]) => {
          if (emittedData.length !== 0) {
            this.users = emittedData;
          } else {
            this.disablePreviousButton = true;
            this.disableNextButton = false;
          }
        }
      );
  }
}
