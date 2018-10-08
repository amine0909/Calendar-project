import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { LoginUser } from '../models/loginUser';
import { AppGlobals } from '../globals/app.globals';
import { Subject } from 'rxjs/Subject';
import { TokenStorage } from './token.storage';
import { Observable } from 'rxjs/Observable';

import { TokenModel } from '../models/token';
@Injectable()
export class AuthService {
  public authSubject: Subject<any> = new Subject<any>();
  private token: TokenModel;
  constructor(
    private httpClient: HttpClient,
    private globals: AppGlobals,
    private tokenStorage: TokenStorage,
  ) {}
  public attemptAuth(loginUser: LoginUser) {
    this.httpClient
      .post(
        this.globals.ApiBaseUrl + "/token/generate-token",
        loginUser
      )
      .subscribe(
        (successData: TokenModel) => {
          this.saveAuthCredentials(successData);
          this.emitAuthSubject(true);
          let appGlobal = new AppGlobals();
          localStorage.setItem("id",appGlobal.getUserIdentifier(successData.token).toString());
        },
        (errorData: HttpErrorResponse) => {
          if (errorData.hasOwnProperty('error')) {
               this.emitAuthSubject(errorData.error["message"]);
          } else {
              this.emitAuthSubject(false);
          }
        }
      );
  }

  emitAuthSubject(data) {
    this.authSubject.next(data);
  }

  saveAuthCredentials(tokenModel: TokenModel): void {
    this.tokenStorage.saveToken(tokenModel.token);
    this.tokenStorage.saveExpirationDate(tokenModel.expirationDateAndTime);
  }
}
