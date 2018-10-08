import { OnInit, Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { UserModel } from '../models/user';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { AppGlobals } from '../globals/app.globals';
import { Router } from '@angular/router';
import { TokenStorage } from './token.storage';
@Injectable()
export class UsersService implements OnInit {
  public usersSubject =  new Subject<UserModel[]>();
  private users: UserModel[];
  constructor(private httpClient: HttpClient, private appGlobals: AppGlobals, private tokenStorage: TokenStorage, private router: Router) {}
  ngOnInit() {
  }

  addUser(user: UserModel) {
    const token = this.tokenStorage.getToken();
    return new Promise((resolve, reject) => {
    this.httpClient
      .post<UserModel>(
        this.appGlobals.ApiBaseUrl + '/users/adduser',
        user
      )
      .subscribe(
        successData => {
          resolve(successData);
        },
        errorData => {
          reject(errorData);
        }
      );
    });
  }

  deleteUser(userId: number) {
    return new Promise<boolean>((resolve, reject) => {
      this.httpClient
        .delete(this.appGlobals.ApiBaseUrl + "/users/delete/" + userId)
        .subscribe(successData => {
           resolve(true);
        }, errorData => {
           resolve(false);
        });
    });
  }

  updateUser(userId: number, user: UserModel) {
    return new Promise<boolean>((resolve, reject) => {
    this.httpClient
      .put(
        this.appGlobals.ApiBaseUrl + "/users/update/" + userId,
        user
      ).subscribe(
         (successData) => {
           resolve(true);
      }, (errorData) => {
           resolve(false);
      });
    });
  }
  getUser() {}
  getAllUsers() {
    this.httpClient.get<UserModel[]>(this.appGlobals.ApiBaseUrl + '/users/all').subscribe(
      (dataSuccess: UserModel[]) => {
        this.users = dataSuccess;
        this.emitUsersSubject();
      }, (errorResponse: HttpErrorResponse) => {
          this.defaultHttpErrorResponseParser(errorResponse);
      }
    );
  }

  defaultHttpErrorResponseParser(httpErrorResponse: HttpErrorResponse) {
      if (httpErrorResponse.status === 401) {
         this.tokenStorage.logItOut();
         this.router.navigateByUrl("/Login");
      }
  }

  getOneUserById(userId: number): Promise<UserModel> {
    return new Promise<UserModel>((resolve, reject) => {
        this.httpClient.get<UserModel>(this.appGlobals.ApiBaseUrl + '/users/get/' + userId).subscribe(
          (user: UserModel) => {
            resolve(user);
          }, (error) => {
            resolve(null);
          }
        );
    });
  }

  getBosses() {
    this.httpClient.get<UserModel[]>(this.appGlobals.ApiBaseUrl + '/users/bosses').subscribe(
      (users: UserModel[]) => {
         this.users = users;
         this.emitUsersSubject();
      }, (errorData) => {
        console.log(' error when getting all bosses');
        console.log(errorData);
      }
    );
  }

  findUserByIdFromList(id: number): UserModel {
   return  this.users.find((user) => {
      return Number(user.id) === id;
    });
  }

  findUserByEmailFromList(email: string) {
    return this.users.find((user) => {
        return user.email === email;
    });
  }

  getNextTenUsers(id: number) {
    this.httpClient.get<UserModel[]>(this.appGlobals.ApiBaseUrl + '/users/next/' + id).subscribe(
      (nextUsers: UserModel[]) => {
        if (nextUsers.length !== 0) {
          this.users = nextUsers;
          this.emitUsersSubject();
        }
      }, (previousUsers: UserModel[]) => {
          console.log('error when paginate from service');
      }
    );
  }

  getManagerEmployees(email: string) {
     this.httpClient
       .post(
         this.appGlobals.ApiBaseUrl + "/users/manageremployees", email
       )
       .subscribe(
         (nextUsers: UserModel[]) => {
           this.users = nextUsers;
           this.emitUsersSubject();
         },
         (previousUsers: UserModel[]) => {
           console.log("error when paginate from service");
         }
       );
  }

  getPreviousTenUsers(id: number) {
    this.httpClient.get<UserModel[]>(this.appGlobals.ApiBaseUrl + '/users/previous/' + id ).subscribe(
      (previousUsers: UserModel[]) => {
        if (previousUsers.length !== 0) {
            this.users = previousUsers;
            this.emitUsersSubject();
        }
      }, (previousUsers: UserModel[]) => {
          console.log('error when paginate from service');
      }
    );
  }

  emitUsersSubject() {
    this.usersSubject.next(this.users.slice());
  }
}
