import { TokenStorage } from './token.storage';
import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpSentEvent,
  HttpHeaderResponse,
  HttpProgressEvent,
  HttpResponse,
  HttpUserEvent,
  HttpErrorResponse,
  HttpEvent,
  HttpEventType
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/operator/do';
import { DateFormatter } from 'ngx-bootstrap/datepicker';
const TOKEN_HEADER_KEY = 'Authorization';
@Injectable()
export class Interceptor implements HttpInterceptor {
    constructor(private router: Router, private tokenStorage: TokenStorage)  {}
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
         const token = this.tokenStorage.getToken();
         if (token != null  && this.tokenStorage.isLoggedIn()) {
             const cloned = req.clone({
                 headers: req.headers.set(TOKEN_HEADER_KEY, 'Bearer ' + token)
              });
         return next.handle(cloned);
        } else {
            this.router.navigate(['Login']);
            return next.handle(req);
        }
  }
}
