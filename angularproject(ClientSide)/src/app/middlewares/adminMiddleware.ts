import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Injectable } from '@angular/core';
import { TokenStorage } from '../services/token.storage';
import { AppGlobals } from '../globals/app.globals';
import { Location } from '@angular/common';
@Injectable()
export class AdminMiddleware implements CanActivate {
    constructor(private location: Location, private tokenStorage: TokenStorage, private appGlobals: AppGlobals) {}
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        const role = this.appGlobals.getUserRole(this.tokenStorage.getToken());
       if (this.appGlobals.isAdmin(role)) {
           return true;
       }
       this.location.back();
       return false;
    }
}
