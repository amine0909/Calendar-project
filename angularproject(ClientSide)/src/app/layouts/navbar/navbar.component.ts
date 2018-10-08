import { Component, OnInit } from '@angular/core';
import { TokenStorage } from '../../services/token.storage';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { AuthService } from '../../services/authentification.service';
import * as moment from "moment";
import { Router } from '@angular/router';
import { AppGlobals } from '../../globals/app.globals';

@Component({
  selector: "layout-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.css"]
})
export class NavbarComponent implements OnInit {
  public showLoginLink = true;
  public showCalendarLink = false;
  public showSignupLink = true;
  public showHomeLink = false;
  public isLoggedIn = false;
  public showAllUsersLink = false;
  public showMyEmployees = false;
  public authSubscription: Subscription;
  constructor(
    private tokenStorage: TokenStorage,
    private authService: AuthService,
    private router: Router,
    private appGlobals: AppGlobals
  ) {}

  ngOnInit() {
    this.authSubscription = this.authService.authSubject.subscribe(
      emittedData => {
        if (emittedData === true) {
          this.whenLoggedIn();
        }
      }
    );
    if (this.tokenStorage.isLoggedIn() === true) {
      this.whenLoggedIn();
    } else {
      this.whenLoggedOut();
    }
  }

  whenLoggedIn() {
    const role = this.appGlobals.getUserRole(this.tokenStorage.getToken());
    if (this.appGlobals.isAdmin(role)) {
      this.showSignupLink = true;
      this.showAllUsersLink = true;
    } else if (this.appGlobals.isManager(role)) {
      this.showSignupLink = true;
      this.showAllUsersLink = true;
      this.showMyEmployees = true;
    } else {
      this.showSignupLink = false;
    }
    this.showLoginLink = false;
    this.showCalendarLink = true;
    this.showHomeLink = true;
    this.isLoggedIn = true;
  }

  whenLoggedOut() {
    this.showLoginLink = true;
    this.showCalendarLink = false;
    this.showSignupLink = false;
    this.showHomeLink = false;
    this.isLoggedIn = false;
  }

  onDeconnect() {
    this.tokenStorage.signOut();
    this.whenLoggedOut();
    this.router.navigateByUrl("/Login");
  }

  openNav() {
    document.getElementById("mySidenav").style.width = "250px";
  }

  closeNav() {
    document.getElementById("mySidenav").style.width = "0";
  }
}
