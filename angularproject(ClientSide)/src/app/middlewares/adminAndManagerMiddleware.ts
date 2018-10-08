import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate } from '@angular/router';
import { TokenStorage } from "../services/token.storage";
import { Observable } from "rxjs/Observable";
import { Location } from '@angular/common';
import { AppGlobals } from "../globals/app.globals";
import { Injectable } from '@angular/core';
@Injectable()
export class AdminAndManagerMiddleware implements CanActivate {
  constructor(
    private location: Location,
    private tokenStorage: TokenStorage,
    private appGlobals: AppGlobals
  ) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    const role = this.appGlobals.getUserRole(this.tokenStorage.getToken());
    if (this.appGlobals.isAdmin(role) || this.appGlobals.isManager(role)) {
      return true;
    }
    this.location.back();
    return false;
  }
}
