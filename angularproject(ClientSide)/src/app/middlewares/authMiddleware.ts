import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { AuthService } from '../services/authentification.service';
import { Injectable } from '@angular/core';
import { TokenStorage } from '../services/token.storage';
@Injectable()
export class AuthMiddleware implements CanActivate {
    constructor(private authService: AuthService, private router: Router, private tokenStorage: TokenStorage) {}
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
         if (this.tokenStorage.isLoggedIn()) {
            return true;
        }
         this.router.navigateByUrl('/Login');
         return false;
    }
}
