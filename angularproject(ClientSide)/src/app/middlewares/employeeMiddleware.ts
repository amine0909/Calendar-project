import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { AppGlobals } from '../globals/app.globals';
import { TokenStorage } from '../services/token.storage';
import { Location } from '@angular/common';
@Injectable()
export class EmployeeMiddleware implements CanActivate {
    constructor(private location: Location, private appGlobals: AppGlobals, private tokenStorage: TokenStorage) {}
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        const role = this.appGlobals.getUserRole(this.tokenStorage.getToken());
        if (this.appGlobals.isEmployee(role)) {
            return true;
        }
        this.location.back();        
        return false;
    }
 }
